package com.whn.ojcodesandbox.controller;

import com.whn.ojcodesandbox.core.CodeSandboxFactory;
import com.whn.ojcodesandbox.core.CodeSandboxTemplate;
import com.whn.ojcodesandbox.model.ExecuteCodeRequest;
import com.whn.ojcodesandbox.model.ExecuteCodeResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/codesandbox")
public class CodeSandboxController {
    // 定义鉴权请求头
    private static final String AUTH_REQUEST_HEADER = "auth";
    private static final String AUTH_REQUEST_SECRET = "d2huX3NlY3JldF9rZXk=";

    @PostMapping("/execute")
    public ExecuteCodeResponse executeCode(@RequestBody ExecuteCodeRequest executeCodeRequest, HttpServletRequest request,
                                           HttpServletResponse response) {
        String authHeader = request.getHeader(AUTH_REQUEST_HEADER);
        if(!AUTH_REQUEST_SECRET.equals(authHeader)) {
            response.setStatus(403);
            return null;
        }
        CodeSandboxTemplate codeSandboxTemplate = CodeSandboxFactory.getInstance(executeCodeRequest.getLanguage());
        return codeSandboxTemplate.executeCode(executeCodeRequest);
    }
}
