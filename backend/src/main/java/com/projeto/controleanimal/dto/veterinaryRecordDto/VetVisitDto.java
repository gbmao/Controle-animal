package com.projeto.controleanimal.dto.veterinaryRecordDto;

import java.time.LocalDate;

public record VetVisitDto(String vetName,
                          String procedure,
                          String notes,
                          Double weight,
                          LocalDate nextVisit) {
}
