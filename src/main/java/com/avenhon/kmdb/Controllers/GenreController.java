package com.avenhon.kmdb.Controllers;

import com.avenhon.kmdb.Entities.Genre;
import com.avenhon.kmdb.ExceptionHandler.BadRequestException;
import com.avenhon.kmdb.ExceptionHandler.ResourceNotFoundException;
import com.avenhon.kmdb.Services.GenreService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api/genres")
public class GenreController {

    private final GenreService genreService;

    // Dependency injection
    @Autowired
    public GenreController(GenreService genreService) {
        this.genreService = genreService;
    }

    // Request for getting genres
    @GetMapping
    public List<Genre> getGenres(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "true") boolean ascending)
            throws BadRequestException {

        if (page < 0 || size < 0) {
            throw new BadRequestException("Page or size cannot be less than zero");
        }

        // Sorting and pagination
        Sort sort = ascending ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);

        return genreService.getGenres(pageable);
    }

    // Request for getting genre with id
    @GetMapping(path = "/{id}")
    public Genre getGenre(@PathVariable("id") Long id)
            throws ResourceNotFoundException {

        return genreService.getGenre(id);
    }

    // Request for creating new Genre
    // Input validating using Bean
    @PostMapping
    public ResponseEntity<Genre> addGenre(@Valid @RequestBody Genre genre)
            throws BadRequestException {

        return genreService.addGenre(genre);
    }

    // Request for editing genre by id
    @PatchMapping(path = "/{genreId}")
    public ResponseEntity<Genre> patchGenre(@PathVariable("genreId") Long existingGenreId, @RequestBody Genre toUpdateGenre)
            throws ResourceNotFoundException {

        return genreService.patchGenre(existingGenreId, toUpdateGenre);
    }

    // Request for removing Genre by id
    @DeleteMapping(path = "/{id}")
    public ResponseEntity<HttpStatus> deleteGenreById(
            @PathVariable Long id,
            @RequestParam(required = false, defaultValue = "false") boolean force)
            throws ResourceNotFoundException, BadRequestException {

        genreService.deleteGenreById(id, force);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
