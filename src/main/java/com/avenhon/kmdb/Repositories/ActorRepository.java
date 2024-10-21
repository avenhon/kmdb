package com.avenhon.kmdb.Repositories;

import com.avenhon.kmdb.Entities.Actor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ActorRepository extends JpaRepository<Actor, Long> {

    // Compare name substring which contains only firstname for custom query
    @Query(value = "SELECT * FROM actor WHERE LOWER(substr(name, 1, instr(name, ' ') - 1)) = LOWER(:name)",
            countQuery = "SELECT COUNT(*) FROM actor WHERE LOWER(substr(name, 1, instr(name, ' ') - 1)) = LOWER(:name)",
            nativeQuery = true)
    Page<Actor> getActorsFromName(@Param("name") String name, Pageable pageable);
}
