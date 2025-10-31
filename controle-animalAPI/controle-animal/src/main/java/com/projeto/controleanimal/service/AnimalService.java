package com.projeto.controleanimal.service;

import com.projeto.controleanimal.model.Animal;
import com.projeto.controleanimal.model.Cat;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class AnimalService {

    private Map<String,Animal> animals = new LinkedHashMap<>();

    public AnimalService() {

        //---------------------------------------------------
        //criado para testes
        animals.put("boris",new Cat("Boris", 5));
        animals.put("jorge",new Cat("Jorge", 3));

    }

    public Map<String, Animal> getAnimals() {
        return Map.copyOf(animals);
    }

    //retorna todos os animais
    public List<Animal> getAllAnimals() {
        return new ArrayList<>(this.animals.values());
    }

    /**
     *  Retorna NULL caso o nome não exista!(
     * @param name
     * @return O animal
     */
    public Animal getAnimal(String name) {
        return animals.getOrDefault(name, null); //TODO lançar uma excessao em caso de null
    }
}
