package com.xavios.learning.http;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockserver.integration.ClientAndServer;
import org.mockserver.model.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.test.StepVerifier;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;

public class PokemonWebClientTest {
    @Test
    void it_should_return_not_null() {

        assertThat(pokemonWebClient.sendGet())
                .as("It should return not null")
                .isNotNull();
    }

    @Test
    void it_should_return_expected_response() {
        setupResponseClient(PATH, "GET", 200, RESPONSE_OK);

        StepVerifier.create(pokemonWebClient.sendGet())
                .as("It should return expected response")
                .expectNext(RESPONSE_OK)
                .verifyComplete();
    }

    @BeforeEach
    void startServer() {
        server = ClientAndServer.startClientAndServer(FIRST_AVAILABLE_PORT);
        var port = server.getPort();
        var webclient = new WebClientConfiguration().pokemonWebClient("https://pokeapi.co", WebClient.builder());
        pokemonWebClient = new PokemonWebClient(webclient);
    }

    @AfterEach
    void stopServer() {
        server.stop();
    }

    private void setupResponseClient(String path, String typeMethod, Integer statusCode, Map<String, Object> body) {
        server.when(request()
                        .withMethod(typeMethod)
                        .withPath(path))
                .respond(response()
                        .withStatusCode(statusCode)
                        .withContentType(MediaType.APPLICATION_JSON)
                        .withBody(mapToJsonString(body)));
    }

    private String mapToJsonString(Map<String, Object> body) {
        try {
            return new ObjectMapper().writeValueAsString(body);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException(e);
        }
    }

    private PokemonWebClient pokemonWebClient;

    // using mockserver-netty to Testing
    private static ClientAndServer server;
    private static final Integer FIRST_AVAILABLE_PORT = 0;
    private static final String PATH = "/api/v2/pokemon";
    private static final Map<String, Object> RESPONSE_OK = Map.of("response", "ok");
    //TODO: deberia mockear todo?
}
