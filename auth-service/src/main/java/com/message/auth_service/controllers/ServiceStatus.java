package com.message.auth_service.controllers;
/**
 * To test whether service working or not
 */

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "1. Test APIs", description = "check whether service is up!")
public class ServiceStatus {

    @GetMapping("/test")
    @Operation(summary = "Returns message if service is up and responsive")
    public String sayHello() {
        return "Service Working..";
    }
}
