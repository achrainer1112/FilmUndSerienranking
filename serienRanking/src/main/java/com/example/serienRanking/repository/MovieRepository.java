package com.example.serienRanking.repository;

import com.example.serienRanking.model.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {

    @Query("SELECT m FROM Movie m LEFT JOIN m.rating r ORDER BY r.overallRating DESC NULLS LAST")
    List<Movie> findAllByOrderByOverallRatingDesc();

    List<Movie> findAllByOrderByTmdbRatingDesc();

    List<Movie> findAllByOrderByReleaseDateDesc();

    Optional<Movie> findByTmdbId(Long tmdbId);

    List<Movie> findByTitleContainingIgnoreCase(String title);

    List<Movie> findByGenreContainingIgnoreCase(String genre);
}