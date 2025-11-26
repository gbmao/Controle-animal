package com.projeto.controleanimal.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ElementCollection
    private List<Long> animalIds = new ArrayList<>();

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false,unique = true)
    private String login;

    @Column(nullable = false)
    private String password;

    public User() {
    }

    public User(String email, String login, String password) {

        this.email = email;
        this.login = login;
        this.password = password;
    }

    public List<Long> getAnimalIds() {
        return animalIds;
    }

    public void setAnimalIds(List<Long> animalIds) {
        this.animalIds = animalIds;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    // manipulando a lista

    public void addAnimal(Long animalId) {
        this.animalIds.add(animalId);
    }

    public void removeAnimal(Long animalId) {
        this.animalIds.remove(animalId);
    }
}
