package com.projeto.controleanimal.controller.animal;

import com.projeto.controleanimal.controller.model.Cat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class Controller {

    @GetMapping("/boris")
    public Cat home(){
        Cat cat = new Cat("Boris", 5);
        return cat;
    }



}
