package com.example.serienRanking.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;

@Entity
@Table(name = "series")
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Series {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private Long tmdbId;

    @Column(nullable = false)
    private String name;

    @Column(length = 1000)
    private String posterPath;

    @Column(length = 5000)
    private String overview;

    private LocalDate firstAirDate;
    private LocalDate lastAirDate;

    private Integer numberOfSeasons;
    private Integer numberOfEpisodes;

    private String status; // Returning Series, Ended, etc.
    private String genre;

    private Double tmdbRating; // TMDB Bewertung

    // 1:1 Beziehung zu Rating
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "rating_id")
    private Rating rating;

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

    // Getter und Setter für Rating-Eigenschaften (für Backend-Kompatibilität)
    public Integer getStoryRating() {
        return rating != null ? rating.getStoryRating() : null;
    }

    public void setStoryRating(Integer storyRating) {
        if (rating == null) {
            rating = new Rating();
        }
        rating.setStoryRating(storyRating);
    }

    public Integer getCharacterRating() {
        return rating != null ? rating.getCharacterRating() : null;
    }

    public void setCharacterRating(Integer characterRating) {
        if (rating == null) {
            rating = new Rating();
        }
        rating.setCharacterRating(characterRating);
    }

    public Integer getActingRating() {
        return rating != null ? rating.getActingRating() : null;
    }

    public void setActingRating(Integer actingRating) {
        if (rating == null) {
            rating = new Rating();
        }
        rating.setActingRating(actingRating);
    }

    public Integer getVisualAudioRating() {
        return rating != null ? rating.getVisualAudioRating() : null;
    }

    public void setVisualAudioRating(Integer visualAudioRating) {
        if (rating == null) {
            rating = new Rating();
        }
        rating.setVisualAudioRating(visualAudioRating);
    }

    public Integer getEntertainmentRating() {
        return rating != null ? rating.getEntertainmentRating() : null;
    }

    public void setEntertainmentRating(Integer entertainmentRating) {
        if (rating == null) {
            rating = new Rating();
        }
        rating.setEntertainmentRating(entertainmentRating);
    }

    public Integer getOverallRating() {
        return rating != null ? rating.getOverallRating() : null;
    }

    public void setOverallRating(Integer overallRating) {
        if (rating == null) {
            rating = new Rating();
        }
        rating.setOverallRating(overallRating);
    }
}