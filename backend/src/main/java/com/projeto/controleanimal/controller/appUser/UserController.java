package com.projeto.controleanimal.controller.appUser;

import com.projeto.controleanimal.dto.appUser.UserCreationDto;
import com.projeto.controleanimal.service.UserService;
import com.projeto.controleanimal.util.ApiKeyValidator;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @PostMapping
    void addUser(@RequestBody UserCreationDto userCreationDto,
                 @RequestHeader("x-api-key") String key) { //TODO checar e-mail nome e senha
        ApiKeyValidator.check(key);
        service.addUser(userCreationDto);
    }
}
