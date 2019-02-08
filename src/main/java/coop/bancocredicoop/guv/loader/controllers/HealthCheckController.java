package coop.bancocredicoop.guv.loader.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class HealthCheckController {

    @GetMapping(value = "/healthCheck")
    public Mono<String> healthCheck() {
        return Mono.just("loader is running");
    }
}
