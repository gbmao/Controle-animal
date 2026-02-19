package com.projeto.controleanimal.model;

import com.projeto.controleanimal.model.vetRecord.VeterinaryRecord;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.Period;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "type")
public abstract class Animal { //removido o abstract para teste do postqgre
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private LocalDate birthDate;
    private int age;

    @OneToOne(mappedBy = "animal",cascade = CascadeType.ALL, orphanRemoval = true)
    private Image image;

    @OneToOne(mappedBy = "animal",cascade = CascadeType.ALL, orphanRemoval = true)
    private VeterinaryRecord veterinaryRecord;

    @Version
    private Long version;

    public Animal() {
    }

    Animal(String name, LocalDate birthDate) {
        if(name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Nome vazio!");
        }
        this.name = name;
        this.birthDate = birthDate; // animal esta sendo criado com idade 0 caso null
    }

    public String getName() {
        return name;
    }

    public int getAge() {

        if(this.birthDate == null) return 0; // apenas para evitar null pointer exception para os cats criados antes

        Period period = Period.between(birthDate, LocalDate.now());
        return period.getYears();
    }

    public int getMonth() {
        if(this.birthDate == null) return 0; // apenas para evitar null pointer exception para os cats criados antes

        Period period = Period.between(birthDate, LocalDate.now());
        return period.getMonths();
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Long getId() {
        return id;
    }


    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public VeterinaryRecord getVeterinaryRecord() {
        return veterinaryRecord;
    }

    public void setVeterinaryRecord(VeterinaryRecord veterinaryRecord) {
        this.veterinaryRecord = veterinaryRecord;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }
}
