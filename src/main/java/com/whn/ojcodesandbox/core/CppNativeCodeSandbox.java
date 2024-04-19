package com.whn.ojcodesandbox.core;

import com.whn.ojcodesandbox.model.CodeSandboxCmd;

import java.io.File;

public class CppNativeCodeSandbox extends CodeSandboxTemplate{

    private static final String PREFIX = File.separator + "cpp";

    private static final String GLOBAL_CODE_DIR_PATH = File.separator + "tempCode";

    private static final String GLOBAL_CPP_NAME = File.separator + "main.cpp";

    public CppNativeCodeSandbox() {
        this.prefix = PREFIX;
        this.globalCodeDirPath = GLOBAL_CODE_DIR_PATH;
        this.globalCodeFileName = GLOBAL_CPP_NAME;
    }

    @Override
    CodeSandboxCmd getCmd(String userCodeParentPath, String userCodePath) {
        return CodeSandboxCmd.builder()
                .compileCmd(String.format("g++ -finput-charset=UTF-8 -fexec-charset=UTF-8 %s -o %s", userCodePath, userCodePath.substring(0, userCodePath.length() - 4)))
                .runCmd(userCodeParentPath + File.separator + "main")
                .build();
    }
}
