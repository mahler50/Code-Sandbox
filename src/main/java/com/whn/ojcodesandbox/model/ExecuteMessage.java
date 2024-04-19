package com.whn.ojcodesandbox.model;

import lombok.Builder;
import lombok.Data;

/**
 *  进程处理信息工具类
 */
@Data
@Builder
public class ExecuteMessage {
    private Integer exitCode;

    private String message;

    private String errorMessage;

    private Long time;
}
