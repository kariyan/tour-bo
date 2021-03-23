package com.wemakeprice.tour.bo.service.controller.health;

import com.wemakeprice.tour.bo.common.annotation.NoLoginRequired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/healthcheck")
@RestController
public class HealthCheckController {

    @GetMapping("/_check")
    @ResponseStatus(value = HttpStatus.OK)
    @NoLoginRequired
    public String healthCheck() {
        return "ok";
    }

    @GetMapping("/health")
    @ResponseStatus(value = HttpStatus.OK)
    @NoLoginRequired
    public String healthCheckVersion2() {
        return "ok";
    }
}
