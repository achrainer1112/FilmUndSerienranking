package com.example.serienRanking.repository;

import com.example.serienRanking.model.Series;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SeriesRepository extends JpaRepository<Series, Long> {

    List<Series> findAllByOrderByOverallRatingDesc();

    List<Series> findAllByOrderByTmdbRatingDesc();

    List<Series> findAllByOrderByFirstAirDateDesc();

    Optional<Series> findByTmdbId(Long tmdbId);

    List<Series> findByNameContainingIgnoreCase(String name);

    List<Series> findByGenreContainingIgnoreCase(String genre);

    List<Series> findByStatus(String status);
}