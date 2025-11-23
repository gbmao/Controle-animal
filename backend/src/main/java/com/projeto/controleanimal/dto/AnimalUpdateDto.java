package com.projeto.controleanimal.dto;

import java.time.LocalDate;

public record AnimalUpdateDto(String name, LocalDate birthDate) {
}
