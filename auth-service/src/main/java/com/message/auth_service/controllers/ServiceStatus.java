package com.message.auth_service.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
//@RequestMapping("/")
public class ServiceStatus {

    @GetMapping("/a")
    public String sayHello(){
        return "Hello";
    }
    @GetMapping("/b")
    public String adminCheck(){
        return "Admin access";
    }
}
