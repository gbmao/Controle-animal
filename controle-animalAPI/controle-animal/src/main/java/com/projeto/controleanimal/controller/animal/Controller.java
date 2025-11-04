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


    @GetMapping("/{id}")
    public Animal getAnimal(@PathVariable("id") Long id) {
     return service.getAnimal(id);
    }

    @GetMapping("/all")
    public List<Animal> getAllAnimals() {
        return service.getAllAnimals();
    }

    //TODO checar qual tipo de animal é para criar uma instancia ESPECIFICA
    @PostMapping()
    public Animal addCat(@RequestBody Animal animal){
        return service.addAnimal(animal);
    }

    // Necessario chamar name como parametro para deletar!
    //ex: http://localhost:8080/api/remove?name=Ivaldo

    @DeleteMapping("/remove")
    public void deleteAnimal(@RequestParam Long id) {
         service.deleteAnimal(id);
    }

}
