package com.xavios.learning.pokemons.domain;

import com.xavios.learning.pokemons.http.PokemonsWebClient;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

@Service
public class PokemonsService {
    public Mono<List<Map<String, Object>>> getAllPokemons() {
        return pokemonsWebClient.getAllPokemons().map(PokemonsService::assemblerResponse);
    }

    private static List<Map<String, Object>> assemblerResponse(List<Map<String, Object>> x) {
        return List.of(Map.of("pokemons", x));
    }

    public PokemonsService(PokemonsWebClient pokemonsWebClient) {
        this.pokemonsWebClient = pokemonsWebClient;
    }

    private final PokemonsWebClient pokemonsWebClient;
}
