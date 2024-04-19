package com.whn.ojcodesandbox.core;

import com.whn.ojcodesandbox.model.CodeSandboxCmd;

import java.io.File;

public class GolangNativeCodeSandbox extends CodeSandboxTemplate{

    private static final String PREFIX = File.separator + "golang";

    private static final String GLOBAL_CODE_DIR_PATH = File.separator + "tempCode";

    private static final String GLOBAL_GOLANG_NAME = File.separator + "main.go";

    public GolangNativeCodeSandbox() {
        this.prefix = PREFIX;
        this.globalCodeDirPath = GLOBAL_CODE_DIR_PATH;
        this.globalCodeFileName = GLOBAL_GOLANG_NAME;
    }

    @Override
    CodeSandboxCmd getCmd(String userCodeParentPath, String userCodePath) {
        return null;
    }
}
