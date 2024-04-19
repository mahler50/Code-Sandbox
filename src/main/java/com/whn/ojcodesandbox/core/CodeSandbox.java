package com.whn.ojcodesandbox.core;


import com.whn.ojcodesandbox.model.ExecuteCodeRequest;
import com.whn.ojcodesandbox.model.ExecuteCodeResponse;

/**
 * 代码沙箱
 */
public interface CodeSandbox {
    /**
     * 执行代码
     *
     * @param executeCodeRequest 执行代码请求
     * @return {@link ExecuteCodeResponse}
     */
    ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest);
}
