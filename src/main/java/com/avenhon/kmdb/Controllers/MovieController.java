package com.avenhon.kmdb.Controllers;

import com.avenhon.kmdb.DTO.MovieDTO;
import com.avenhon.kmdb.Entities.Actor;
import com.avenhon.kmdb.Entities.Movie;
import com.avenhon.kmdb.ExceptionHandler.BadRequestException;
import com.avenhon.kmdb.ExceptionHandler.ResourceNotFoundException;
import com.avenhon.kmdb.Services.MovieService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping(path = "/api/movies")
public class MovieController {
    private final MovieService movieService;

    // Dependency injection
    @Autowired
    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    // Request for getting movies
    @GetMapping
    public List<Movie> getMovies(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "true") boolean ascending,
            @RequestParam(required = false, name = "genre") Optional<Long> genreId,
            @RequestParam(required = false, name = "year") Optional<Integer> year,
            @RequestParam(required = false, name = "actor") Optional<Long> actorId)
            throws BadRequestException {

        if (page < 0 || size < 0) {
            throw new BadRequestException("Page or size cannot be less than zero");
        }

        // Sorting and pagination
        Sort sort = ascending ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);

        if (genreId.isPresent()) {
            return movieService.getMovieWithGenreId(genreId.get(), pageable).getContent();
        }
        else if (year.isPresent()) {
            return movieService.getMovieWithReleaseYear(year.get(), pageable).getContent();
        } else if (actorId.isPresent()) {
            return movieService.getMovieWithActorId(actorId.get(), pageable).getContent();
        }

        return movieService.getMovies(pageable).getContent();
    }

    // Request for searching movie
    @GetMapping(path = "/search")
    public List<Movie> searchMovie(
            @RequestParam(defaultValue = "") String title) {

        return movieService.searchMovie(title);
    }

    // Request for getting movie actors
    @GetMapping(path = "/{id}/actors")
    public Set<Actor> getActorsFromMovie(@PathVariable(name = "id") Long movieId)
            throws ResourceNotFoundException {

        return movieService.getActorsFromMovie(movieId);
    }

    // Request for getting Movie by id
    @GetMapping(path = "/{id}")
    public Movie getMovieById(
            @PathVariable Long id)
            throws ResourceNotFoundException {

        Optional<Movie> movie = movieService.getMovieById(id);

        if (movie.isPresent()) {
            return movie.get();
        } else {
            throw new ResourceNotFoundException("Movie not found with ID: " + id);
        }
    }

    // Request for create new Movie
    @PostMapping
    public ResponseEntity<Movie> addMovie(@Valid @RequestBody MovieDTO movieDTO)
            throws BadRequestException {

        return movieService.addMovie(movieDTO);
    }

    // Request for edit movie by id
    @PatchMapping(path = "/{movieId}")
    public ResponseEntity<Movie> patchMovie(@PathVariable Long movieId, @RequestBody MovieDTO movieDTO)
            throws ResourceNotFoundException, BadRequestException {

        if (movieDTO == null) {
            throw new BadRequestException("Request Body shouldn't be empty");
        }

        return movieService.patchMovie(movieId, movieDTO);
    }

    // Request for delete movie
    @DeleteMapping(path = "/{id}")
    public ResponseEntity<HttpStatus> deleteMovie(@PathVariable Long id)
            throws ResourceNotFoundException {

        movieService.deleteMovie(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
