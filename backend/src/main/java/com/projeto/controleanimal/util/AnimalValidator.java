package com.projeto.controleanimal.util;

import com.projeto.controleanimal.model.Animal;
import com.projeto.controleanimal.repository.AnimalRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
@Component
public final class AnimalValidator {

    private  final AnimalRepository repo;

    public AnimalValidator(AnimalRepository repo) {
        this.repo = repo;
    }

    public  void validate(String name){

        if(repo.existsByNameIgnoreCase(name)) throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Já existe esse nome na lista");
    }

    public  void validate(LocalDate birthDate) {
        if (birthDate.isAfter(LocalDate.now())) throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"O gato ainda vai nascer?(data de nascimento no futuro");
    }

}
