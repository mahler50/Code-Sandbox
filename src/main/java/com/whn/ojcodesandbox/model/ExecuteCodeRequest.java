package com.whn.ojcodesandbox.model;

import com.whn.ojcodesandbox.model.enums.QuestionSubmitLanguageEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 执行代码请求
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExecuteCodeRequest {
    private List<String> inputList;

    /**
     * 代码
     */
    private String code;

    /**
     * 代码语言
     */
    private QuestionSubmitLanguageEnum language;
}
