package com.xavios.learning.pokemons.http;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockserver.integration.ClientAndServer;
import org.mockserver.model.MediaType;
import reactor.test.StepVerifier;

import java.util.List;
import java.util.Map;

import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;
import static org.assertj.core.api.Assertions.assertThat;

class PokemonsWebClientTest {

    @Test
    void it_should_return_not_null() {
        assertThat(pokemonsWebClient.getAllPokemons())
                .as("It should return not null")
                .isNotNull();
    }

    @Test
    void it_should_return_expected_response() {
        setupResponseClient();

        StepVerifier.create(pokemonsWebClient.getAllPokemons())
                .as("It should return expected response")
                .expectNext(RESPONSE_OK)
                .verifyComplete();
    }

    @BeforeEach
    void startServer() {
        server = ClientAndServer.startClientAndServer(0);
        pokemonsWebClient = new PokemonsWebClientConfiguration().getPokemonsWebClient();
    }

    @AfterEach
    void stopServer() {
        server.stop();
    }

    private void setupResponseClient() {
        server.when(request()
                        .withMethod("GET")
                        .withPath("https://pokeapi.co/api/v2/pokemon"))
                .respond(response()
                        .withStatusCode(200)
                        .withContentType(MediaType.APPLICATION_JSON)
                        .withBody(mapToJsonString(RESPONSE_OK)));
    }

    private String mapToJsonString(Map<String, Object> body) {
        try {
            return new ObjectMapper().writeValueAsString(body);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException(e);
        }
    }

    private PokemonsWebClient pokemonsWebClient;

    private static ClientAndServer server;
    private static final Map<String, Object> RESPONSE_OK = Map.of("response", "ok");
}