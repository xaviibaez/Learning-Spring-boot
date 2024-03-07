package com.xavios.learning.pokemons.domain;

import com.xavios.learning.pokemons.http.PokemonsWebClient;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class PokemonsService {
    public Mono<Map<String, Object>> getPokemons() {
        //TODO: deberia comprobar en el flujo principal si la respues del cliente es vacia?
        return getAllPokemons()
                .map(this::assemblerResponse);
    }

    private Mono<List<Map<String, Object>>> getAllPokemons() {
        return pokemonsWebClient.getPokemons(0).flatMap(result -> {
            List<Map<String, Object>> pokemons = new ArrayList<>();

            pokemons.addAll((List<Map<String, Object>>) (result.get("results")));
            int count = (int) result.get("count");
            int offset = 20;

            while (pokemons.size() < count) {
                var response = pokemonsWebClient.getPokemons(offset).block();
                pokemons.addAll((List<Map<String, Object>>) (response.get("results")));
                offset += 20;
            }

            return Mono.just(pokemons);
        });
    }

    private Map<String, Object> assemblerResponse(List<Map<String, Object>> body) {
        return Map.of("pokemons", body);
    }

    public PokemonsService(PokemonsWebClient pokemonsWebClient) {
        this.pokemonsWebClient = pokemonsWebClient;
    }

    private final PokemonsWebClient pokemonsWebClient;
}
