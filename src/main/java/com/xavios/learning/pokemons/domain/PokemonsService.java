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
        //TODO: iterar por skip y limit
        return getAllPokemons()
                .map(this::assemblerResponse);
    }

    private Mono<List<Map<String, Object>>> getAllPokemons() {
        return pokemonsWebClient.getAllPokemons(0)
                .doOnNext(x -> System.out.println("TEST " + x))
                .flatMap(pokemonsClientResponse -> {
                    var pokemons = new ArrayList<Map<String, Object>>();
                    pokemons.addAll((List<Map<String, Object>>) pokemonsClientResponse.get("results"));

                    var nextPage = (String) pokemonsClientResponse.get("next");
                    nextPage.split("?")[1].split("&");

                    var count = (Integer) pokemonsClientResponse.get("count");
                    for (int i = 20; i < count; i = i + 20) {
                        pokemons.add((Map<String, Object>) pokemonsWebClient.getAllPokemons(20));
                    }

                    return Mono.just(List.of(Map.of("pokemons", pokemons)));
                });
    }

    private Map<String, Object> assemblerResponse(List<Map<String, Object>> body) {
        System.out.println("TEST");
        return null;
    }

    public PokemonsService(PokemonsWebClient pokemonsWebClient) {
        this.pokemonsWebClient = pokemonsWebClient;
    }

    private final PokemonsWebClient pokemonsWebClient;
}
