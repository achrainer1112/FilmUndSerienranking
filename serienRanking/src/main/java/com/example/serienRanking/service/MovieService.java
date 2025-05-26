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

    @Autowired
    public MovieService(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    public Movie saveMovie(Movie movie) {
        try {
            if (movie.getTmdbId() == null) {
                throw new IllegalArgumentException("tmdbId kann nicht null sein");
            }

            // Berechne Gesamtbewertung falls Bewertungen vorhanden sind
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

    public List<Movie> getAllMovies() {
        return movieRepository.findAllByOrderByOverallRatingDesc();
    }

    public List<Movie> getAllMoviesByTmdbRating() {
        return movieRepository.findAllByOrderByTmdbRatingDesc();
    }

    public List<Movie> getAllMoviesByReleaseDate() {
        return movieRepository.findAllByOrderByReleaseDateDesc();
    }

    public Optional<Movie> getMovieById(Long id) {
        return movieRepository.findById(id);
    }

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

    public List<Movie> searchMoviesByTitle(String title) {
        return movieRepository.findByTitleContainingIgnoreCase(title);
    }

    public List<Movie> getMoviesByGenre(String genre) {
        return movieRepository.findByGenreContainingIgnoreCase(genre);
    }

    public void deleteMovie(Long id) {
        movieRepository.deleteById(id);
    }

    private boolean hasRatings(Movie movie) {
        return movie.getStoryRating() != null ||
                movie.getCharacterRating() != null ||
                movie.getActingRating() != null ||
                movie.getVisualAudioRating() != null ||
                movie.getEntertainmentRating() != null;
    }

    private int calculateOverallRating(Movie movie) {
        int total = 0;
        if (movie.getStoryRating() != null) total += movie.getStoryRating();
        if (movie.getCharacterRating() != null) total += movie.getCharacterRating();
        if (movie.getActingRating() != null) total += movie.getActingRating();
        if (movie.getVisualAudioRating() != null) total += movie.getVisualAudioRating();
        if (movie.getEntertainmentRating() != null) total += movie.getEntertainmentRating();
        return total;
    }

    public int recalculateOverallRating(Movie movie) {
        return calculateOverallRating(movie);
    }
}