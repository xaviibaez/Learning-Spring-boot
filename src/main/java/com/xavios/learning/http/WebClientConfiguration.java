package com.xavios.learning.http;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.HttpProtocol;

@Configuration
public class WebClientConfiguration {

    @Bean("pokemon-web-client")
    public WebClient pokemonWebClient(@Value("${pokemon_web_client}") String host,
                                     @Autowired WebClient.Builder webClientBuilder) {
        var webClientFactory = new WebClientFactory(webClientBuilder,
                host,
                "pokemonClient",
                HttpProtocol.HTTP11);
        return webClientFactory.create();
    }
}
