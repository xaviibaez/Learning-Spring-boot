package com.xavios.learning.pokemons.http;

import io.netty.channel.ChannelOption;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.HttpProtocol;
import reactor.netty.http.client.HttpClient;
import reactor.netty.resources.ConnectionProvider;

import java.time.Duration;

@Configuration
public class PokemonsWebClientConfiguration {

    @Bean("pokemons-web-client")
    public PokemonsWebClient getPokemonsWebClient() {
        return new PokemonsWebClient(getWebClient(getHttpClient()));
    }

    private HttpClient getHttpClient() {
        var connectionProvider = ConnectionProvider.builder("pokemonClient" + "ConnectionPool")
                .maxConnections(1000)
                .pendingAcquireMaxCount(2000)
                .build();

        return HttpClient.create(connectionProvider)
                .protocol(HttpProtocol.HTTP11)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 500)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .responseTimeout(Duration.ofMillis(400))
                .resolver(spec -> spec.roundRobinSelection(true))
                .wiretap(true);
    }

    private WebClient getWebClient(HttpClient httpClient) {
        return webClientBuilder
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .build();
    }

    private final WebClient.Builder webClientBuilder = WebClient.builder();
}
