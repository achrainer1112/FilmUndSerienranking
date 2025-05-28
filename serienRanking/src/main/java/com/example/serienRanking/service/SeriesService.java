package com.example.serienRanking.service;

import com.example.serienRanking.model.Series;
import com.example.serienRanking.repository.SeriesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SeriesService {

    private final SeriesRepository seriesRepository;

    @Autowired
    public SeriesService(SeriesRepository seriesRepository) {
        this.seriesRepository = seriesRepository;
    }

    public Series saveSeries(Series series) {
        try {
            if (series.getTmdbId() == null) {
                // Eine Serie ohne TMDB-ID darf nicht gespeichert werden
                throw new IllegalArgumentException("tmdbId kann nicht null sein");
            }

            // Falls Einzelbewertungen vorhanden sind, berechne die Gesamtbewertung
            if (hasRatings(series)) {
                int overallRating = calculateOverallRating(series);
                series.setOverallRating(overallRating);
            }

            System.out.println("Speichere Serie: " + series);
            return seriesRepository.save(series);
        } catch (Exception e) {
            // Fehlerausgabe im Fehlerfall (z. B. bei DB-Problemen)
            System.err.println("Fehler beim Speichern der Serie: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    // Gibt alle Serien absteigend nach der Gesamtbewertung zurück
    public List<Series> getAllSeries() {
        return seriesRepository.findAllByOrderByOverallRatingDesc();
    }

    // Gibt alle Serien absteigend nach der TMDB-Bewertung zurück
    public List<Series> getAllSeriesByTmdbRating() {
        return seriesRepository.findAllByOrderByTmdbRatingDesc();
    }

    // Gibt alle Serien absteigend nach dem Ausstrahlungsdatum zurück
    public List<Series> getAllSeriesByAirDate() {
        return seriesRepository.findAllByOrderByFirstAirDateDesc();
    }

    // Einzelne Serie anhand ihrer internen ID finden
    public Optional<Series> getSeriesById(Long id) {
        return seriesRepository.findById(id);
    }

    // Suche nach einer Serie anhand der externen TMDB-ID
    public Optional<Series> findByTmdbId(Long tmdbId) {
        try {
            System.out.println("Suche nach Serie mit tmdbId = '" + tmdbId + "'");
            return seriesRepository.findByTmdbId(tmdbId);
        } catch (Exception e) {
            System.err.println("Fehler beim Suchen nach tmdbId: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    // Suche nach Serien, deren Name einen bestimmten Begriff enthält (Groß-/Kleinschreibung egal)
    public List<Series> searchSeriesByName(String name) {
        return seriesRepository.findByNameContainingIgnoreCase(name);
    }

    // Suche nach Serien, die ein bestimmtes Genre enthalten
    public List<Series> getSeriesByGenre(String genre) {
        return seriesRepository.findByGenreContainingIgnoreCase(genre);
    }

    // Gibt Serien nach Status zurück (z. B. "Laufend", "Abgeschlossen")
    public List<Series> getSeriesByStatus(String status) {
        return seriesRepository.findByStatus(status);
    }

    // Löscht eine Serie anhand ihrer ID
    public void deleteSeries(Long id) {
        seriesRepository.deleteById(id);
    }

    // Prüft, ob überhaupt Bewertungen vorhanden sind
    private boolean hasRatings(Series series) {
        return series.getStoryRating() != null ||
                series.getCharacterRating() != null ||
                series.getActingRating() != null ||
                series.getVisualAudioRating() != null ||
                series.getEntertainmentRating() != null;
    }

    // Berechnet die Gesamtbewertung als Summe aller Einzelbewertungen
    private int calculateOverallRating(Series series) {
        int total = 0;
        if (series.getStoryRating() != null) total += series.getStoryRating();
        if (series.getCharacterRating() != null) total += series.getCharacterRating();
        if (series.getActingRating() != null) total += series.getActingRating();
        if (series.getVisualAudioRating() != null) total += series.getVisualAudioRating();
        if (series.getEntertainmentRating() != null) total += series.getEntertainmentRating();
        return total;
    }

    // Öffentliche Methode zum Neuberechnen der Gesamtbewertung
    public int recalculateOverallRating(Series series) {
        return calculateOverallRating(series);
    }
}
