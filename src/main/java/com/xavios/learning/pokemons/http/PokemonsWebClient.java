package com.xavios.learning.pokemons.http;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Repository;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

@Repository
public class PokemonsWebClient {

    public Mono<List<Map<String, Object>>> getAllPokemons() {
        return webClient
                .get()
                .uri("api/v2/pokemon")
                .retrieve()
                .onStatus(
                        httpStatusCode -> {
                            if (httpStatusCode.isError())
                                System.out.println("error");

                            return true;
                        },
                        clientResponse -> {
                            new Exception(clientResponse.toString());
                            return "test";
                        })
                .bodyToMono(new ParameterizedTypeReference<>() {
                });
    }

    public PokemonsWebClient(@Qualifier("pokemons-web-client") WebClient webClient) {
        this.webClient = webClient;
    }

    private final WebClient webClient;
}
