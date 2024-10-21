package com.avenhon.kmdb.Controllers;

import com.avenhon.kmdb.Entities.Actor;
import com.avenhon.kmdb.ExceptionHandler.BadRequestException;
import com.avenhon.kmdb.ExceptionHandler.ResourceNotFoundException;
import com.avenhon.kmdb.Services.ActorService;
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

@RestController
@RequestMapping(path = "/api/actors")
public class ActorController {
    private final ActorService actorService;

    // Dependency injection
    @Autowired
    public ActorController(ActorService actorService) {
        this.actorService = actorService;
    }

    // Request for getting actors
    @GetMapping
    public List<Actor> getActors(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "true") boolean ascending,
            @RequestParam(required = false, name = "name") Optional<String> name)
            throws BadRequestException {

        if (page < 0 || size < 0) {
            throw new BadRequestException("Page or size cannot be less than zero");
        }

        Sort sort = ascending ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);

        if (name.isPresent()) {
            return actorService.getActorsFromName(name, pageable);
        }

        return actorService.getActors(pageable);
    }

    // Request for getting actors by id
    @GetMapping(path = "/{id}")
    public Actor getActorById(@PathVariable Long id)
            throws ResourceNotFoundException {

        return actorService.getActorById(id);
    }

    // Request for create new Actor
    // Input validating using Bean
    @PostMapping
    public ResponseEntity<Actor> addActor(@Valid @RequestBody Actor actor)
            throws BadRequestException {

        return actorService.addActor(actor);
    }

    // Request for edit actor
    @PatchMapping(path = "/{actorId}")
    public ResponseEntity<Actor> patchActor(@PathVariable("actorId") Long existenceActorId, @RequestBody Actor toUpdateActor)
            throws ResourceNotFoundException, BadRequestException {

        if (toUpdateActor == null) {
            throw new BadRequestException("Request Body shouldn't be empty");
        }

        try {
           return actorService.patchActor(existenceActorId, toUpdateActor);
        } catch (BadRequestException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    // Request for delete Actor by id
    @DeleteMapping(path = "/{id}")
    public ResponseEntity<HttpStatus> deleteActor(
            @PathVariable Long id,
            @RequestParam(required = false, defaultValue = "false") boolean force)
            throws ResourceNotFoundException, BadRequestException {

        actorService.deleteActor(id, force);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
