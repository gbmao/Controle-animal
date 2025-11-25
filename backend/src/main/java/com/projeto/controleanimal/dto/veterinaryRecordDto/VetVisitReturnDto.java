package com.projeto.controleanimal.dto.veterinaryRecordDto;

import java.time.LocalDate;

public record VetVisitReturnDto(String vetName,
                                LocalDate visitDate,
                                String procedure,
                                String notes,
                                Double weight,
                                LocalDate nextVisit) {
}
