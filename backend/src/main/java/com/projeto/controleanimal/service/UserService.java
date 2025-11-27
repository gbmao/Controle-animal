package com.projeto.controleanimal.service;

import com.projeto.controleanimal.dto.appUser.UserCreationDto;
import com.projeto.controleanimal.model.AppUser;
import com.projeto.controleanimal.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class UserService {

    private final UserRepository repo;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository repo, PasswordEncoder passwordEncoder) {
        this.repo = repo;
        this.passwordEncoder = passwordEncoder;
    }

    public void addUser(UserCreationDto userDto) {

        //TODO fazer a checagem

        String hashed = passwordEncoder.encode(userDto.password());

        AppUser appUser = new AppUser(userDto.email(), userDto.login(), hashed);

        appUser.getRoles().add("APPUSER");

        repo.save(appUser);
    }

    public void checkAnimalId(Long user, Long animalId) {

        if(!instantiateUser(user)
                .getAnimalIds().contains(animalId)) throw new  ResponseStatusException(HttpStatus.FORBIDDEN,"Este User nao possui animal com o id: " + animalId);
    }

    public void deleteAnimalId(Long user, Long animalId) {
       AppUser appUser = instantiateUser(user);
       appUser.removeAnimal(animalId);
       repo.save(appUser);

    }

    private AppUser instantiateUser(Long user) {
        return repo.findById(user)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"User nao encontrado"));
    }
}
