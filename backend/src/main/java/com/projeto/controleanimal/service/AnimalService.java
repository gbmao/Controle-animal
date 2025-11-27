package com.projeto.controleanimal.service;

import com.projeto.controleanimal.dto.AnimalCreationDto;
import com.projeto.controleanimal.dto.AnimalDto;
import com.projeto.controleanimal.dto.AnimalUpdateDto;
import com.projeto.controleanimal.dto.AnimalWithImgIdReturnDto;
import com.projeto.controleanimal.model.Animal;
import com.projeto.controleanimal.model.AppUser;
import com.projeto.controleanimal.model.Cat;
import com.projeto.controleanimal.model.Image;
import com.projeto.controleanimal.repository.AnimalRepository;
import com.projeto.controleanimal.repository.UserRepository;
import com.projeto.controleanimal.util.AnimalValidator;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class AnimalService {

    private final AnimalValidator validator;
    private final AnimalRepository repo;
    private final UserRepository userRepository;

    public AnimalService(AnimalRepository repo, AnimalValidator validator, UserRepository userRepository) {
        this.repo = repo;
        this.validator = validator;
        this.userRepository = userRepository;
    }

    public List<AnimalWithImgIdReturnDto> getAllAnimals(Long userId) {
        var user = userRepository.findById(userId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Como voce chegou ate aqui?"));

        if (user.getAnimalIds().isEmpty()) return List.of(); //retorna uma lista vazia, será a melhor resposta???

        return user.getAnimalIds().stream()
                .map(repo::findById)
                .flatMap(Optional::stream)
                .map(s -> new AnimalWithImgIdReturnDto(s.getId(),
                        s.getName(),
                        s.getAge(),
                        s.getClass().getSimpleName()
                        , s.getImage() == null ? -1 : s.getImage().getId()))
                .toList();

    }


    public AnimalDto getAnimal(Long id) {

        var animal = repo.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Nao encontramos animal com o id: " + id));

        return new AnimalDto(animal.getId(), animal.getName(), animal.getAge(), animal.getClass().getSimpleName());
    }


    public AnimalWithImgIdReturnDto addAnimal(AnimalCreationDto animalDto, MultipartFile multipartImage, Long userId) throws Exception {


        validator.validate(animalDto.name());
        validator.validate(animalDto.birthDate());

        Animal animal = createAnimalEntity(animalDto);

        if (multipartImage == null || multipartImage.isEmpty()) return saveAndReturnDto(animal, userId);

        Image dbImage = createImageEntity(multipartImage);

        return saveAndReturnDto(animal, dbImage, userId);
    }

    public void deleteAnimal(Long id) {
        Animal animal = repo.findById(id).orElseThrow();

        animal.setImage(null);

        repo.delete(animal);
        //colocar alguma msg avisando que foi deletado???
    }

    public AnimalDto changeAnimal(Long animalId, AnimalUpdateDto animalUpdateDto) {

        var animalToBeChanged = repo.findById(animalId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Id não encontrado"));


        if (animalUpdateDto.name() != null) {
            validator.validate(animalUpdateDto.name());
            animalToBeChanged.setName(animalUpdateDto.name());
        }

        if (animalUpdateDto.birthDate() != null) {
            validator.validate(animalUpdateDto.birthDate());
            animalToBeChanged.setBirthDate(animalUpdateDto.birthDate());
        }

        repo.save(animalToBeChanged);

        return new AnimalDto(animalToBeChanged.getId(), animalToBeChanged.getName(), animalToBeChanged.getAge(),
                animalToBeChanged.getClass().getSimpleName());
    }


    public Long getIdByName(String name) {

        return repo.findByNameIgnoreCase(name)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Nome não encotnrado!"))
                .getId();
    }

    public List<AnimalDto> getListOfAnimals(String name) {
        return repo.findByNameContainingIgnoreCase(name).stream()
                .limit(10) // limitar para um numero maximo
                .map(a -> new AnimalDto(
                        a.getId(),
                        a.getName(),
                        a.getAge(),
                        a.getClass().getSimpleName()))
                .toList();

    }

    private Animal createAnimalEntity(AnimalCreationDto animalDto) {

        return switch ((animalDto.type() == null ? "Classe generica " : animalDto.type().toLowerCase())) {
            case "cat" -> new Cat(animalDto.name(), animalDto.birthDate());
            default ->
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Tipo invalido"); // TODO criar uma classe generica para aceitar quando vier null
        };
    }

    private Image createImageEntity(MultipartFile multipartImage) throws Exception {
        Image dbImage = new Image();
        dbImage.setName(multipartImage.getOriginalFilename()); // nao sei bem como funciona existe a opcao de pegar com getName
        dbImage.setContent(multipartImage.getBytes());

        return dbImage;
    }

    private AnimalWithImgIdReturnDto saveAndReturnDto(Animal animal, Long userId) {
        repo.save(animal);
        //colocando o id do animal no User
        saveAnimalIdToUser(animal, userId);

        return new AnimalWithImgIdReturnDto(animal.getId(), animal.getName(),
                animal.getAge(),
                animal.getClass().getSimpleName(),
                null);
    }

    private AnimalWithImgIdReturnDto saveAndReturnDto(Animal animal, Image dbImage, Long userId) {
        dbImage.setAnimal(animal);
        animal.setImage(dbImage);

        repo.save(animal);
        //colocando o id do animal no User
        saveAnimalIdToUser(animal, userId);
        return new AnimalWithImgIdReturnDto(animal.getId(), animal.getName(), animal.getAge(), animal.getClass().getSimpleName(),
                animal.getImage().getId());
    }

    private void saveAnimalIdToUser(Animal animal, Long userId) {

        var user = userRepository.findById(userId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Como voce chegou ate aqui?"));

        user.addAnimal(animal.getId());
        userRepository.save(user);
    }
}
