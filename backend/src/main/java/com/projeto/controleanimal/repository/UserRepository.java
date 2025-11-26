package com.projeto.controleanimal.repository;

import com.projeto.controleanimal.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
