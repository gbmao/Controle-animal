package com.projeto.controleanimal.service;

import com.projeto.controleanimal.dto.appUser.UserCreationDto;
import com.projeto.controleanimal.model.AppUser;
import com.projeto.controleanimal.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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
}
