package com.example.serienRanking.controller;

import com.example.serienRanking.service.TmdbService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/tmdb")
public class TmdbController {

    private final TmdbService tmdbService;

    @Autowired
    public TmdbController(TmdbService tmdbService) {
        this.tmdbService = tmdbService;
    }

    @GetMapping("/search")
    public Mono<ResponseEntity<String>> searchTitles(@RequestParam String query) {
        return tmdbService.searchTitles(query)
                .map(ResponseEntity::ok);
    }

    @GetMapping("/upcoming")
    public Mono<ResponseEntity<String>> getUpcomingMovies() {
        return tmdbService.getUpcomingMovies()
                .map(ResponseEntity::ok);
    }

    @GetMapping("/popular-tv")
    public Mono<ResponseEntity<String>> getPopularTvShows() {
        return tmdbService.getPopularTvShows()
                .map(ResponseEntity::ok);
    }

    @GetMapping("/title/{type}/{id}")
    public Mono<String> getTitleById(@PathVariable String type, @PathVariable String id) {
        return tmdbService.getTitleById(type, id);
    }
}