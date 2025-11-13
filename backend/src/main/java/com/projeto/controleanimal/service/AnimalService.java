package com.projeto.controleanimal.service;

import com.projeto.controleanimal.database.Db;
import com.projeto.controleanimal.dto.AnimalDto;
import com.projeto.controleanimal.model.Animal;
import com.projeto.controleanimal.model.Cat;
import com.projeto.controleanimal.repository.AnimalRepository;
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

    //    public Map<String, Animal> getAnimals() {
//        return Map.copyOf(animals);
//    }
    public List<Animal> getAllAnimals() {
        return repo.findAll();
    }

    //retorna todos os animais
//    public List<Animal> getAllAnimals() {
//        return new ArrayList<>(this.animals.values());
//    }

    /**
     * Retorna NULL caso o nome não exista!(
     *
     * @param id
     * @return O animal
     */
    public Animal getAnimal(Long id) {
//        return animals.getOrDefault(name, null); //TODO lançar uma excessao em caso de null
        return repo.findById(id).orElse(null);
    }

    public Animal addAnimal(AnimalDto animalDto) {

        if(containsName(animalDto.name())) {
            throw new IllegalArgumentException("Já existe esse nome na lista");
        }
        //pega número negativo em age
        if(animalDto.age() < 0) throw new IllegalArgumentException(" número negativo");

        Animal animal = switch ((animalDto.type() == null ? "Classe generica " : animalDto.type().toLowerCase())) {
            case "cat" -> new Cat(animalDto.name(), animalDto.age());
            default -> throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Tipo invalido"); // TODO criar uma classe generica para aceitar quando vier null
        };

        repo.save(animal);
        return animal;
    }

    public void deleteAnimal(Long id) {

//        Animal removed = animals.remove(name.toLowerCase());
//        Db.saveList(animals);
//        return removed;
         repo.delete(getAnimal(id));
         //colocar alguma msg avisando que foi deletado???
    }
    
    
    //procura por nomes duplicados
    //eventualmente trocar esse method por algo mais pratico do propio postgre!!!
    private boolean containsName(String name) {
        List<Animal> listOfName = new ArrayList<>(repo.findAll());
        for (Animal a : listOfName) {
            if(a.getName().equalsIgnoreCase(name))return true;
        }
        return false;
    }
}
