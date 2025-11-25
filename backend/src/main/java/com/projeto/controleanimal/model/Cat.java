package com.projeto.controleanimal.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

import java.time.LocalDate;

@Entity
@DiscriminatorValue("CAT")
public class Cat extends Animal {

    public Cat(String name, LocalDate birthDate) {
        super(name, birthDate);
    }

    public Cat() {
    }

    @Override
    public String toString() {
        return "Cat{" +
                "name='" + getName() + '\'' +
                ", age=" + getAge() +
                '}';
    }


}
