package com.projeto.controleanimal.util;

import com.projeto.controleanimal.model.Animal;
import com.projeto.controleanimal.repository.AnimalRepository;
import com.projeto.controleanimal.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Component
public final class AnimalValidator {

    private final AnimalRepository repo;
    private final UserRepository userRepository;

    public AnimalValidator(AnimalRepository repo, UserRepository userRepository) {
        this.repo = repo;
        this.userRepository = userRepository;
    }

    public void validate(String name, Long userId) {

        var animalsFoundByName = repo.findByNameContainingIgnoreCase(name);

        if (animalsFoundByName.isEmpty()) return;

        var user = userRepository.findById(userId).orElseThrow(); //user já é tratado muito antes de chegar nessa camada, mas será que ainda assim é necessario tratar o erro?
        if(user.getAnimalIds() == null) return;
        List<Long> userAnimalIds = user.getAnimalIds();

        Set<Long> idsFoundByName = animalsFoundByName.stream()
                .map(Animal::getId)
                .collect(Collectors.toSet());

        Set<Long> commonIds = new HashSet<>(userAnimalIds);
        commonIds.retainAll(idsFoundByName);

        if (!commonIds.isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Já existe o nome: " + name + " na lista do user: " + user.getUsername()
            );
        }

    }

    public void validate(LocalDate birthDate) {
        if (birthDate.isAfter(LocalDate.now()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O gato ainda vai nascer?(data de nascimento no futuro");
    }

}
