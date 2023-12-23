package com.xavios.learning.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class PokemonsServiceTest {

    @Test
    void it_should_return_not_null() {
        assertThat(pokemonsService.getAllPokemons())
                .as("It should return not null")
                .isNotNull();
    }

    @Test
    void it_should_call_pokemons_repository() {

    }

    @BeforeEach
    void setup() {
        this.pokemonsService = new PokemonsService();
    }

    private PokemonsService pokemonsService;
}