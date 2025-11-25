package com.projeto.controleanimal.controller.animal;

import com.projeto.controleanimal.dto.AnimalCreationDto;
import com.projeto.controleanimal.dto.AnimalDto;
import com.projeto.controleanimal.dto.AnimalUpdateDto;
import com.projeto.controleanimal.dto.AnimalWithImgIdReturnDto;
import com.projeto.controleanimal.service.AnimalService;
import com.projeto.controleanimal.util.ApiKeyValidator;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api")
public class AnimalController {
    private final AnimalService service;


    public AnimalController(AnimalService service) {
        this.service = service;
    }


    @GetMapping("/{id}")
    public AnimalDto getAnimal(@PathVariable("id") Long id) {
        return service.getAnimal(id);
    }

    @GetMapping("/all")
    public List<AnimalWithImgIdReturnDto> getAllAnimals() {

        return service.getAllAnimals();
    }

    @GetMapping("search/{name}")
    public AnimalDto getAnimalByName(@PathVariable("name") String name) {

        return getAnimal(service.getIdByName(name));
    }


    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public AnimalWithImgIdReturnDto addAnimal(@RequestHeader("x-api-key") String key,
                                              @RequestPart("data") AnimalCreationDto dto,
                                              @RequestParam("multipartImage") MultipartFile multipartImage) throws Exception {

        ApiKeyValidator.check(key);

        return service.addAnimal(dto, multipartImage);
    }


    @DeleteMapping("/{id}")
    public void deleteAnimal(@RequestHeader("x-api-key") String key, @PathVariable("id") Long id) {
        ApiKeyValidator.check(key);
        service.deleteAnimal(id);
    }


    @PutMapping("/{id}")
    public AnimalDto changeName(@RequestHeader("x-api-key") String key, @PathVariable("id") Long animalId,
                                      @RequestBody AnimalUpdateDto animalDto) {

        ApiKeyValidator.check(key);

        return service.changeAnimal(animalId, animalDto);
    }

    @GetMapping("/busca/{name}")
    public List<AnimalDto> getListOfAnimals(@PathVariable String name) {

        return service.getListOfAnimals(name);
    }

}
