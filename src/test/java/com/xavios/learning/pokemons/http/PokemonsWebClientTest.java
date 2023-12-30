package com.xavios.learning.pokemons.http;

import com.xavios.learning.pokemons.domain.PokemonsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class PokemonsWebClientTest {

    @Test
    void it_should_return_not_null() {
        assertThat(pokemonsWebClient.getAllPokemons())
                .as("It should return not null")
                .isNotNull();
    }

    @BeforeEach
    void setup() {
        this.pokemonsWebClient = new PokemonsWebClient();
    }

    private PokemonsWebClient pokemonsWebClient;
}