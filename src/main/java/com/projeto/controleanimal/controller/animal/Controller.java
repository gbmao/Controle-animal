package com.projeto.controleanimal.controller.animal;

import com.projeto.controleanimal.dto.AnimalDto;
import com.projeto.controleanimal.model.Animal;
import com.projeto.controleanimal.model.Cat;
import com.projeto.controleanimal.service.AnimalService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class Controller {
    private final AnimalService service;

    private final String secret = System.getenv("API_SECRET");

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


    @PostMapping()
    public Animal addAnimal(@RequestHeader("x-api-key") String key, @RequestBody AnimalDto dto) {
        if (!secret.equals(key)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Não autorizado");
        }
        return service.addAnimal(dto);
    }

    // Necessario chamar name como parametro para deletar!
    //ex: http://localhost:8080/api/remove?name=Ivaldo

    @DeleteMapping("/remove")
    public void deleteAnimal(@RequestHeader("x-api-key") String key, @RequestParam Long id) {
        if (!secret.equals(key)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Não autorizado");
        }
        service.deleteAnimal(id);
    }

}
