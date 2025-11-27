package com.projeto.controleanimal.controller.animal;

import com.projeto.controleanimal.dto.AnimalCreationDto;
import com.projeto.controleanimal.dto.AnimalDto;
import com.projeto.controleanimal.dto.AnimalUpdateDto;
import com.projeto.controleanimal.dto.AnimalWithImgIdReturnDto;
import com.projeto.controleanimal.model.AppUser;
import com.projeto.controleanimal.repository.UserRepository;
import com.projeto.controleanimal.security.CustomUserDetails;
import com.projeto.controleanimal.service.AnimalService;
import com.projeto.controleanimal.service.UserService;
import com.projeto.controleanimal.util.ApiKeyValidator;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api")
public class AnimalController {
    private final AnimalService service;
    private final UserService userService;

    public AnimalController(AnimalService service, UserService userService) {
        this.service = service;

        this.userService = userService;
    }


    @GetMapping("/{animalId}")
    public AnimalDto getAnimal(@PathVariable("animalId") Long animalId,
                               @AuthenticationPrincipal CustomUserDetails user) {
        userService.checkAnimalId(user.getId(), animalId);
        return service.getAnimal(animalId);
    }

    @GetMapping("/all")
    public List<AnimalWithImgIdReturnDto> getAllAnimals(@AuthenticationPrincipal CustomUserDetails user) {

        return service.getAllAnimals(user.getId());
    }



    @GetMapping("search/{name}")
    public AnimalDto getAnimalByName(@PathVariable("name") String name,
                                     @AuthenticationPrincipal CustomUserDetails user) {

        return getAnimal(service.getIdByName(name), user);
    }


    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public AnimalWithImgIdReturnDto addAnimal(@RequestHeader("x-api-key") String key,
                                              @RequestPart("data") AnimalCreationDto dto,
                                              @RequestParam("multipartImage") MultipartFile multipartImage,
                                              @AuthenticationPrincipal CustomUserDetails user) throws Exception {

        ApiKeyValidator.check(key);

        return service.addAnimal(dto, multipartImage, user.getId());
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
