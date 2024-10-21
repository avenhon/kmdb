package com.avenhon.kmdb.DTO;

import java.util.Set;

public class MovieDTO {
    private String title;
    private Integer releaseYear;
    private Integer duration;
    private Set<Long> movieGenres;
    private Set<Long> movieActors;

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

    public Set<Long> getMovieGenres() {
        return movieGenres;
    }

    public void setMovieGenres(Set<Long> movieGenres) {
        this.movieGenres = movieGenres;
    }

    public Set<Long> getMovieActors() {
        return movieActors;
    }

    public void setMovieActors(Set<Long> movieActors) {
        this.movieActors = movieActors;
    }
}
