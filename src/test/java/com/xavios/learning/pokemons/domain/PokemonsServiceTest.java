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
        when(pokemonsWebClient.getAllPokemons(0)).thenReturn(Mono.just(Map.of()));

        assertThat(pokemonsService.getPokemons())
                .as("It should return not null")
                .isNotNull();
    }

    @Test
    void it_should_call_pokemons_web_client() {
        when(pokemonsWebClient.getAllPokemons(0)).thenReturn(Mono.just(Map.of()));

        StepVerifier.create(pokemonsService.getPokemons())
                .as("It should call pokemons web client")
                .assertNext(x -> verify(pokemonsWebClient, only()
                        .description("It should call pokemons web client"))
                        .getAllPokemons(0))
                .verifyComplete();
    }

    @Test
    void it_should_return_expected_pokemons_list() {
        when(pokemonsWebClient.getAllPokemons(0)).thenReturn(Mono.just(pokemonsClientResponse));

        StepVerifier.create(pokemonsService.getPokemons())
                .as("it should return expected pokemons list")
                .expectNext(Map.of("pokemons", List.of(pokemonsClientResponse)))
                .verifyComplete();
    }

    private Map<String, Object> buildPokemonsPaginationResponse(Integer count,
                                                                String nextUrl,
                                                                String previousUrl,
                                                                List<Map<String, Object>> pokemonsResults) {
        return Map.of(
                "count", count,
                "next", nextUrl,
                "previous", previousUrl,
                "results", pokemonsResults);
    }
    @BeforeEach
    void setup() {
        this.pokemonsService = new PokemonsService(pokemonsWebClient);
    }

    private PokemonsService pokemonsService;

    @Mock
    private PokemonsWebClient pokemonsWebClient;
    @Mock
    private Map<String, Object> pokemonsClientResponse;

    private static final List<Map<String, Object>> POKEMONS_PAGE_1 = List.of(
            Map.of("name", "bulbasaur", "url", "https://pokeapi.co/api/v2/pokemon/1/")
    );
    private static final List<Map<String, Object>> POKEMONS_PAGE_2 = List.of(
            Map.of("name", "bulbasaur2", "url", "https://pokeapi.co/api/v2/pokemon/2/")
    );
}