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

    // Konstruktor-Injektion des TMDB-Service
    @Autowired
    public TmdbController(TmdbService tmdbService) {
        this.tmdbService = tmdbService;
    }

    // Sucht nach Filmen oder Serien mit einem bestimmten Titel (query)
    @GetMapping("/search")
    public Mono<ResponseEntity<String>> searchTitles(@RequestParam String query) {
        return tmdbService.searchTitles(query)
                .map(ResponseEntity::ok);
    }

    // Gibt eine Liste kommender Filme zurück (laut TMDB)
    @GetMapping("/upcoming")
    public Mono<ResponseEntity<String>> getUpcomingMovies() {
        return tmdbService.getUpcomingMovies()
                .map(ResponseEntity::ok);
    }

    // Gibt beliebte TV-Shows zurück (laut TMDB)
    @GetMapping("/popular-tv")
    public Mono<ResponseEntity<String>> getPopularTvShows() {
        return tmdbService.getPopularTvShows()
                .map(ResponseEntity::ok);
    }

    // Gibt die Details eines Titels (Movie oder TV-Show) anhand Typ und ID zurück
    // Beispiel: /title/movie/123 oder /title/tv/456
    @GetMapping("/title/{type}/{id}")
    public Mono<String> getTitleById(@PathVariable String type, @PathVariable String id) {
        return tmdbService.getTitleById(type, id);
    }
}
