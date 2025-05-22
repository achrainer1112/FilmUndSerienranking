package com.example.serienRanking.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString  // Hinzugefügt für besseres Logging
public class Title {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)  // Sicherstellen, dass tmdbId eindeutig ist
    private Long tmdbId;

    private String name;
    private String type; // "movie" oder "series"

    @Column(length = 1000)  // Verlängern für längere Pfade
    private String posterPath;

    @Column(length = 5000)  // Verlängern für längere Beschreibungen
    private String overview;

    private int storyRating;
    private int characterRating;
    private int actingRating;
    private int visualAudioRating;
    private int entertainmentRating;
    private int overallRating;
}