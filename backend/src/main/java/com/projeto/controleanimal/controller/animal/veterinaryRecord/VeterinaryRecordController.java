package com.projeto.controleanimal.controller.animal.veterinaryRecord;

import com.projeto.controleanimal.dto.veterinaryRecordDto.VetVisitDto;
import com.projeto.controleanimal.dto.veterinaryRecordDto.VetVisitReturnDto;
import com.projeto.controleanimal.dto.veterinaryRecordDto.VeterinaryRecordDto;
import com.projeto.controleanimal.security.service.CustomUserDetails;
import com.projeto.controleanimal.service.UserService;
import com.projeto.controleanimal.service.VeterinaryRecordService;
import com.projeto.controleanimal.util.ApiKeyValidator;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/{idAnimal}/veterinary-record")
public class VeterinaryRecordController {

    private final VeterinaryRecordService veterinaryRecordService;
    private final UserService userService;

    public VeterinaryRecordController(VeterinaryRecordService veterinaryRecordService, UserService userService) {
        this.veterinaryRecordService = veterinaryRecordService;
        this.userService = userService;
    }

    @PostMapping()
    VeterinaryRecordDto createVeterinaryRecord(@PathVariable("idAnimal") Long idAnimal,
                                               @RequestHeader("x-api-key") String key,
                                               @AuthenticationPrincipal CustomUserDetails user) {
        userService.checkAnimalId(user.getId(), idAnimal);
        ApiKeyValidator.check(key);
        return veterinaryRecordService.createVeterinaryRecord(idAnimal);
    }

    @PostMapping("/vet-visit")
    VetVisitReturnDto createVetVisit(@PathVariable("idAnimal") Long idAnimal,
                                     @RequestBody VetVisitDto vetVisitDto,
                                     @RequestHeader("x-api-key") String key,
                                     @AuthenticationPrincipal CustomUserDetails user) {
        userService.checkAnimalId(user.getId(), idAnimal);
        ApiKeyValidator.check(key);
        return veterinaryRecordService.createVetVisit(vetVisitDto, idAnimal);
    }

    @GetMapping("/vet-visit/all")
    List<VetVisitReturnDto> getAllVetVisit(@PathVariable("idAnimal") Long idAnimal,
                                           @AuthenticationPrincipal CustomUserDetails user) {
        userService.checkAnimalId(user.getId(), idAnimal);
        return veterinaryRecordService.getAllVetVisits(idAnimal);
    }
}
