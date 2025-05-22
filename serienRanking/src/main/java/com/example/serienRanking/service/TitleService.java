package com.example.serienRanking.service;

import com.example.serienRanking.model.Title;
import com.example.serienRanking.repository.TitleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TitleService {

    private final TitleRepository titleRepository;

    @Autowired
    public TitleService(TitleRepository titleRepository) {
        this.titleRepository = titleRepository;
    }

    public Title saveTitle(Title title) {
        try {
            // Überprüfe ob tmdbId vorhanden ist
            if (title.getTmdbId() == null) {
                throw new IllegalArgumentException("tmdbId kann nicht null sein");
            }

            // Berechne Gesamtbewertung
            int overallRating = calculateOverallRating(title);
            title.setOverallRating(overallRating);

            // Debug-Info
            System.out.println("Speichere Titel: " + title);

            return titleRepository.save(title);
        } catch (Exception e) {
            System.err.println("Fehler beim Speichern des Titels: " + e.getMessage());
            e.printStackTrace();
            throw e; // Werfe die Exception weiter
        }
    }

    public List<Title> getAllTitles() {
        return titleRepository.findAllByOrderByOverallRatingDesc();
    }

    public List<Title> getTitlesByType(String type) {
        return titleRepository.findByTypeOrderByOverallRatingDesc(type);
    }

    private int calculateOverallRating(Title title) {
        return (title.getStoryRating() + title.getCharacterRating() +
                title.getActingRating() + title.getVisualAudioRating() +
                title.getEntertainmentRating());
    }

    public Optional<Title> findByTmdbId(Long tmdbId) {
        try {
            System.out.println("Suche nach Serie mit tmdbId = '" + tmdbId + "' vom Typ: " +
                    (tmdbId != null ? tmdbId.getClass().getName() : "null"));
            return titleRepository.findByTmdbId(tmdbId);
        } catch (Exception e) {
            System.err.println("Fehler beim Suchen nach tmdbId: " + e.getMessage());
            e.printStackTrace();
            throw e; // Werfe die Exception weiter
        }
    }

    public int recalculateOverallRating(Title title) {
        return title.getStoryRating() +
                title.getCharacterRating() +
                title.getActingRating() +
                title.getVisualAudioRating() +
                title.getEntertainmentRating();
    }
}