package com.xavios.learning.pokemons.domain;

import com.xavios.learning.pokemons.http.PokemonsWebClient;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class PokemonsService {
    public Mono<Map<String, Object>> getPokemons() {
        return getAllPokemons()
                .map(this::assemblerResponse);
    }

    private Mono<List<Map<String, Object>>> getAllPokemons() {
        return pokemonsWebClient.getAllPokemons(0)
                .flatMap(pokemonsClientResponse -> {
                    var pokemons = new ArrayList<Map<String, Object>>();
                    pokemons.addAll((List<Map<String, Object>>) pokemonsClientResponse.get("results"));

                    var count = (Integer) pokemonsClientResponse.get("count");
                    for (int offset = 20; offset < 60; offset = offset + 20) {
                        //1 -> pokemons.add((Map<String, Object>) pokemonsWebClient.getAllPokemons(offset));
                        //2 -> pokemonsWebClient.getAllPokemons(offset).map(pokemons::add);
                        pokemonsWebClient.getAllPokemons(offset)
                                .flatMap(x -> {
                                    pokemons.add(x);
                                    return Mono.just(x);
                                });
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
