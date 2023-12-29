package com.xavios.learning.http;

import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

public interface PokemonsWebClient {
    Mono<List<Map<String, Object>>> getAllPokemons();
}