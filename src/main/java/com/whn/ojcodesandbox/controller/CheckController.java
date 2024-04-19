package com.whn.ojcodesandbox.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("/")
public class CheckController {
    @GetMapping("/health")
    public String checkHealth(){
        return "ok";
    }
}
