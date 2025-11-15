package com.projeto.controleanimal.model;

import jakarta.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "type")
public abstract class Animal { //removido o abstract para teste do postqgre
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private int age;

    public Animal() {
    }

    Animal(String name, int age) {
        if(name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Nome vazio!");
        }
        this.name = name;
        this.age = age; //TODO animal esta sendo criado com idade 0 caso null
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    // setters devem ser chamados apenas por  funçoes especificas
    //ex crie uma funcao changeName()
    public void setName(String name) {
        this.name = name;
    }

    //eventualmente criar um envelhecimento automatico baseado na data de nascimento do animal
    // funcao birthday() que alem de alterar a idade daria um aviso de aniversario
    public void setAge(int age) {
        this.age = age;
    }

    public Long getId() {
        return id;
    }
}
