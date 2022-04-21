package com.xyz.parking.repository;

import com.xyz.parking.entity.EntryPoint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EntryPointRepository extends JpaRepository<EntryPoint, Long> {

    Optional<EntryPoint> findFirstByName(String name);

    @Query(value = "SELECT name FROM entry_point",
        nativeQuery = true
    )
    List<String> findAllEntryPointNames();
}
