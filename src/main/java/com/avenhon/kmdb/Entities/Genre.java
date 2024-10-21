package com.avenhon.kmdb.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table
public class Genre {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    protected Long id;

    // Bean validation for name field
    @NotNull(message = "Name cannot be null")
    @Pattern(regexp = "^[A-Za-z\\s]*$", message = "Name should contains only letters")
    private String name;

    // Many-to-many relationship with Genre
    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "genre_movie",
            joinColumns = @JoinColumn(name = "genre_id"),
            inverseJoinColumns = @JoinColumn(name = "movie_id"))
    // Ignoring json for evading response recursion
    @JsonIgnore
    private Set<Movie> genreMovies = new HashSet<>();

    // Constructor for requests
    public Genre() {

    }

    // Constructor with id
    public Genre(Long id, String name, Set<Movie> movies) {
        this.id = id;
        this.name = name;
        this.genreMovies = movies;
    }

    // Constructor without id
    public Genre(String name, Set<Movie> movies) {
        this.name = name;
        this.genreMovies = movies;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Movie> getGenreMovies() {
        return genreMovies;
    }

    public void setGenreMovies(Set<Movie> genreMovies) {
        this.genreMovies = genreMovies;
    }
}