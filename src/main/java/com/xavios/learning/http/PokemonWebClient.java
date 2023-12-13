package com.xavios.learning.http;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Map;

@Component
public class PokemonWebClient {

    public Mono<Map<String, Object>> sendGet() {
        return Mono.empty();
    }

    public PokemonWebClient(@Qualifier("pokemon-web-client") WebClient webClient) {
        this.webClient = webClient;
    }

    private final WebClient webClient;
}
