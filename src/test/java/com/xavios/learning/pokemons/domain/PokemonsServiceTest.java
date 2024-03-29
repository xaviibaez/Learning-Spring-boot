package com.xavios.learning.pokemons.domain;

import com.xavios.learning.pokemons.http.PokemonsWebClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PokemonsServiceTest {

    @Test
    void it_should_return_not_null() {
        when(pokemonsWebClient.getPokemons(0)).thenReturn(Mono.just(POKEMONS_CLIENT_RESPONSE));

        assertThat(pokemonsService.getPokemons())
                .as("It should return not null")
                .isNotNull();
    }

    @Test
    void it_should_call_pokemons_web_client() {
        when(pokemonsWebClient.getPokemons(0)).thenReturn(Mono.just(POKEMONS_CLIENT_RESPONSE));

        StepVerifier.create(pokemonsService.getPokemons())
                .as("It should call pokemons web client")
                .assertNext(x -> verify(pokemonsWebClient, only()
                        .description("It should call pokemons web client"))
                        .getPokemons(0))
                .verifyComplete();
    }

    @Test
    void it_should_return_expected_pokemons_list() {
        when(pokemonsWebClient.getPokemons(0)).thenReturn(Mono.just(POKEMONS_CLIENT_RESPONSE));

        StepVerifier.create(pokemonsService.getPokemons())
                .as("it should return expected pokemons list")
                .expectNext(POKEMONS_SERVICE_ASSEMBLED_RESPONSE)
                .verifyComplete();
    }

    @Test
    void it_should_call_2_times_pokemons_client() {
        when(pokemonsWebClient.getPokemons(0)).thenReturn(Mono.just(POKEMONS_CLIENT_RESPONSE_PAGE_0));
        when(pokemonsWebClient.getPokemons(20)).thenReturn(Mono.just(POKEMONS_CLIENT_RESPONSE_PAGE_1));

        StepVerifier.create(pokemonsService.getPokemons())
                .as("It should call pokemons web client")
                .assertNext(x -> verify(pokemonsWebClient, times(2)
                        .description("It should call pokemons web client"))
                        .getPokemons(anyInt()))
                .verifyComplete();
    }

    @Test
    void it_should_return_expected_pokemons_list_paginated() {
        when(pokemonsWebClient.getPokemons(0)).thenReturn(Mono.just(POKEMONS_CLIENT_RESPONSE_PAGE_0));
        when(pokemonsWebClient.getPokemons(20)).thenReturn(Mono.just(POKEMONS_CLIENT_RESPONSE_PAGE_1));

        StepVerifier.create(pokemonsService.getPokemons())
                .as("it should return expected pokemons list paginated")
                .expectNext(POKEMONS_SERVICE_ASSEMBLED_RESPONSE_PAGE_0_1)
                .verifyComplete();
    }

    @BeforeEach
    void setup() {
        this.pokemonsService = new PokemonsService(pokemonsWebClient);
    }

    private PokemonsService pokemonsService;

    @Mock
    private PokemonsWebClient pokemonsWebClient;

    private final Map<String, Object> POKEMONS_CLIENT_RESPONSE = Map.of(
            "count", 2,
            "results", List.of(
                    Map.of("name", "bulbasaur", "url", "https://pokeapi.co/api/v2/pokemon/1/"),
                    Map.of("name", "ivysaur", "url", "https://pokeapi.co/api/v2/pokemon/2/")
            )
    );
    private final Map<String, Object> POKEMONS_CLIENT_RESPONSE_PAGE_0 = Map.of(
            "count", 4,
            "next", "https://pokeapi.co/api/v2/pokemon?offset=40&limit=20",
            "previous", "https://pokeapi.co/api/v2/pokemon?offset=20&limit=20",
            "results", List.of(
                    Map.of("name", "bulbasaur", "url", "https://pokeapi.co/api/v2/pokemon/1/"),
                    Map.of("name", "ivysaur", "url", "https://pokeapi.co/api/v2/pokemon/2/")
            )
    );
    private final Map<String, Object> POKEMONS_CLIENT_RESPONSE_PAGE_1 = Map.of(
            "count", 4,
            "next", "https://pokeapi.co/api/v2/pokemon?offset=40&limit=20",
            "previous", "https://pokeapi.co/api/v2/pokemon?offset=20&limit=20",
            "results", List.of(
                    Map.of("name", "venusaur", "url", "https://pokeapi.co/api/v2/pokemon/3/"),
                    Map.of("name", "charmander", "url", "https://pokeapi.co/api/v2/pokemon/4/")
            )
    );
    private final Map<String, Object> POKEMONS_SERVICE_ASSEMBLED_RESPONSE = Map.of(
            "pokemons", List.of(
                    Map.of("name", "bulbasaur", "url", "https://pokeapi.co/api/v2/pokemon/1/"),
                    Map.of("name", "ivysaur", "url", "https://pokeapi.co/api/v2/pokemon/2/")
            )
    );
    private final Map<String, Object> POKEMONS_SERVICE_ASSEMBLED_RESPONSE_PAGE_0_1 = Map.of(
            "pokemons", List.of(
                    Map.of("name", "bulbasaur", "url", "https://pokeapi.co/api/v2/pokemon/1/"),
                    Map.of("name", "ivysaur", "url", "https://pokeapi.co/api/v2/pokemon/2/"),
                    Map.of("name", "venusaur", "url", "https://pokeapi.co/api/v2/pokemon/3/"),
                    Map.of("name", "charmander", "url", "https://pokeapi.co/api/v2/pokemon/4/")
            )
    );
}