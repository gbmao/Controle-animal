package com.projeto.controleanimal.service;

import com.projeto.controleanimal.dto.veterinaryRecordDto.NextVisitDto;
import com.projeto.controleanimal.dto.veterinaryRecordDto.VetVisitDto;
import com.projeto.controleanimal.dto.veterinaryRecordDto.VetVisitReturnDto;
import com.projeto.controleanimal.dto.veterinaryRecordDto.VeterinaryRecordDto;
import com.projeto.controleanimal.model.Animal;
import com.projeto.controleanimal.model.vetRecord.VetVisits;
import com.projeto.controleanimal.model.vetRecord.VeterinaryRecord;
import com.projeto.controleanimal.repository.AnimalRepository;
import com.projeto.controleanimal.repository.VeterinaryRecordRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalField;
import java.util.Comparator;
import java.util.Objects;
import java.util.Optional;

@Service
public class VeterinaryRecordService {

    private final VeterinaryRecordRepository repo;
    private final AnimalRepository animalRepo;

    public VeterinaryRecordService(VeterinaryRecordRepository repo, AnimalRepository animalRepo) {
        this.repo = repo;
        this.animalRepo = animalRepo;
    }

    public VeterinaryRecordDto createVeterinaryRecord(Long idAnimal) { //TODO change to return DATA from VeterinaryRecord
        Animal animal = animalRepo.findById(idAnimal)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Id não existe"));

        VeterinaryRecord vetRec = new VeterinaryRecord();
        vetRec.setAnimal(animal);

        animal.setVeterinaryRecord(vetRec);

        repo.save(animal.getVeterinaryRecord());
        animalRepo.save(animal);


        return new VeterinaryRecordDto(animal.getName(), animal.getId(), animal.getVeterinaryRecord().getId());
    }

    public VetVisitReturnDto createVetVisit(VetVisitDto vetVisitDto, Long idAnimal) {
        Animal animal = animalRepo.findById(idAnimal)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Id não existe"));
        if (animal.getVeterinaryRecord() == null) {
            createVeterinaryRecord(animal.getId());
        }

        VetVisits vetVisit = new VetVisits(
                vetVisitDto.vetName() == null ? "" : vetVisitDto.vetName(),
                LocalDate.now(),
                vetVisitDto.procedure() == null ? "" : vetVisitDto.procedure(),
                vetVisitDto.notes() == null ? "" : vetVisitDto.notes(),
                vetVisitDto.weight() == null ? grabFromLastWeight(idAnimal).orElse(0.0) : vetVisitDto.weight(),
                vetVisitDto.nextVisit()
        );

        animal.getVeterinaryRecord().createVetVisit(vetVisit);
        animalRepo.save(animal);

        return new VetVisitReturnDto(vetVisit.vetName(),
                vetVisit.visitDate(),
                vetVisit.procedure(),
                vetVisit.notes(),
                vetVisit.weight(),
                vetVisit.nextVisit());

    }

    private Optional<Double> grabFromLastWeight(Long idAnimal) {
        Animal animal = animalRepo.findById(idAnimal)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Id não existe"));


        return animal.getVeterinaryRecord().getVetVisitsList().stream()
                .sorted(Comparator.comparing(VetVisits::visitDate).reversed())
                .map(VetVisits::weight)
                .filter(Objects::nonNull)
                .findFirst();
    }
}
