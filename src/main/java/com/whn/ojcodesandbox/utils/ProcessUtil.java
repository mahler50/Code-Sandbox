package com.whn.ojcodesandbox.utils;

import cn.hutool.core.util.StrUtil;
import com.whn.ojcodesandbox.model.ExecuteMessage;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;

/**
 * 进程工具类
 */
@Slf4j
public class ProcessUtil {
    /**
     * 执行进程，获取信息
     * @param runProcess
     * @param operationName
     * @return
     */
    public static ExecuteMessage handleProcessMessage(Process runProcess, String operationName) {
        int exitCode;
        StringBuilder output = new StringBuilder();
        StringBuilder errorOutput = new StringBuilder();
        try {
            exitCode = runProcess.waitFor();
            if (exitCode == 0) {
                log.info(operationName + "成功");
            } else {
                log.error(operationName + "失败，错误码为: {}", exitCode);
                BufferedReader errorBufferedReader = new BufferedReader(new InputStreamReader(runProcess.getErrorStream()));
                String errorRunOutputLine;
                while ((errorRunOutputLine = errorBufferedReader.readLine()) != null) {
                    errorOutput.append(errorRunOutputLine);
                }
                log.error("错误输出为：{}", errorOutput);
            }
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(runProcess.getInputStream()));
            String runOutputLine;
            while ((runOutputLine = bufferedReader.readLine()) != null) {
                output.append(runOutputLine);
            }
            if (StrUtil.isNotBlank(output)) {
                log.info("正常输出：{}", output);
            }
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
        return ExecuteMessage.builder()
                .exitCode(exitCode)
                .message(output.toString())
                .errorMessage(errorOutput.toString())
                .build();
    }

    public static ExecuteMessage handleProcessInteraction(Process runProcess, String input, String operationName) {
        OutputStream outputStream = runProcess.getOutputStream();

        try {
            outputStream.write((input + "\n").getBytes());
            outputStream.flush();
            outputStream.close();
            return handleProcessMessage(runProcess, operationName);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                outputStream.close();
            } catch (IOException e) {
                log.error("关闭输入流失败");
            }
        }
    }

}
