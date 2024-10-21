package com.avenhon.kmdb.Repositories;

import com.avenhon.kmdb.Entities.Movie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {

    // Custom query which getting all movies with genre ids in sqlite database
    @Query(value = "SELECT m.* FROM movie m " +
            "JOIN genre_movie gm ON m.id = gm.movie_id " +
            "JOIN genre g ON g.id = gm.genre_id " +
            "WHERE g.id = :genre " +
            "GROUP BY m.id",
            countQuery = "SELECT COUNT(DISTINCT m.id) FROM movie m " +
                    "JOIN genre_movie gm ON m.id = gm.movie_id " +
                    "JOIN genre g ON g.id = gm.genre_id " +
                    "WHERE g.id = :genre",
            nativeQuery = true)
    Page<Movie> getMovieWithGenreId(@Param("genre") Long genreId, Pageable pageable);

    // Custom query which getting all movies with release year in sqlite database
    @Query(value = "SELECT * FROM movie WHERE release_year = :year",
        countQuery = "SELECT count(*) FROM movie WHERE release_year = :year",
        nativeQuery = true)
    Page<Movie> getMovieWithReleaseYear(@Param("year") Integer year, Pageable pageable);

    // Custom query which getting all movies with actor id in sqlite database
    @Query(value = "SELECT m.* FROM movie m " +
            "JOIN actor_movie am ON m.id = am.movie_id " +
            "JOIN actor a ON a.id = am.actor_id " +
            "WHERE a.id = :actorId " +
            "GROUP BY m.id",
            countQuery = "SELECT COUNT(DISTINCT m.id) FROM movie m " +
                    "JOIN actor_movie am ON m.id = am.movie_id " +
                    "JOIN actor a ON a.id = am.actor_id " +
                    "WHERE a.id = :actorId",
            nativeQuery = true)
    Page<Movie> getMovieWithActorId(@Param("actorId") Long actorId, Pageable pageable);
}
