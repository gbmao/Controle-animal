package com.projeto.controleanimal.model;

public abstract class Animal {
    private String name;
    private int age;


    Animal(String name, int age) {
        if(name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Nome vazio!");
        }
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    // setters devem ser chamados apenas por  funçoes especificas
    //ex crie uma funcao changeName()
    void setName(String name) {
        this.name = name;
    }

    //eventualmente criar um envelhecimento automatico baseado na data de nascimento do animal
    // funcao birthday() que alem de alterar a idade daria um aviso de aniversario
    void setAge(int age) {
        this.age = age;
    }
}
