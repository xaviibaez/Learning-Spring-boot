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

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PokemonsControllerTest {

    @Test
    void it_should_return_not_null() {
        when(pokemonsService.getAllPokemons()).thenReturn(Mono.just(List.of()));

        assertThat(pokemonsController.getPokemons())
                .as("It should not null")
                .isNotNull();
    }

    @Test
    void it_should_call_pokemon_service() {
        when(pokemonsService.getAllPokemons()).thenReturn(Mono.just(List.of()));

        StepVerifier.create(pokemonsController.getPokemons())
                .as("it should call pokemon service")
                .assertNext(x -> verify(pokemonsService, only()
                        .description("it should call pokemon service"))
                        .getAllPokemons())
                .verifyComplete();
    }

    @Test
    void it_should_return_expected_pokemons_list() {
        when(pokemonsService.getAllPokemons()).thenReturn(Mono.just(pokemonsList));

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
    private List<Map<String, Object>> pokemonsList;
}