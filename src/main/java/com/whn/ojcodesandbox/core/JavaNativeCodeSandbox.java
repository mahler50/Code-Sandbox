package com.whn.ojcodesandbox.core;

import java.io.File;
import com.whn.ojcodesandbox.model.CodeSandboxCmd;


public class JavaNativeCodeSandbox extends CodeSandboxTemplate {

    private static final String PREFIX = File.separator + "java";

    private static final String GLOBAL_CODE_DIR_PATH = File.separator + "tempCode";

    private static final String GLOBAL_JAVA_CLASS_NAME = File.separator + "Main.java";

    public JavaNativeCodeSandbox() {
        super.prefix = PREFIX;
        super.globalCodeDirPath = GLOBAL_CODE_DIR_PATH;
        super.globalCodeFileName = GLOBAL_JAVA_CLASS_NAME;
    }

    @Override
    public CodeSandboxCmd getCmd(String userCodeParentPath, String userCodePath) {
        return CodeSandboxCmd
                .builder()
                .compileCmd(String.format("javac -encoding utf-8 %s", userCodePath))
                .runCmd(String.format("java -Xmx256m -Dfile.encoding=UTF-8 -cp %s Main", userCodeParentPath))
                .build();
    }
}