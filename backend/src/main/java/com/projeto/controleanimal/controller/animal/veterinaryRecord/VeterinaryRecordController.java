package com.projeto.controleanimal.controller.animal.veterinaryRecord;

import com.projeto.controleanimal.dto.veterinaryRecordDto.NextVisitDto;
import com.projeto.controleanimal.service.AnimalService;
import com.projeto.controleanimal.service.VeterinaryRecordService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/{idAnimal}/veterinary-record")
public class VeterinaryRecordController {

    private final VeterinaryRecordService veterinaryRecordService;

    public VeterinaryRecordController(VeterinaryRecordService veterinaryRecordService) {
        this.veterinaryRecordService = veterinaryRecordService;
    }

    @PostMapping()
    NextVisitDto createVeterinaryRecord(@PathVariable("idAnimal") Long idAnimal) {

        return veterinaryRecordService.createVeterinaryRecord(idAnimal);
    }
}
