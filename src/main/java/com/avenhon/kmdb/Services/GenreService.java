package com.avenhon.kmdb.Services;

import com.avenhon.kmdb.Entities.Genre;
import com.avenhon.kmdb.ExceptionHandler.BadRequestException;
import com.avenhon.kmdb.ExceptionHandler.ResourceNotFoundException;
import com.avenhon.kmdb.Repositories.GenreRepository;
import com.avenhon.kmdb.Repositories.MovieRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GenreService {

    private final GenreRepository genreRepository;
    private final MovieRepository movieRepository;

    @Autowired
    public GenreService(GenreRepository genreRepository, MovieRepository movieRepository) {
        this.genreRepository = genreRepository;
        this.movieRepository = movieRepository;
    }

    public List<Genre> getGenres(Pageable pageable) {
        return genreRepository.findAll(pageable).getContent();
    }

    public Genre getGenre(Long id) throws ResourceNotFoundException {
        return genreRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Genre not found with ID: " + id));
    }

    public ResponseEntity<Genre> addGenre(Genre genre)
            throws BadRequestException {

        if (genre.getName() == null) {
            throw new BadRequestException("Title shouldn't be empty");
        }

        genreRepository.save(genre);
        return new ResponseEntity<Genre>(genre, HttpStatus.CREATED);
    }

    public ResponseEntity<Genre> patchGenre(Long existingGenreId, Genre toUpdateGenre)
            throws ResourceNotFoundException {

        Genre existingGenre = genreRepository.findById(existingGenreId).orElseThrow(
                () -> new ResourceNotFoundException("Genre not found with ID: " + existingGenreId));

        if (toUpdateGenre.getName() != null) {
            existingGenre.setName(toUpdateGenre.getName());
        }

        if (toUpdateGenre.getGenreMovies() != null) {
            existingGenre.setGenreMovies(toUpdateGenre.getGenreMovies());
        }

        genreRepository.save(existingGenre);
        return new ResponseEntity<Genre>(existingGenre, HttpStatus.OK);
    }

    @Transactional
    public void deleteGenreById(Long id, boolean force)
            throws ResourceNotFoundException, BadRequestException {

        Genre genre = genreRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Genre not found with ID: " + id));

        if (!genre.getGenreMovies().isEmpty() && !force) {
            throw new BadRequestException("Cannot delete genre " + "'" + genre.getName() + "'" +
                    " because it has " + genre.getGenreMovies().size() + " associated movies.");
        }

        if (force) {
            genre.getGenreMovies().forEach(movie -> {
                movieRepository.deleteById(movie.getId());
            });
        }

        genreRepository.deleteById(id);
    }
}
