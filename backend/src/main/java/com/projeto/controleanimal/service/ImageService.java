package com.projeto.controleanimal.service;

import com.projeto.controleanimal.dto.imageDto.ImageInfoDto;
import com.projeto.controleanimal.model.Animal;
import com.projeto.controleanimal.model.Image;
import com.projeto.controleanimal.repository.AnimalRepository;
import com.projeto.controleanimal.repository.ImageDpRepository;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;
import java.util.Optional;

@Service
public class ImageService {
    private final ImageDpRepository imageDpRepository;
    private final AnimalRepository animalRepository;

    public ImageService(ImageDpRepository imageDpRepository, AnimalRepository animalRepository) {
        this.imageDpRepository = imageDpRepository;
        this.animalRepository = animalRepository;
    }

    public Long uploadImage(MultipartFile multipartFile, Long animalId) throws Exception {


        Animal animal = animalRepository.findById(animalId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Animal nao encontrado"));


        Image dbImage = new Image();
        dbImage.setName(multipartFile.getOriginalFilename()); // nao sei bem como funciona existe a opcao de pegar com getName
        dbImage.setContent(multipartFile.getBytes());
        dbImage.setAnimal(animal);

        animal.setImage(dbImage);

        Animal saved = animalRepository.save(animal); // salvando o animal ao banco de dados

        return saved.getImage().getId();
    }

    public List<ImageInfoDto> getAllImgInfo() {

        return imageDpRepository.findAll().stream()
                .map(img -> new ImageInfoDto(
                        img.getAnimal().getId(),
                        img.getAnimal().getName(),
                        img.getId()))
                .toList();
    }

    //toda a logica de instanciar o animal aqui me parece fora da responsabilidade do ImageService
    public void deleteImg(Long animalId) {
        Animal animal = animalRepository.findById(animalId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Nao existe ese animal"));

        animal.setImage(null);
        animalRepository.save(animal);
    }

    public ResponseEntity<ByteArrayResource> getImgByAnimalId(Long animalId) {

        Animal animal = findAnimalOrThrow(animalId);

        var imageBytes = Optional.ofNullable(animal.getImage())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Animal não possui imagem!"))
                .getContent();

        ByteArrayResource resource = new ByteArrayResource(imageBytes);

        return ResponseEntity
                .ok()
                .contentLength(imageBytes.length)
                .contentType(MediaType.IMAGE_JPEG)
                .body(resource);
    }

    private Animal findAnimalOrThrow(Long animalId) {
        return animalRepository.findById(animalId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Animal não encontrado com o id: " + animalId)
        );
    }

}
