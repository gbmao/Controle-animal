package com.projeto.controleanimal.service;

import com.projeto.controleanimal.config.SecurityConfig;
import com.projeto.controleanimal.dto.user.UserCreationDto;
import com.projeto.controleanimal.model.User;
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

        User user = new User(userDto.email(), userDto.login(), hashed);

        repo.save(user);
    }
}
