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
                throw new IllegalArgumentException("tmdbId kann nicht null sein");
            }

            // Berechne Gesamtbewertung falls Bewertungen vorhanden sind
            if (hasRatings(series)) {
                int overallRating = calculateOverallRating(series);
                series.setOverallRating(overallRating);
            }

            System.out.println("Speichere Serie: " + series);
            return seriesRepository.save(series);
        } catch (Exception e) {
            System.err.println("Fehler beim Speichern der Serie: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    public List<Series> getAllSeries() {
        return seriesRepository.findAllByOrderByOverallRatingDesc();
    }

    public List<Series> getAllSeriesByTmdbRating() {
        return seriesRepository.findAllByOrderByTmdbRatingDesc();
    }

    public List<Series> getAllSeriesByAirDate() {
        return seriesRepository.findAllByOrderByFirstAirDateDesc();
    }

    public Optional<Series> getSeriesById(Long id) {
        return seriesRepository.findById(id);
    }

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

    public List<Series> searchSeriesByName(String name) {
        return seriesRepository.findByNameContainingIgnoreCase(name);
    }

    public List<Series> getSeriesByGenre(String genre) {
        return seriesRepository.findByGenreContainingIgnoreCase(genre);
    }

    public List<Series> getSeriesByStatus(String status) {
        return seriesRepository.findByStatus(status);
    }

    public void deleteSeries(Long id) {
        seriesRepository.deleteById(id);
    }

    private boolean hasRatings(Series series) {
        return series.getStoryRating() != null ||
                series.getCharacterRating() != null ||
                series.getActingRating() != null ||
                series.getVisualAudioRating() != null ||
                series.getEntertainmentRating() != null;
    }

    private int calculateOverallRating(Series series) {
        int total = 0;
        if (series.getStoryRating() != null) total += series.getStoryRating();
        if (series.getCharacterRating() != null) total += series.getCharacterRating();
        if (series.getActingRating() != null) total += series.getActingRating();
        if (series.getVisualAudioRating() != null) total += series.getVisualAudioRating();
        if (series.getEntertainmentRating() != null) total += series.getEntertainmentRating();
        return total;
    }

    public int recalculateOverallRating(Series series) {
        return calculateOverallRating(series);
    }
}