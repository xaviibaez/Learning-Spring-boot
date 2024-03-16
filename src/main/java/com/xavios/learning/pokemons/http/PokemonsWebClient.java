package com.xavios.learning.pokemons.http;

import io.netty.channel.ChannelOption;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.netty.http.HttpProtocol;
import reactor.netty.http.client.HttpClient;
import reactor.netty.resources.ConnectionProvider;

import java.time.Duration;
import java.util.Map;

@Component
public class PokemonsWebClient {

    public Mono<Map<String, Object>> getPokemons(int offset) {
        return webClient
                .get()
                .uri(uriBuilder -> uriBuilder
                        .queryParam("offset", String.valueOf(offset))
                        .queryParam("limit", String.valueOf(20))
                        .build()
                )
                .retrieve()
                .onStatus(HttpStatusCode::isError, error -> error
                        .bodyToMono(String.class)
                        .defaultIfEmpty("Error")
                        .flatMap(body -> Mono.error(
                                new ClientException(
                                        String.valueOf(error.statusCode().value()), body)
                        )))
                .bodyToMono(new ParameterizedTypeReference<>() {
                });
    }

    public Mono<Map<String, Object>> getPokemonDetail(String name) {
        return webClient
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path("/" + name)
                        .build()
                )
                .retrieve()
                .onStatus(HttpStatusCode::isError, error -> error
                        .bodyToMono(String.class)
                        .defaultIfEmpty("Error")
                        .flatMap(body -> Mono.error(
                                new ClientException(
                                        String.valueOf(error.statusCode().value()), body)
                        )))
                .bodyToMono(new ParameterizedTypeReference<>() {
                });
    }

    public PokemonsWebClient(@Autowired WebClient.Builder webClientBuilder, @Value("${pokemon_web_client}") String host) {
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
                .baseUrl(host)
                .build();
    }

    private final WebClient webClient;
}