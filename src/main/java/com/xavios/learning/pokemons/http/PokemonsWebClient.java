package com.xavios.learning.pokemons.http;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

public class PokemonsWebClient {

    public Mono<Map<String, Object>> getAllPokemons() {
        return webClient
                .get()
                .uri("https://pokeapi.co/api/v2/pokemon")
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<>() {
                });
    }

    public PokemonsWebClient(WebClient webClient) {
        this.webClient = webClient;
    }

    private final WebClient webClient;
}
