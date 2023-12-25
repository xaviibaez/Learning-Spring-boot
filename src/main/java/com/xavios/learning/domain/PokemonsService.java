package com.xavios.learning.domain;

import com.xavios.learning.http.PokemonsWebClient;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

@Service
public class PokemonsService{
    public Mono<List<Map<String, Object>>> getAllPokemons(){
        return pokemonsWebClient.getAllPokemons().map(x -> List.of(Map.of()));
    };

    public PokemonsService(PokemonsWebClient pokemonsWebClient) {
        this.pokemonsWebClient = pokemonsWebClient;
    }
    private final PokemonsWebClient pokemonsWebClient;
}
