package com.projeto.controleanimal.controller.animal.veterinaryRecord;

import com.projeto.controleanimal.dto.veterinaryRecordDto.NextVisitDto;
import com.projeto.controleanimal.dto.veterinaryRecordDto.VetVisitDto;
import com.projeto.controleanimal.dto.veterinaryRecordDto.VetVisitReturnDto;
import com.projeto.controleanimal.dto.veterinaryRecordDto.VeterinaryRecordDto;
import com.projeto.controleanimal.model.vetRecord.VetVisits;
import com.projeto.controleanimal.service.AnimalService;
import com.projeto.controleanimal.service.VeterinaryRecordService;
import com.projeto.controleanimal.util.ApiKeyValidator;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/{idAnimal}/veterinary-record")
public class VeterinaryRecordController {

    private final VeterinaryRecordService veterinaryRecordService;

    public VeterinaryRecordController(VeterinaryRecordService veterinaryRecordService) {
        this.veterinaryRecordService = veterinaryRecordService;
    }

    @PostMapping()
    VeterinaryRecordDto createVeterinaryRecord(@PathVariable("idAnimal") Long idAnimal,
                                               @RequestHeader("x-api-key")String key) {
        ApiKeyValidator.check(key);
        return veterinaryRecordService.createVeterinaryRecord(idAnimal);
    }

    @PostMapping("/vet-visit")
    VetVisitReturnDto createVetVisit(@PathVariable("idAnimal") Long idAnimal,
                                     @RequestBody VetVisitDto vetVisitDto,
                                     @RequestHeader("x-api-key")String key) {

        ApiKeyValidator.check(key);
        return veterinaryRecordService.createVetVisit(vetVisitDto, idAnimal);
    }
}
