package com.example.serienRanking.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;

@Entity
@Table(name = "movies")
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private Long tmdbId;

    @Column(nullable = false)
    private String title;

    @Column(length = 1000)
    private String posterPath;

    @Column(length = 5000)
    private String overview;

    private LocalDate releaseDate;

    private Integer runtime; // Laufzeit in Minuten

    private String genre;

    private Double tmdbRating; // TMDB Bewertung

    // Eigene Bewertungen
    private Integer storyRating;
    private Integer characterRating;
    private Integer actingRating;
    private Integer visualAudioRating;
    private Integer entertainmentRating;
    private Integer overallRating;

    @Column(updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private java.util.Date createdAt;

    @Temporal(TemporalType.TIMESTAMP)
    private java.util.Date updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = new java.util.Date();
        updatedAt = new java.util.Date();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = new java.util.Date();
    }
}