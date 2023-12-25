package com.xavios.learning.domain;

import com.xavios.learning.http.PokemonsWebClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;
import static reactor.core.publisher.Mono.when;

@ExtendWith(MockitoExtension.class)
class PokemonsServiceTest {

    @Test
    void it_should_return_not_null() {
        assertThat(pokemonsService.getAllPokemons())
                .as("It should return not null")
                .isNotNull();
    }

    @Test
    void it_should_call_pokemons_web_client() {
        //TODO: este no va -> java.lang.NullPointerException: publisher
        when(pokemonsWebClient.getAllPokemons()).thenReturn(Mono.just(List.of()));

        StepVerifier.create(pokemonsService.getAllPokemons())
                .as("It should call pokemons web client")
                .assertNext(x -> verify(pokemonsWebClient, only()
                        .description("It should call pokemons web client"))
                        .getAllPokemons())
                .verifyComplete();
    }

    @BeforeEach
    void setup() {
        this.pokemonsService = new PokemonsService(pokemonsWebClient);
    }

    private PokemonsService pokemonsService;

    @Mock
    private PokemonsWebClient pokemonsWebClient;
}