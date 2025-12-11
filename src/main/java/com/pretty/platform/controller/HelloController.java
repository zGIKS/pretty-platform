package com.pretty.platform.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "Hello", description = "Hello World API")
public class HelloController {

    @GetMapping("/hello")
    @Operation(summary = "Get Hello World", description = "Returns a hello world message")
    public String hello() {
        return "Hello, World!";
    }

}