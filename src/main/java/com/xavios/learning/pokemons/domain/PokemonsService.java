package com.xavios.learning.pokemons.domain;

import com.xavios.learning.pokemons.http.PokemonsWebClient;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

@Service
public class PokemonsService {
    public Mono<Map<String, Object>> getAllPokemons() {
        //TODO: iterar por skip y limit
        return pokemonsWebClient.getAllPokemons().map(this::assemblerResponse);
    }

    private Map<String, Object> assemblerResponse(Map<String, Object> body) {
        for (var pokemon : (List<Map<String, Object>>) body.get("results")) {
            var url = (String) pokemon.get("url");
            var id = url.substring(34, url.length() - 1);
            pokemon.put("id", id);
        }
        return Map.of("pokemons", body);
    }

    public PokemonsService(PokemonsWebClient pokemonsWebClient) {
        this.pokemonsWebClient = pokemonsWebClient;
    }

    private final PokemonsWebClient pokemonsWebClient;
}
