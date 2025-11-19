package com.projeto.controleanimal.model.vetRecord;

import com.projeto.controleanimal.dto.veterinaryRecordDto.NextVisitDto;
import com.projeto.controleanimal.model.Animal;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Entity
public class VeterinaryRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "animal_id")
    private Animal animal;

    @ElementCollection
    @CollectionTable(name = "vet_visits", joinColumns = @JoinColumn(name = "record_id"))
    @OrderColumn(name = "visit_order")
    private List<VetVisits> vetVisitsList = new ArrayList<>();

    public VeterinaryRecord() {
    }

    public Animal getAnimal() {
        return animal;
    }

    public void setAnimal(Animal animal) {
        this.animal = animal;
    }

    public List<VetVisits> getVetVisitsList() {
        return vetVisitsList;
    }

    public void setVetVisitsList(List<VetVisits> vetVisitsList) {
        this.vetVisitsList = vetVisitsList;
    }


}
