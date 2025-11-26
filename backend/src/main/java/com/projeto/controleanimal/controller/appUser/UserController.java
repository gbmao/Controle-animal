package com.projeto.controleanimal.controller.appUser;

import com.projeto.controleanimal.dto.appUser.UserCreationDto;
import com.projeto.controleanimal.service.UserService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @PostMapping
    void addUser(@RequestBody UserCreationDto userCreationDto) {

        service.addUser(userCreationDto);
    }
}
