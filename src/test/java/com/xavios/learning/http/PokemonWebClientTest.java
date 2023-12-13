package com.xavios.learning.http;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockserver.integration.ClientAndServer;
import org.springframework.web.reactive.function.client.WebClient;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;
import static org.mockserver.model.JsonBody.json;

public class PokemonWebClientTest {
    @Test
    void it_should_return_not_null() {

        assertThat(pokemonWebClient.sendGet())
                .as("It should return not null")
                .isNotNull();
    }

    @BeforeEach
    void startServer() {
        server = ClientAndServer.startClientAndServer(FIRST_AVAILABLE_PORT);
        var port = server.getPort();
        var webclient = new WebClientConfiguration().pokemonWebClient("http://127.0.0.1:" + port, WebClient.builder());
        pokemonWebClient = new PokemonWebClient(webclient);
    }


    private PokemonWebClient pokemonWebClient;

    // using mockserver-netty to Testing
    private static ClientAndServer server;
    private static final Integer FIRST_AVAILABLE_PORT = 0;
    private static final String PATH = "/api/pokemon";
}
