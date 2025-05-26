package com.example.serienRanking.controller;

import com.example.serienRanking.model.Series;
import com.example.serienRanking.service.SeriesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/series")
public class SeriesController {

    private final SeriesService seriesService;

    @Autowired
    public SeriesController(SeriesService seriesService) {
        this.seriesService = seriesService;
    }

    @PostMapping
    public ResponseEntity<Series> saveSeries(@RequestBody Series series) {
        try {
            Series savedSeries = seriesService.saveSeries(series);
            return ResponseEntity.ok(savedSeries);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping
    public ResponseEntity<List<Series>> getAllSeries() {
        return ResponseEntity.ok(seriesService.getAllSeries());
    }

    @GetMapping("/by-tmdb-rating")
    public ResponseEntity<List<Series>> getAllSeriesByTmdbRating() {
        return ResponseEntity.ok(seriesService.getAllSeriesByTmdbRating());
    }

    @GetMapping("/by-air-date")
    public ResponseEntity<List<Series>> getAllSeriesByAirDate() {
        return ResponseEntity.ok(seriesService.getAllSeriesByAirDate());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Series> getSeriesById(@PathVariable Long id) {
        Optional<Series> series = seriesService.getSeriesById(id);
        return series.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/tmdb/{tmdbId}")
    public ResponseEntity<Series> getSeriesByTmdbId(@PathVariable Long tmdbId) {
        Optional<Series> series = seriesService.findByTmdbId(tmdbId);
        return series.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/search")
    public ResponseEntity<List<Series>> searchSeries(@RequestParam String name) {
        return ResponseEntity.ok(seriesService.searchSeriesByName(name));
    }

    @GetMapping("/genre/{genre}")
    public ResponseEntity<List<Series>> getSeriesByGenre(@PathVariable String genre) {
        return ResponseEntity.ok(seriesService.getSeriesByGenre(genre));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<Series>> getSeriesByStatus(@PathVariable String status) {
        return ResponseEntity.ok(seriesService.getSeriesByStatus(status));
    }

    @PostMapping("/rate")
    public ResponseEntity<?> rateSeries(@RequestBody Series ratingRequest) {
        try {
            System.out.println("Erhalten Series Rating Request: " + ratingRequest);

            if (ratingRequest.getTmdbId() == null) {
                return ResponseEntity.badRequest().body("tmdbId darf nicht null sein");
            }

            Optional<Series> existing = seriesService.findByTmdbId(ratingRequest.getTmdbId());
            Series series;

            if (existing.isEmpty()) {
                System.out.println("Keine existierende Serie gefunden. Erstelle neue Serie.");
                series = seriesService.saveSeries(ratingRequest);
            } else {
                System.out.println("Existierende Serie gefunden: " + existing.get());
                series = existing.get();

                // Bewertungen aktualisieren
                series.setStoryRating(ratingRequest.getStoryRating());
                series.setCharacterRating(ratingRequest.getCharacterRating());
                series.setActingRating(ratingRequest.getActingRating());
                series.setVisualAudioRating(ratingRequest.getVisualAudioRating());
                series.setEntertainmentRating(ratingRequest.getEntertainmentRating());

                // Gesamtwertung aktualisieren
                series.setOverallRating(seriesService.recalculateOverallRating(series));

                series = seriesService.saveSeries(series);
            }

            return ResponseEntity.ok(series);
        } catch (Exception e) {
            System.err.println("Fehler beim Verarbeiten der Serien-Bewertung: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Fehler beim Verarbeiten der Bewertung: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSeries(@PathVariable Long id) {
        try {
            seriesService.deleteSeries(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}