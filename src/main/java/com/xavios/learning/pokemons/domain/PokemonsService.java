package com.xavios.learning.pokemons.domain;

import com.xavios.learning.pokemons.http.PokemonsWebClient;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

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
        return pokemonsWebClient.getPokemons(0).flatMap(x -> {
            pokemonsWebClient.getPokemons(20);

            List<Map<String, Object>> results = (List<Map<String, Object>>)(x.get("results"));

            return Mono.just(results != null ? results : List.of(x));
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
