package com.projeto.controleanimal.controller.animal;

import com.projeto.controleanimal.dto.AnimalDto;
import com.projeto.controleanimal.dto.AnimalUpdateDto;
import com.projeto.controleanimal.dto.AnimalWithImgDto;
import com.projeto.controleanimal.dto.AnimalWithImgReturnDto;
import com.projeto.controleanimal.model.Animal;
import com.projeto.controleanimal.model.Cat;
import com.projeto.controleanimal.service.AnimalService;
import jakarta.annotation.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
//@CrossOrigin(origins = "*")
public class Controller {
    private final AnimalService service;

    private final String secret = System.getenv("API_SECRET");

    public Controller(AnimalService service) {
        this.service = service;
    }


    @GetMapping("/{id}")
    public AnimalDto getAnimal(@PathVariable("id") Long id) {
//        Animal animal = service.getAnimal(id);
//        return new AnimalDto(animal.getId(), animal.getName(), animal.getAge(), animal.getClass().getSimpleName());
        return service.getAnimal(id);
    }

    @GetMapping("/all")
    public List<AnimalDto> getAllAnimals() {
//        return service.getAllAnimals()
//                .stream()
//                .map(s -> new AnimalDto(s.getId(), s.getName(), s.getAge(), s.getClass().getSimpleName()))
//                .toList();

        return service.getAllAnimals();
    }

    @GetMapping("search/{name}")
    public AnimalDto getAnimalByName(@PathVariable("name") String name) {

        return getAnimal(service.getIdByName(name)); //TODO devolver um DTO com os dados requisitados
    }


    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public AnimalWithImgReturnDto addAnimal(@RequestHeader("x-api-key") String key,
                                            @RequestPart("data") AnimalDto dto,
                                            @RequestParam("multipartImage") MultipartFile multipartImage) throws Exception {

        if (!secret.equals(key)) { //TODO colocar isso em um metodo para ser chamado e evitar repeticao
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Não autorizado");
        }

        if(multipartImage.isEmpty()) {
            return  service.addAnimal(dto);
        }
        return service.addAnimal(dto, multipartImage);
    }


    @DeleteMapping("/{id}")
    public void deleteAnimal(@RequestHeader("x-api-key") String key, @PathVariable("id") Long id) {
        if (!secret.equals(key)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Não autorizado");
        }
        service.deleteAnimal(id);
    }

    @PutMapping("/{id}")
    public AnimalDto changeName(@RequestHeader("x-api-key") String key, @PathVariable("id") Long animalId,
                                      @RequestBody AnimalUpdateDto animalDto) {

        if (!secret.equals(key)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Não autorizado");
        }

        return service.changeAnimal(animalId, animalDto);
    }

    @GetMapping("/busca/{name}")
    public List<AnimalDto> getListOfAnimals(@PathVariable String name) {

        return service.getListOfAnimals(name);
    }

}
