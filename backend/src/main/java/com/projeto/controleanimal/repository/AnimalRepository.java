package com.projeto.controleanimal.repository;

import com.projeto.controleanimal.model.Animal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AnimalRepository extends JpaRepository<Animal,Long> {
    boolean existsByNameIgnoreCase(String name);
    Optional<Animal> findByNameIgnoreCase(String name);
    List<Animal> findByNameContainingIgnoreCase(String name);
}
