package com.xavios.learning.rest;

import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

public interface PokemonsService {
    Mono<List<Map<String, Object>>> getAllPokemons();
}
