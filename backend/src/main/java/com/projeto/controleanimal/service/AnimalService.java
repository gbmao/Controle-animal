package com.projeto.controleanimal.service;

import com.projeto.controleanimal.database.Db;
import com.projeto.controleanimal.dto.AnimalDto;
import com.projeto.controleanimal.dto.AnimalUpdateDto;
import com.projeto.controleanimal.model.Animal;
import com.projeto.controleanimal.model.Cat;
import com.projeto.controleanimal.repository.AnimalRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

@Service
public class AnimalService {

    //    private Map<String, Animal> animals = new LinkedHashMap<>(Db.loadList());
    private final AnimalRepository repo;

    public AnimalService(AnimalRepository repo) {
        this.repo = repo;
    }

    public List<AnimalDto> getAllAnimals() {
        return repo.findAll().stream()
                .map(s -> new AnimalDto(s.getId(), s.getName(), s.getAge(), s.getClass().getSimpleName()))
                .toList();
    }


    /**
     * Retorna NULL caso o nome não exista!(
     *
     * @param id
     * @return O animal
     */
    public AnimalDto getAnimal(Long id) {
        var animal = repo.findById(id).orElseThrow( () ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Nao encontramos animal com o id: " + id));

//        // checa se existe
//        if (animal == null) {
//            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Animal not found");
//
//        }

        return new AnimalDto(animal.getId(), animal.getName(), animal.getAge(), animal.getClass().getSimpleName());
    }

    public Animal addAnimal(AnimalDto animalDto) {

        if (containsName(animalDto.name())) {
            throw new IllegalArgumentException("Já existe esse nome na lista");
        }
        //pega número negativo em age
        if (animalDto.age() < 0) throw new IllegalArgumentException(" número negativo");

        Animal animal = switch ((animalDto.type() == null ? "Classe generica " : animalDto.type().toLowerCase())) {
            case "cat" -> new Cat(animalDto.name(), animalDto.age());
            default ->
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Tipo invalido"); // TODO criar uma classe generica para aceitar quando vier null
        };

        repo.save(animal);
        return animal;
    }

    public void deleteAnimal(Long id) {
        repo.deleteById(id);
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

        repo.save(animalToBeChanged); //TODO devolver na class especifica ao inves de Animal

        return new AnimalDto(animalToBeChanged.getId(), animalToBeChanged.getName(), animalToBeChanged.getAge(),
                animalToBeChanged.getClass().getSimpleName());
    }


    //procura por nomes duplicados
    //eventualmente trocar esse method por algo mais pratico do propio postgre!!!
    private boolean containsName(String name) {
        List<Animal> listOfName = new ArrayList<>(repo.findAll());
        for (Animal a : listOfName) {
            if (a.getName().equalsIgnoreCase(name)) return true;
        }
        return false;
    }

    public Long getIdByName(String name) {

        return repo.findAll().stream()
                .filter(a -> a.getName().equalsIgnoreCase(name)) // TODO criar metodo no repository para enviar buscar a lista inteira
                .findAny()
                .map(Animal::getId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Nome não encontrado"));
    }
}
