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
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PokemonsServiceTest {

    @Test
    void it_should_return_not_null() {
        when(pokemonsWebClient.getAllPokemons()).thenReturn(Mono.just(List.of()));

        assertThat(pokemonsService.getAllPokemons())
                .as("It should return not null")
                .isNotNull();
    }

    @Test
    void it_should_call_pokemons_web_client() {
        when(pokemonsWebClient.getAllPokemons()).thenReturn(Mono.just(List.of(Map.of())));

        StepVerifier.create(pokemonsService.getAllPokemons())
                .as("It should call pokemons web client")
                .assertNext(x -> verify(pokemonsWebClient, only()
                        .description("It should call pokemons web client"))
                        .getAllPokemons())
                .verifyComplete();
    }

    @Test
    void it_should_return_expected_pokemons_list() {
        when(pokemonsWebClient.getAllPokemons()).thenReturn(Mono.just(pokemonsList));

        StepVerifier.create(pokemonsService.getAllPokemons())
                .as("it should return expected pokemons list")
                .expectNext(List.of(Map.of("pokemons", pokemonsList)))
                .verifyComplete();
    }

    @BeforeEach
    void setup() {
        this.pokemonsService = new PokemonsService(pokemonsWebClient);
    }

    private PokemonsService pokemonsService;

    @Mock
    private PokemonsWebClient pokemonsWebClient;
    @Mock
    private List<Map<String, Object>> pokemonsList;
}