package com.example.serienRanking.repository;

import com.example.serienRanking.model.Rating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RatingRepository extends JpaRepository<Rating, Long> {
}