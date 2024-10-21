package com.avenhon.kmdb.Entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.util.HashSet;
import java.util.Set;

@Entity
public class Movie {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    // Bean validation for title field
    @NotNull(message = "Name cannot be null")
    private String title;
    // Bean validation for releaseYear field
    @NotNull(message = "Release year cannot be null")
    private Integer releaseYear;
    // Bean validation for duration field
    @NotNull(message = "Duration cannot be null")
    private Integer duration;

    // Many-to-many relationship with Genre
    @ManyToMany(mappedBy = "genreMovies")
    private Set<Genre> movieGenres = new HashSet<>();
    // Many-to-many relationship with Actor
    @ManyToMany(mappedBy = "actorMovies")
    private Set<Actor> movieActors = new HashSet<>();

    // Constructor for request
    public Movie() {
    }

    // Constructor with id
    public Movie(Long id, String title, Integer releaseYear, Integer duration, Set<Genre> genres, Set<Actor> actors) {
        this.id = id;
        this.title = title;
        this.releaseYear = releaseYear;
        this.duration = duration;
        this.movieGenres = genres;
        this.movieActors = actors;
    }

    // Constructor without id
    public Movie(String title, Integer releaseYear, Integer duration, Set<Genre> genres, Set<Actor> actors) {
        this.title = title;
        this.releaseYear = releaseYear;
        this.duration = duration;
        this.movieGenres = genres;
        this.movieActors = actors;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getReleaseYear() {
        return releaseYear;
    }

    public void setReleaseYear(Integer releaseYear) {
        this.releaseYear = releaseYear;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public Set<Genre> getMovieGenres() {
        return movieGenres;
    }

    public void setMovieGenres(Set<Genre> movieGenres) {
        this.movieGenres = movieGenres;
    }

    public Set<Actor> getMovieActors() {
        return movieActors;
    }

    public void setMovieActors(Set<Actor> movieActors) {
        this.movieActors = movieActors;
    }
}
