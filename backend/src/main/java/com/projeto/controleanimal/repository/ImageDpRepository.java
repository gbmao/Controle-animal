package com.projeto.controleanimal.repository;

import com.projeto.controleanimal.model.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageDpRepository extends JpaRepository<Image, Long> {
}
