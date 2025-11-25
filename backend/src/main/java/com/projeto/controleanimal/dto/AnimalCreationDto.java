package com.projeto.controleanimal.dto;

import java.time.LocalDate;

public record AnimalCreationDto(long id, String name, LocalDate birthDate, String type) {
}
