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
        return pokemonsWebClient.getPokemons(0).flatMap(result -> {
            List<Map<String, Object>> pokemons = (List<Map<String, Object>>) (result.get("results"));
            int count = (int) result.get("count");

            for (int offset = 20; offset <= count; offset = offset + 20) {
                pokemonsWebClient.getPokemons(offset).subscribe(response -> {
                    System.out.println("response: " + response.get("results"));
                    System.out.println("results: " + pokemons.size());
                    pokemons.addAll((List<Map<String, Object>>) response.get("results"));
                });
                //pokemons.addAll((List<Map<String, Object>>) response.get("results"));
            }

            return Mono.just(pokemons != null ? pokemons : List.of(result));
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
