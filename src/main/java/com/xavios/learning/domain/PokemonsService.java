package com.xavios.learning.domain;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

@Service
public class PokemonsService{
    public Mono<List<Map<String, Object>>> getAllPokemons(){
        return null;
    };
}
