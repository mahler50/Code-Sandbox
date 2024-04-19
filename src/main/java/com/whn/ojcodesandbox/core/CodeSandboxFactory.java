package com.whn.ojcodesandbox.core;


import com.whn.ojcodesandbox.model.enums.QuestionSubmitLanguageEnum;

/**
 * 代码沙箱工厂
 */
public class CodeSandboxFactory {
    public static CodeSandboxTemplate getInstance(QuestionSubmitLanguageEnum language) {
        switch (language) {
            case JAVA:
                return new JavaNativeCodeSandbox();
            case CPP:
                return new CppNativeCodeSandbox();
            default:
                throw new RuntimeException("该语言暂不支持");
        }
    }
}