package com.buildbuddy.application.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;
@RestController
@RequestMapping("/health")
@RequiredArgsConstructor
public class HealthCheck {

    @GetMapping
    public String healthCheck() {
        return "Connection connecting fine ***";
    }
}

