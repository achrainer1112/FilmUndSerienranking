package com.example.serienRanking.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "ratings")
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Rating {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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