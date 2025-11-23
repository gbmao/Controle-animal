package com.projeto.controleanimal.service;

import com.projeto.controleanimal.dto.AnimalCreationDto;
import com.projeto.controleanimal.dto.AnimalDto;
import com.projeto.controleanimal.dto.AnimalUpdateDto;
import com.projeto.controleanimal.dto.AnimalWithImgIdReturnDto;
import com.projeto.controleanimal.model.Animal;
import com.projeto.controleanimal.model.Cat;
import com.projeto.controleanimal.model.Image;
import com.projeto.controleanimal.repository.AnimalRepository;
import com.projeto.controleanimal.util.AnimalValidator;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.*;

@Service
public class AnimalService {

    private final AnimalValidator validator;
    private final AnimalRepository repo;

    public AnimalService(AnimalRepository repo, AnimalValidator validator) {
        this.repo = repo;
        this.validator = validator;
    }

    public List<AnimalWithImgIdReturnDto> getAllAnimals() { //TODO avoid stream for better performance
        return repo.findAll().stream()
                .map(s -> new AnimalWithImgIdReturnDto(s.getId(),
                        s.getName(),
                        s.getAge(),
                        s.getClass().getSimpleName()
                ,s.getImage() ==null ? -1 : s.getImage().getId()))
                .toList();
    }


    /**
     * Retorna NULL caso o nome não exista!(
     *
     * @param id
     * @return O animal
     */
    public AnimalDto getAnimal(Long id) {

        var animal = repo.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Nao encontramos animal com o id: " + id));

        return new AnimalDto(animal.getId(), animal.getName(), animal.getAge(), animal.getClass().getSimpleName());
    }

    public AnimalWithImgIdReturnDto addAnimal(AnimalCreationDto animalDto) {


        validator.validate(animalDto.name());
        validator.validate(animalDto.birthDate());

        Animal animal = switch ((animalDto.type() == null ? "Classe generica " : animalDto.type().toLowerCase())) {
            case "cat" -> new Cat(animalDto.name(), animalDto.birthDate());
            default ->
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Tipo invalido"); // TODO criar uma classe generica para aceitar quando vier null
        };

        repo.save(animal);
        return new AnimalWithImgIdReturnDto(animal.getId(), animal.getName(),
                animal.getAge(),
                animal.getClass().getSimpleName(),
                null);
    }

    public AnimalWithImgIdReturnDto addAnimal(AnimalCreationDto animalDto, MultipartFile multipartImage) throws Exception {

        if(multipartImage == null || multipartImage.isEmpty() ) {
            return addAnimal(animalDto);
        }

        validator.validate(animalDto.name());
        validator.validate(animalDto.birthDate());

        Animal animal = switch ((animalDto.type() == null ? "Classe generica " : animalDto.type().toLowerCase())) {
            case "cat" -> new Cat(animalDto.name(), animalDto.birthDate());
            default ->
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Tipo invalido"); // TODO criar uma classe generica para aceitar quando vier null
        };


        //criando a img

        Image dbImage = new Image();
        dbImage.setName(multipartImage.getOriginalFilename()); // nao sei bem como funciona existe a opcao de pegar com getName
        dbImage.setContent(multipartImage.getBytes());
        dbImage.setAnimal(animal);

        animal.setImage(dbImage);

        repo.save(animal);
        return new AnimalWithImgIdReturnDto(animal.getId(), animal.getName(),animal.getAge(),animal.getClass().getSimpleName(),
                animal.getImage().getId());
    }

    public void deleteAnimal(Long id) {
        Animal animal = repo.findById(id).orElseThrow();

        animal.setImage(null);

        repo.delete(animal);
        //colocar alguma msg avisando que foi deletado???
    }

    public AnimalDto changeAnimal(Long animalId, AnimalUpdateDto animalUpdateDto) {

        var animalToBeChanged = repo.findById(animalId)
                .orElseThrow(); //TODO entender melhor e talvez dedicar uma classe em GlobalExceptionHandler

        if (animalUpdateDto.name() != null) {
            animalToBeChanged.setName(animalUpdateDto.name());
        }
        if (animalUpdateDto.age() != null) {
            //pega número negativo em age
            if (animalUpdateDto.age() < 0) throw new IllegalArgumentException(" número negativo");
            animalToBeChanged.setAge(animalUpdateDto.age());
        }

        repo.save(animalToBeChanged);

        return new AnimalDto(animalToBeChanged.getId(), animalToBeChanged.getName(), animalToBeChanged.getAge(),
                animalToBeChanged.getClass().getSimpleName());
    }




    public Long getIdByName(String name) {

        return repo.findAll().stream()
                .filter(a -> a.getName().equalsIgnoreCase(name)) // TODO criar metodo no repository para enviar buscar a lista inteira
                .findAny()
                .map(Animal::getId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Nome não encontrado"));
    }

    public List<AnimalDto> getListOfAnimals(String name) {
        return repo.findAll().stream()
                .filter(a -> a.getName().toLowerCase().contains(name.toLowerCase()))
                .limit(10) // limitar para um numero maximo
                .map(a -> new AnimalDto(a.getId(),
                        a.getName(),
                        a.getAge(),
                        a.getClass().getSimpleName()))
                .toList();

    }
}
