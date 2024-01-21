package com.xavios.learning.pokemons.rest;

import com.xavios.learning.pokemons.domain.PokemonsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PokemonsControllerTest {

    @Test
    void it_should_return_not_null() {
        when(pokemonsService.getPokemons()).thenReturn(Mono.just(Map.of()));

        assertThat(pokemonsController.getPokemons())
                .as("It should not null")
                .isNotNull();
    }

    @Test
    void it_should_call_pokemon_service() {
        when(pokemonsService.getPokemons()).thenReturn(Mono.just(Map.of()));

        StepVerifier.create(pokemonsController.getPokemons())
                .as("it should call pokemon service")
                .assertNext(x -> verify(pokemonsService, only()
                        .description("it should call pokemon service"))
                        .getPokemons())
                .verifyComplete();
    }

    @Test
    void it_should_return_expected_pokemons_list() {
        when(pokemonsService.getPokemons()).thenReturn(Mono.just(pokemonsList));

        StepVerifier.create(pokemonsController.getPokemons())
                .as("it should return expected pokemons list")
                .expectNext(ResponseEntity.ok(Map.of("pokemons", pokemonsList)))
                .verifyComplete();
    }

    @BeforeEach
    void setup() {
        this.pokemonsController = new PokemonsController(pokemonsService);
    }

    private PokemonsController pokemonsController;
    @Mock
    private PokemonsService pokemonsService;
    @Mock
    private Map<String, Object> pokemonsList;
}