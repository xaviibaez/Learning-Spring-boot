package com.xavios.learning.pokemons.http;

import io.netty.channel.ChannelOption;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.netty.http.HttpProtocol;
import reactor.netty.http.client.HttpClient;
import reactor.netty.resources.ConnectionProvider;

import java.time.Duration;
import java.util.Map;

public class PokemonsWebClient {

    public Mono<Map<String, Object>> getAllPokemons() {
        return webClient
                .get()
                .uri(uri)
                .retrieve()
                .onStatus(x -> x.isError(), error -> {
                    return error
                            .bodyToMono(String.class)
                            .defaultIfEmpty("Error")
                            .flatMap(body -> Mono.error(
                                    new Exception(error.toString())
                            ));
                })
                .bodyToMono(new ParameterizedTypeReference<>() {
                });
    }

    public PokemonsWebClient(@Autowired WebClient.Builder webClientBuilder, String uri) {
        var connectionProvider = ConnectionProvider.builder("pokemonClientConnectionPool")
                .maxConnections(1000)
                .pendingAcquireMaxCount(2000)
                .build();

        var httpClient = HttpClient.create(connectionProvider)
                .protocol(HttpProtocol.HTTP11)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 500)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .responseTimeout(Duration.ofMillis(400))
                .resolver(spec -> spec.roundRobinSelection(true))
                .wiretap(true);

        this.webClient = webClientBuilder
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .build();
        this.uri = uri;
    }

    private final WebClient webClient;
    private final String uri;
}
