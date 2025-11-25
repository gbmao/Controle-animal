package com.projeto.controleanimal.model.vetRecord;

import jakarta.persistence.Embeddable;

import java.time.LocalDate;
@Embeddable
public record VetVisits(String vetName,
                        LocalDate visitDate,
                        String procedure,
                        String notes,
                        Double weight,
                        LocalDate nextVisit) {
}
