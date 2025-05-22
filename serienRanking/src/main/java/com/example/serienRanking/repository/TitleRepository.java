package com.example.serienRanking.repository;

import com.example.serienRanking.model.Title;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TitleRepository extends JpaRepository<Title, Long> {
    List<Title> findByTypeOrderByOverallRatingDesc(String type);

    List<Title> findAllByOrderByOverallRatingDesc();

    Optional<Title> findByTmdbId(Long tmdbId);
}
