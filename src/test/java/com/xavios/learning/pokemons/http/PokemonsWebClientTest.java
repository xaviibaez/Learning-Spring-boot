package com.xavios.learning.pokemons.http;

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

class PokemonsWebClientTest {

    @Test
    void it_should_return_not_null() {
        assertThat(pokemonsWebClient.getAllPokemons(0))
                .as("It should return not null")
                .isNotNull();
    }

    @Test
    void it_should_return_expected_response() {
        setupResponseClient(RESPONSE_OK, 200);

        StepVerifier.create(pokemonsWebClient.getAllPokemons(0))
                .as("It should return expected response")
                .expectNext(RESPONSE_OK)
                .verifyComplete();
    }

    @Test
    void it_should_return_error_when_have_one_error_in_client() {
        setupResponseClient(RESPONSE_KO, 500);

        StepVerifier.create(pokemonsWebClient.getAllPokemons(0))
                .as("It should return error")
                .verifyError(ClientException.class);
    }

    @Test
    void it_should_return_expected_exception_when_have_one_error_in_client() {
        setupResponseClient(RESPONSE_KO, 500);

        StepVerifier.create(pokemonsWebClient.getAllPokemons(0))
                .as("It should return expected error response")
                .verifyErrorMessage(RESPONSE_KO_STRING);
    }

    @Test
    void it_should_return_expected_error_code_when_have_one_error_in_client() {
        setupResponseClient(RESPONSE_KO, 500);

        StepVerifier.create(pokemonsWebClient.getAllPokemons(0))
                .as("It should return expected error response code")
                .verifyErrorSatisfies(error ->
                        assertThat(((ClientException) error).getHttpStatusCode())
                                .as("It should return error 500")
                                .isEqualTo("500")
                );
    }

    @BeforeEach
    void startServer() {
        server = ClientAndServer.startClientAndServer(0);
        var port = server.getPort();
        pokemonsWebClient = new PokemonsWebClient(WebClient.builder(), "http://127.0.0.1:" + port + "/api/v2/pokemon");
    }

    @AfterEach
    void stopServer() {
        server.stop();
    }

    private void setupResponseClient(Map<String, Object> response, Integer statusCode) {
        server.when(request()
                        .withMethod("GET")
                        .withPath("/api/v2/pokemon"))
                .respond(response()
                        .withStatusCode(statusCode)
                        .withContentType(MediaType.APPLICATION_JSON)
                        .withBody(mapToJsonString(response)));
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
    private static final Map<String, Object> RESPONSE_KO = Map.of("response", "ko");
    private static final String RESPONSE_KO_STRING = "{\"response\":\"ko\"} - 500";
}