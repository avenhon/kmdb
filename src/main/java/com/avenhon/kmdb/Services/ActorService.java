package com.avenhon.kmdb.Services;

import com.avenhon.kmdb.Entities.Actor;
import com.avenhon.kmdb.ExceptionHandler.BadRequestException;
import com.avenhon.kmdb.ExceptionHandler.ResourceNotFoundException;
import com.avenhon.kmdb.Repositories.ActorRepository;
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
public class ActorService {
    private final ActorRepository actorRepository;
    private final MovieRepository movieRepository;

    // Dependency injection
    @Autowired
    public ActorService(ActorRepository actorRepository, MovieRepository movieRepository) {
        this.actorRepository = actorRepository;
        this.movieRepository = movieRepository;
    }

    public List<Actor> getActors(Pageable pageable) {
        return actorRepository.findAll(pageable).getContent();
    }

    public List<Actor> getActorsFromName(Optional<String> name, Pageable pageable) {
        return actorRepository.getActorsFromName(name.get(), pageable).getContent();
    }

    public Actor getActorById(Long id)
            throws ResourceNotFoundException {

        return actorRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Actor not found with ID: " + id));
    }

    public ResponseEntity<Actor> addActor(Actor actor)
            throws BadRequestException {

        if (actor.getName() == null) {
            throw new BadRequestException("Name cannot be null");
        }

        actorRepository.save(actor);
        return new ResponseEntity<Actor>(actor, HttpStatus.CREATED);
    }

    public ResponseEntity<Actor> patchActor(Long existenceActorId, Actor toUpdateActor)
            throws ResourceNotFoundException, BadRequestException {

        Actor existenceActor = actorRepository.findById(existenceActorId).orElseThrow(
                () -> new ResourceNotFoundException("Actor not found with ID: " + existenceActorId));

        if (toUpdateActor.getName() != null) {
            existenceActor.setName(toUpdateActor.getName());
        }

        if (toUpdateActor.getBirthDate() != null) {
            existenceActor.setBirthDate(toUpdateActor.getBirthDate());
        }

        if (toUpdateActor.getActorMovies() != null) {
            existenceActor.setActorMovies(toUpdateActor.getActorMovies());
        }

        actorRepository.save(existenceActor);
        return new ResponseEntity<Actor>(existenceActor, HttpStatus.OK);
    }

    @Transactional
    public void deleteActor(Long id, boolean force)
            throws ResourceNotFoundException, BadRequestException {

        // handler for non-existing actor
        Actor actor = actorRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Actor not found with ID: " + id));

        // if force equal false and actor have associated movies throw exception
        if (!actor.getActorMovies().isEmpty() && !force) {
            throw new BadRequestException("Cannot delete actor " + "'" + actor.getName() + "'" +
                    " because it has " + actor.getActorMovies().size() + " associated movies.");
        }

        // if force equal true remove all movies which contains actor
        if (force) {
            actor.getActorMovies().forEach(movie -> {
                movieRepository.deleteById(movie.getId());
            });
        }

        actorRepository.deleteById(id);
    }
}
