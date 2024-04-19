package com.whn.ojcodesandbox.core;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.ObjectUtil;
import com.whn.ojcodesandbox.model.*;
import com.whn.ojcodesandbox.utils.ProcessUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StopWatch;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

/**
 * 代码沙箱模板
 */
@Slf4j
public abstract class CodeSandboxTemplate implements CodeSandbox{

    String prefix;

    String globalCodeDirPath;

    String globalCodeFileName;

    /**
     * 超时时间，超过10秒则结束
     */
    public static final Long DEFAULT_TIME_OUT = 10000L;

    /**
     * 每一个类必须实现的代码编译及运行的Cmd
     * @param userCodeParentPath
     * @param userCodePath
     * @return
     */
    abstract CodeSandboxCmd getCmd(String userCodeParentPath, String userCodePath);

    /**
     * 保存用户代码到文件中，不同编程语言放至不同的文件夹
     * @param code
     * @return
     */
    private File saveCodeToFile(String code) {
        String globalCodePath = System.getProperty("user.dir") + globalCodeDirPath;
        if (!FileUtil.exist(globalCodePath)) {
            FileUtil.mkdir(globalCodePath);
        }
        // 存放用户代码
        String userCodeParentPath = globalCodePath + prefix + File.separator + UUID.randomUUID();
        String userCodePath = userCodeParentPath + globalCodeFileName;
        return FileUtil.writeString(code, userCodePath, StandardCharsets.UTF_8);
    }

    /**
     * 编译代码
     * @param compileCode
     * @return
     * @throws IOException
     */
    private ExecuteMessage compileCode(String compileCode) throws IOException {
        Process compileProcess = Runtime.getRuntime().exec(compileCode);
        return ProcessUtil.handleProcessMessage(compileProcess, "编译");
    }

    /**
     * 运行代码
     * @param inputList
     * @param runCmd
     * @return
     * @throws RuntimeException
     */
    private List<ExecuteMessage> runCode(List<String> inputList, String runCmd) throws RuntimeException{
        List<ExecuteMessage> executeMessageList = new LinkedList<>();
        for (String input :
                inputList) {
            Process runProcess;
            Thread computTimeThread;
            try{
                runProcess = Runtime.getRuntime().exec(runCmd);
                computTimeThread = new Thread(() -> {
                    try {
                        Thread.sleep(DEFAULT_TIME_OUT);
                        if (runProcess.isAlive()) {
                            log.info("超时了，中断");
                            runProcess.destroy();
                        }
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                });
                computTimeThread.start();
                StopWatch stopWatch = new StopWatch();
                stopWatch.start();
                ExecuteMessage executeMessage = ProcessUtil.handleProcessInteraction(runProcess, input, "运行");
                stopWatch.stop();
                computTimeThread.stop();
                executeMessage.setTime(stopWatch.getLastTaskTimeMillis());
                executeMessageList.add(executeMessage);
            } catch (IOException e) {
                throw new RuntimeException();
            }
        }
        return executeMessageList;
    }

    @Override
    public ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest) {
        List<String> inputList = executeCodeRequest.getInputList();
        String code = executeCodeRequest.getCode();
        // 保存代码
        File userCodeFile = saveCodeToFile(code);
        String userCodePath = userCodeFile.getAbsolutePath();
        String userCodeParentPath = userCodeFile.getParentFile().getAbsolutePath();
        CodeSandboxCmd codeSandboxCmd = getCmd(userCodeParentPath, userCodePath);
        String compileCmd = codeSandboxCmd.getCompileCmd();
        String runCmd = codeSandboxCmd.getRunCmd();
        // 编译代码
        try {
            ExecuteMessage executeMessage = compileCode(compileCmd);
            if (executeMessage.getExitCode() != 0) {
                FileUtil.del(userCodeParentPath);
                return ExecuteCodeResponse
                        .builder()
                        .status(2)
                        .message("编译错误")
                        .build();
            }
        } catch (IOException e) {
            FileUtil.del(userCodeParentPath);
            throw new RuntimeException(e);
        }
        // 执行代码
        try {
            List<ExecuteMessage> executeMessageList = runCode(inputList, runCmd);
            // 返回处理结果
            ExecuteCodeResponse executeCodeResponse = new ExecuteCodeResponse();
            executeCodeResponse.setStatus(1);
            JudgeInfo judgeInfo = new JudgeInfo();
            executeCodeResponse.setJudgeInfo(judgeInfo);
            List<String> outputList = new LinkedList<>();
            long maxTime = 0;

            for (ExecuteMessage executeMessage :
                    executeMessageList) {
                if (ObjectUtil.equal(0, executeMessage.getExitCode())) {
                    outputList.add(executeMessage.getMessage());
                } else {
                    executeCodeResponse.setMessage(executeMessage.getMessage());
                    executeCodeResponse.setStatus(3);
                    break;
                }
                maxTime = Math.max(maxTime, executeMessage.getTime());
            }
            judgeInfo.setTime(maxTime);
            executeCodeResponse.setOutputList(outputList);
            FileUtil.del(userCodeParentPath);
            return executeCodeResponse;
        } catch (RuntimeException e) {
            FileUtil.del(userCodeParentPath);
            return errorResponse(e);
        }
    }

    /**
     * 获取错误响应
     * @param e
     * @return
     */
    final ExecuteCodeResponse errorResponse(Throwable e) {
        return ExecuteCodeResponse
                .builder()
                .outputList(new ArrayList<>())
                .message(e.getMessage())
                .judgeInfo(new JudgeInfo())
                .status(2)
                .build();
    }
}

