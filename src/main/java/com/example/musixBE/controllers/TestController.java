package com.example.musixBE.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class TestController {
    // test1
    @ResponseBody
    String home() {
        return "Hello World!";
    }
}