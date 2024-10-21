package com.avenhon.kmdb.Entities;

import com.avenhon.kmdb.ExceptionHandler.BadRequestException;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Set;
@Entity
public class Actor {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    // Bean validation for name field
    @NotNull(message = "Name cannot be null")
    @Pattern(regexp = "^[A-Za-z\\s]*$", message = "Name should contains only letters")
    private String name;

    // Bean validation for birthDate field
    @NotNull(message = "Birth date cannot be null")
    private String birthDate; // should be in ISO 8601 (YYYY-MM-DD)

    // Many-to-many relationship with Movie
    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.PERSIST})
    @JoinTable(
            name = "actor_movie",
            joinColumns = @JoinColumn(name = "actor_id"),
            inverseJoinColumns = @JoinColumn(name = "movie_id"))
    // Ignoring json for evading response recursion
    @JsonIgnore
    private Set<Movie> actorMovies = new HashSet<>();

    // Constructor for requests
    public Actor() {
    }

    // Constructor without id
    public Actor(String name, String birthDate, Set<Movie> actorMovies) {
        this.name = name;
        this.birthDate = birthDate;
        this.actorMovies = actorMovies;
    }

    // Constructor with id
    public Actor(Long id, String name, String birthDate, Set<Movie> actorMovies) {
        this.id = id;
        this.name = name;
        this.birthDate = birthDate;
        this.actorMovies = actorMovies;
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

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate)
            throws DateTimeException, BadRequestException {

        DateTimeFormatter validationPattern = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        try {
            LocalDate date = LocalDate.parse(birthDate, validationPattern);
        } catch (DateTimeException e) {
            throw new BadRequestException("Date format must be ISO 8601 (YYYY-MM-DD)");
        }

        this.birthDate = birthDate;
    }

    public Set<Movie> getActorMovies() {
        return actorMovies;
    }

    public void setActorMovies(Set<Movie> actorMovies) {
        this.actorMovies = actorMovies;
    }
}
