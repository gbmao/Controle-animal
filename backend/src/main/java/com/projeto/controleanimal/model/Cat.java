package com.projeto.controleanimal.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("CAT")
public class Cat extends Animal {

    public Cat(String name, int age) {
        super(name, age);
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
