package com.example.serienRanking.service;

import com.example.serienRanking.model.Movie;
import com.example.serienRanking.repository.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MovieService {

    private final MovieRepository movieRepository;

    // Konstruktor-Injektion des MovieRepository
    @Autowired
    public MovieService(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    // Speichert einen Film in der Datenbank
    public Movie saveMovie(Movie movie) {
        try {
            if (movie.getTmdbId() == null) {
                throw new IllegalArgumentException("tmdbId kann nicht null sein");
            }

            // Gesamtbewertung berechnen, falls Einzelbewertungen vorhanden sind
            if (hasRatings(movie)) {
                int overallRating = calculateOverallRating(movie);
                movie.setOverallRating(overallRating);
            }

            System.out.println("Speichere Film: " + movie);
            return movieRepository.save(movie);
        } catch (Exception e) {
            System.err.println("Fehler beim Speichern des Films: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    // Gibt alle Filme nach Gesamtbewertung absteigend sortiert zurück
    public List<Movie> getAllMovies() {
        return movieRepository.findAllByOrderByOverallRatingDesc();
    }

    // Gibt alle Filme nach TMDB-Rating absteigend sortiert zurück
    public List<Movie> getAllMoviesByTmdbRating() {
        return movieRepository.findAllByOrderByTmdbRatingDesc();
    }

    // Gibt alle Filme nach Erscheinungsdatum absteigend sortiert zurück
    public List<Movie> getAllMoviesByReleaseDate() {
        return movieRepository.findAllByOrderByReleaseDateDesc();
    }

    // Gibt optional einen Film anhand seiner Datenbank-ID zurück
    public Optional<Movie> getMovieById(Long id) {
        return movieRepository.findById(id);
    }

    // Sucht nach einem Film anhand der TMDB-ID
    public Optional<Movie> findByTmdbId(Long tmdbId) {
        try {
            System.out.println("Suche nach Film mit tmdbId = '" + tmdbId + "'");
            return movieRepository.findByTmdbId(tmdbId);
        } catch (Exception e) {
            System.err.println("Fehler beim Suchen nach tmdbId: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    // Sucht nach Filmen, die den angegebenen Titel enthalten (case-insensitive)
    public List<Movie> searchMoviesByTitle(String title) {
        return movieRepository.findByTitleContainingIgnoreCase(title);
    }

    // Sucht nach Filmen mit einem bestimmten Genre (Teilstring, case-insensitive)
    public List<Movie> getMoviesByGenre(String genre) {
        return movieRepository.findByGenreContainingIgnoreCase(genre);
    }

    // Löscht einen Film anhand seiner ID
    public void deleteMovie(Long id) {
        movieRepository.deleteById(id);
    }

    // Prüft, ob ein Film mindestens eine Bewertung hat
    private boolean hasRatings(Movie movie) {
        return movie.getStoryRating() != null ||
                movie.getCharacterRating() != null ||
                movie.getActingRating() != null ||
                movie.getVisualAudioRating() != null ||
                movie.getEntertainmentRating() != null;
    }

    // Berechnet die Gesamtbewertung durch Addition aller Einzelbewertungen
    private int calculateOverallRating(Movie movie) {
        int total = 0;
        if (movie.getStoryRating() != null) total += movie.getStoryRating();
        if (movie.getCharacterRating() != null) total += movie.getCharacterRating();
        if (movie.getActingRating() != null) total += movie.getActingRating();
        if (movie.getVisualAudioRating() != null) total += movie.getVisualAudioRating();
        if (movie.getEntertainmentRating() != null) total += movie.getEntertainmentRating();
        return total;
    }

    // Rechnet die Gesamtbewertung neu (falls du es separat aufrufen willst)
    public int recalculateOverallRating(Movie movie) {
        return calculateOverallRating(movie);
    }
}
