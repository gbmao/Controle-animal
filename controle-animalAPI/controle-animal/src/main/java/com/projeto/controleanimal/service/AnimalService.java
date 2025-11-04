package com.projeto.controleanimal.service;

import com.projeto.controleanimal.database.Db;
import com.projeto.controleanimal.model.Animal;
import com.projeto.controleanimal.model.Cat;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class AnimalService {

    private Map<String, Animal> animals = new LinkedHashMap<>(Db.loadList());

    public AnimalService() {

        //---------------------------------------------------
        //criado para testes
//        animals.put("boris",new Cat("Boris", 7));
//        animals.put("ivaldo", new Cat("Ivaldo", 1));
//        Db.saveList(animals);
    }

    public Map<String, Animal> getAnimals() {
        return Map.copyOf(animals);
    }

    //retorna todos os animais
    public List<Animal> getAllAnimals() {
        return new ArrayList<>(this.animals.values());
    }

    /**
     * Retorna NULL caso o nome não exista!(
     *
     * @param name
     * @return O animal
     */
    public Animal getAnimal(String name) {
        return animals.getOrDefault(name, null); //TODO lançar uma excessao em caso de null
    }

    public Animal addAnimal(Cat cat) {
        animals.put(cat.getName().toLowerCase(), cat);
        Db.saveList(animals);
        return cat;
    }

    public Animal deleteAnimal(String name) {

//        Animal removed = animals.remove(name.substring(0,1).toUpperCase().concat(name.substring(1).toLowerCase()));
        Animal removed = animals.remove(name.toLowerCase());
        Db.saveList(animals);
       return removed;
    }
}
