package com.projeto.controleanimal.dto;

import org.springframework.web.multipart.MultipartFile;

public record AnimalWithImgDto(long id, String name, int age, String type, MultipartFile multipartImage) {
}
