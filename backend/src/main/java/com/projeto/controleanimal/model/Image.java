package com.projeto.controleanimal.model;

import jakarta.persistence.*;

@Entity
public class Image {


    //TODO checar se animal só tem uma imagem
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "image_seq_generator")
    @SequenceGenerator(
            name = "image_seq_generator",
            sequenceName = "image_id_seq",
            allocationSize = 1
    )
    Long id;


    @Column(columnDefinition = "BYTEA")
    byte[] content;

    @OneToOne
    @JoinColumn(name = "animal_id")
    private Animal animal;

    String name;

//    public Image(Long animal_id,byte[] content, String name) {
//        this.content = content;
//        this.name = name;
//    }


    public Image() {
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Animal getAnimal() {
        return animal;
    }

    public void setAnimal(Animal animal) {
        this.animal = animal;
    }

    public Long getId() {
        return id;
    }

}
