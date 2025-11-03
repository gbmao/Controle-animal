package com.projeto.controleanimal.controller.animal;

import com.projeto.controleanimal.model.Animal;
import com.projeto.controleanimal.model.Cat;
import com.projeto.controleanimal.service.AnimalService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class Controller {
    private final AnimalService service;

    public Controller(AnimalService service) {
        this.service = service;
    }


    @GetMapping("/{name}")
    public Animal getAnimal(@PathVariable("name") String name) {
     return service.getAnimal(name);
    }

    @GetMapping("/all")
    public List<Animal> getAllAnimals() {
        return service.getAllAnimals();
    }

    @PostMapping()
    public Animal addCat(@RequestBody Cat cat){
        return service.addAnimal(cat);
    }

    // Necessario chamar name como parametro para deletar!
    //ex: http://localhost:8080/api/remove?name=Ivaldo

    @DeleteMapping("/remove")
    public Animal deleteAnimal(@RequestParam String name) {
        return service.deleteAnimal(name);
    }

}
