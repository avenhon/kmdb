package com.avenhon.kmdb.Services;

import com.avenhon.kmdb.DTO.MovieDTO;
import com.avenhon.kmdb.Entities.Actor;
import com.avenhon.kmdb.Entities.Genre;
import com.avenhon.kmdb.Entities.Movie;
import com.avenhon.kmdb.ExceptionHandler.BadRequestException;
import com.avenhon.kmdb.ExceptionHandler.ResourceNotFoundException;
import com.avenhon.kmdb.Repositories.ActorRepository;
import com.avenhon.kmdb.Repositories.GenreRepository;
import com.avenhon.kmdb.Repositories.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class MovieService {
    private final MovieRepository movieRepository;
    private final GenreRepository genreRepository;
    private final ActorRepository actorRepository;

    @Autowired
    public MovieService(MovieRepository movieRepository, GenreRepository genreRepository, ActorRepository actorRepository) {
        this.movieRepository = movieRepository;
        this.genreRepository = genreRepository;
        this.actorRepository = actorRepository;
    }

    public Optional<Movie> getMovieById(Long id) throws ResourceNotFoundException {
        return Optional.of(movieRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Movie not found with ID: " + id)));
    }

    public Page<Movie> getMovies(Pageable pageable) {
        return movieRepository.findAll(pageable);
    }

    public List<Movie> searchMovie(String title) {
        return new PageImpl<>(movieRepository.findAll().stream().filter(movie ->
                movie.getTitle().toLowerCase().contains(title.toLowerCase()))
                .toList()).getContent();
    }

    public Page<Movie> getMovieWithGenreId(Long genreId, Pageable pageable) {
        return movieRepository.getMovieWithGenreId(genreId, pageable);
    }

    public Page<Movie> getMovieWithReleaseYear(Integer year, Pageable pageable) {
        return movieRepository.getMovieWithReleaseYear(year, pageable);
    }

    public Page<Movie> getMovieWithActorId(Long actorId, Pageable pageable) {
        return movieRepository.getMovieWithActorId(actorId, pageable);
    }

    public Set<Actor> getActorsFromMovie(Long movieId)
            throws ResourceNotFoundException {

        Movie movie = movieRepository.findById(movieId).orElseThrow(
                () -> new ResourceNotFoundException("Movie not found with ID: " + movieId));

        return movie.getMovieActors();
    }

    public ResponseEntity<Movie> addMovie(MovieDTO movieDTO)
            throws BadRequestException {

        Movie movie = new Movie();

        if (movieDTO.getTitle() == null) {
            throw new BadRequestException("Title field cannot be null");
        } else if (movieDTO.getReleaseYear() == null) {
            throw new BadRequestException("Release Year field cannot be null");
        } else if (movieDTO.getDuration() == null) {
            throw new BadRequestException("Duration field cannot be null");
        };

        movie.setTitle(movieDTO.getTitle());
        movie.setReleaseYear(movieDTO.getReleaseYear());
        movie.setDuration(movieDTO.getDuration());

        if (!movieDTO.getMovieGenres().isEmpty()) {
            Set<Genre> genres = new HashSet<>(genreRepository.findAllById(movieDTO.getMovieGenres()));

            for (Genre genre : genres) {
                genre.getGenreMovies().add(movie);
            }

            movie.setMovieGenres(genres);
        }

        if (!movieDTO.getMovieActors().isEmpty()) {
            Set<Actor> actors = new HashSet<>(actorRepository.findAllById(movieDTO.getMovieActors()));

            for (Actor actor : actors) {
                actor.getActorMovies().add(movie);
            }

            movie.setMovieActors(actors);
        }

        movieRepository.save(movie);
        return new ResponseEntity<Movie>(movie, HttpStatus.CREATED);
    }

    public ResponseEntity<Movie> patchMovie(Long movieId, MovieDTO movieDTO)
            throws ResourceNotFoundException {

        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new ResourceNotFoundException("Movie not found with ID: " + movieId));

        if (movieDTO.getTitle() != null) {
            movie.setTitle(movieDTO.getTitle());
        }

        if (movieDTO.getDuration() != null) {
            movie.setDuration(movieDTO.getDuration());
        }

        if (movieDTO.getReleaseYear() != null) {
            movie.setReleaseYear(movieDTO.getReleaseYear());
        }

        if (movieDTO.getMovieGenres() != null) {

            for (Genre genre : movie.getMovieGenres()) {
                genre.getGenreMovies().remove(movie);
            }

            movie.getMovieGenres().clear();

            Set<Genre> genres = new HashSet<>(genreRepository.findAllById(movieDTO.getMovieGenres()));

            for (Genre genre : genres) {
                genre.getGenreMovies().add(movie);
            }

            movie.setMovieGenres(genres);
        }

        if (movieDTO.getMovieActors() != null) {
            Set<Actor> actors = new HashSet<>(actorRepository.findAllById(movieDTO.getMovieActors()));

            for (Actor actor : movie.getMovieActors()) {
                actor.getActorMovies().remove(movie);
            }

            movie.getMovieActors().clear();


            for (Actor actor : actors) {
                actor.getActorMovies().add(movie);
            }

            movie.setMovieActors(actors);
        }

        movieRepository.save(movie);
        return new ResponseEntity<Movie>(movie, HttpStatus.OK);
    }

    public void deleteMovie(Long id)
            throws ResourceNotFoundException {

        if (!movieRepository.existsById(id)) {
            throw new ResourceNotFoundException("Movie not found with ID: " + id);
        }

        movieRepository.deleteById(id);
    }
}
