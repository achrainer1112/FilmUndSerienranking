package com.example.serienRanking.controller;

import com.example.serienRanking.model.Movie;
import com.example.serienRanking.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/movies")
public class MovieController {

    private final MovieService movieService;

    @Autowired
    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    @PostMapping
    public ResponseEntity<Movie> saveMovie(@RequestBody Movie movie) {
        try {
            Movie savedMovie = movieService.saveMovie(movie);
            return ResponseEntity.ok(savedMovie);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping
    public ResponseEntity<List<Movie>> getAllMovies() {
        return ResponseEntity.ok(movieService.getAllMovies());
    }

    @GetMapping("/by-tmdb-rating")
    public ResponseEntity<List<Movie>> getAllMoviesByTmdbRating() {
        return ResponseEntity.ok(movieService.getAllMoviesByTmdbRating());
    }

    @GetMapping("/by-release-date")
    public ResponseEntity<List<Movie>> getAllMoviesByReleaseDate() {
        return ResponseEntity.ok(movieService.getAllMoviesByReleaseDate());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Movie> getMovieById(@PathVariable Long id) {
        Optional<Movie> movie = movieService.getMovieById(id);
        return movie.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/tmdb/{tmdbId}")
    public ResponseEntity<Movie> getMovieByTmdbId(@PathVariable Long tmdbId) {
        Optional<Movie> movie = movieService.findByTmdbId(tmdbId);
        return movie.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/search")
    public ResponseEntity<List<Movie>> searchMovies(@RequestParam String title) {
        return ResponseEntity.ok(movieService.searchMoviesByTitle(title));
    }

    @GetMapping("/genre/{genre}")
    public ResponseEntity<List<Movie>> getMoviesByGenre(@PathVariable String genre) {
        return ResponseEntity.ok(movieService.getMoviesByGenre(genre));
    }

    @PostMapping("/rate")
    public ResponseEntity<?> rateMovie(@RequestBody Movie ratingRequest) {
        try {
            System.out.println("Erhalten Movie Rating Request: " + ratingRequest);

            if (ratingRequest.getTmdbId() == null) {
                return ResponseEntity.badRequest().body("tmdbId darf nicht null sein");
            }

            Optional<Movie> existing = movieService.findByTmdbId(ratingRequest.getTmdbId());
            Movie movie;

            if (existing.isEmpty()) {
                System.out.println("Kein existierender Film gefunden. Erstelle neuen Film.");
                movie = movieService.saveMovie(ratingRequest);
            } else {
                System.out.println("Existierender Film gefunden: " + existing.get());
                movie = existing.get();

                // Bewertungen aktualisieren
                movie.setStoryRating(ratingRequest.getStoryRating());
                movie.setCharacterRating(ratingRequest.getCharacterRating());
                movie.setActingRating(ratingRequest.getActingRating());
                movie.setVisualAudioRating(ratingRequest.getVisualAudioRating());
                movie.setEntertainmentRating(ratingRequest.getEntertainmentRating());

                // Gesamtwertung aktualisieren
                movie.setOverallRating(movieService.recalculateOverallRating(movie));

                movie = movieService.saveMovie(movie);
            }

            return ResponseEntity.ok(movie);
        } catch (Exception e) {
            System.err.println("Fehler beim Verarbeiten der Film-Bewertung: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Fehler beim Verarbeiten der Bewertung: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMovie(@PathVariable Long id) {
        try {
            movieService.deleteMovie(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}