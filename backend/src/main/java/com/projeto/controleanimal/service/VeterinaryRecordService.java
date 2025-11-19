package com.projeto.controleanimal.service;

import com.projeto.controleanimal.dto.veterinaryRecordDto.NextVisitDto;
import com.projeto.controleanimal.model.Animal;
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

@Service
public class VeterinaryRecordService {

    private final VeterinaryRecordRepository repo;
    private final AnimalRepository animalRepo;

    public VeterinaryRecordService(VeterinaryRecordRepository repo, AnimalRepository animalRepo) {
        this.repo = repo;
        this.animalRepo = animalRepo;
    }

    public NextVisitDto createVeterinaryRecord(Long idAnimal) { //TODO change to return DATA from VeterinaryREcord
        Animal animal = animalRepo.findById(idAnimal)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Id não existe"));

        VeterinaryRecord vetRec = new VeterinaryRecord();
        vetRec.setAnimal(animal);

        animal.setVeterinaryRecord(vetRec);

        repo.save(animal.getVeterinaryRecord());
        animalRepo.save(animal);
//        System.out.println("Chegamos aqui");
        //tests
        return new NextVisitDto(animalRepo.findById(idAnimal).orElseThrow().getVeterinaryRecord().getAnimal().getName(),
                 LocalDateTime.now().getLong(ChronoField.MONTH_OF_YEAR));
    }
}
