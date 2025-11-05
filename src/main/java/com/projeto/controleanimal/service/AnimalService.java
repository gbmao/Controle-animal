package com.projeto.controleanimal.service;

import com.projeto.controleanimal.database.Db;
import com.projeto.controleanimal.model.Animal;
import com.projeto.controleanimal.repository.AnimalRepository;
import org.springframework.stereotype.Service;

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

    public Animal addAnimal(Animal animal) {
//        if (repo.containsKey(animal.getName().toLowerCase())) {
//            throw new IllegalArgumentException("Já existe esse nome na lista");
//        }
        if(containsName(animal.getName())) {
            throw new IllegalArgumentException("Já existe esse nome na lista");
        }
//        animals.put(animal.getName().toLowerCase(), animal);
//        Db.saveList(animals);
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
