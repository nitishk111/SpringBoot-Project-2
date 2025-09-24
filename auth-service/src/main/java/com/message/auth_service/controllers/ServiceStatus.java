package com.message.auth_service.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ServiceStatus {

    @GetMapping("/")
    public String sayHello(){
        return "Hello";
    }
}
