package com.xavios.learning.http;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Map;

@Component
public class PokemonWebClient {

    public Mono<Map<String, Object>> sendGet() {
        var uriTemplate = "https://pokeapi.co/api/v2/pokemon";
        return webClient
                .get()
                .uri(uriTemplate)
                .retrieve()
                .onStatus(HttpStatusCode::isError, this::httpErrorHandler)
                .bodyToMono(new ParameterizedTypeReference<>() {
                });
    }

    private Mono<? extends Throwable> httpErrorHandler(ClientResponse clientResponse) {
        return clientResponse
                .bodyToMono(String.class)
                .defaultIfEmpty("")
                .flatMap(body -> Mono.error(
                        new PokemonClientException(
                                String.valueOf(clientResponse.statusCode().value()), body)));
    }

    public PokemonWebClient(@Qualifier("pokemon-web-client") WebClient webClient) {
        this.webClient = webClient;
    }

    private final WebClient webClient;
}
