package com.example.serienRanking.controller;

import com.example.serienRanking.model.Title;
import com.example.serienRanking.service.TitleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/titles")
public class TitleController {

    private final TitleService titleService;

    @Autowired
    public TitleController(TitleService titleService) {
        this.titleService = titleService;
    }

    @PostMapping
    public ResponseEntity<Title> saveTitle(@RequestBody Title title) {
        return ResponseEntity.ok(titleService.saveTitle(title));
    }

    @GetMapping
    public ResponseEntity<List<Title>> getAllTitles() {
        return ResponseEntity.ok(titleService.getAllTitles());
    }

    @GetMapping("/type/{type}")
    public ResponseEntity<List<Title>> getTitlesByType(@PathVariable String type) {
        return ResponseEntity.ok(titleService.getTitlesByType(type));
    }

    @PostMapping("/rate")
    public ResponseEntity<?> rateTitle(@RequestBody Title ratingRequest) {
        try {
            // Ausführliches Logging für Debug-Zwecke
            System.out.println("Erhalten Rating Request: " + ratingRequest);
            System.out.println("tmdbId: " + ratingRequest.getTmdbId() + ", Typ: " +
                    (ratingRequest.getTmdbId() != null ? ratingRequest.getTmdbId().getClass().getName() : "null"));

            if (ratingRequest.getTmdbId() == null) {
                System.out.println("FEHLER: tmdbId ist null");
                return ResponseEntity.badRequest().body("tmdbId darf nicht null sein");
            }

            Optional<Title> existing = titleService.findByTmdbId(ratingRequest.getTmdbId());
            Title title;

            if (existing.isEmpty()) {
                System.out.println("Kein existierender Titel gefunden. Erstelle neuen Titel.");
                // Titel existiert noch nicht – neu speichern
                title = titleService.saveTitle(ratingRequest);
            } else {
                System.out.println("Existierender Titel gefunden: " + existing.get());
                title = existing.get();

                // Felder aktualisieren
                title.setStoryRating(ratingRequest.getStoryRating());
                title.setCharacterRating(ratingRequest.getCharacterRating());
                title.setActingRating(ratingRequest.getActingRating());
                title.setVisualAudioRating(ratingRequest.getVisualAudioRating());
                title.setEntertainmentRating(ratingRequest.getEntertainmentRating());

                // Gesamtwertung aktualisieren
                title.setOverallRating(
                        titleService.recalculateOverallRating(title)
                );

                // speichern
                title = titleService.saveTitle(title);
            }

            return ResponseEntity.ok(title);
        } catch (Exception e) {
            // Ausführliche Fehlerprotokollierung
            System.err.println("Fehler beim Verarbeiten der Bewertung: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Fehler beim Verarbeiten der Bewertung: " + e.getMessage());
        }
    }


}
