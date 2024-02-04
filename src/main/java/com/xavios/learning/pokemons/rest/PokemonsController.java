package com.xavios.learning.pokemons.rest;

import com.xavios.learning.pokemons.domain.PokemonsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.Map;

@RestController
@RequestMapping("/api/pokemons")
public class PokemonsController {

    @GetMapping
    public Mono<ResponseEntity<Map<String, Object>>> getPokemons() {
        return pokemonsService.getPokemons()
                .map(ResponseEntity::ok);
    }

    public PokemonsController(PokemonsService pokemonsService) {
        this.pokemonsService = pokemonsService;
    }

    private final PokemonsService pokemonsService;
}
