package org.openmbee.services.sparql.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("/healthcheck")
public class HealthCheck {

    @GetMapping
    public String ping() {
        return "ok";
    }
}
