package com.projeto.controleanimal.model;

import jakarta.persistence.Entity;

@Entity
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
