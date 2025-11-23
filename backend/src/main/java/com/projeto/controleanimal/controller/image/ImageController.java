package com.projeto.controleanimal.controller.image;

import com.projeto.controleanimal.dto.imageDto.ImageInfoDto;
import com.projeto.controleanimal.model.Animal;
import com.projeto.controleanimal.repository.AnimalRepository;
import com.projeto.controleanimal.repository.ImageDpRepository;
import com.projeto.controleanimal.service.ImageService;
import com.projeto.controleanimal.util.ApiKeyValidator;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.projeto.controleanimal.model.Image;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/images")
//@CrossOrigin(origins = "*")
public class ImageController {

    private final ImageDpRepository imageDpRepository;
    private final AnimalRepository animalRepository;
    private ImageService service;




    @Autowired
    public ImageController(ImageDpRepository imageDpRepository, AnimalRepository animalRepository, ImageService service) {
        this.imageDpRepository = imageDpRepository;
        this.animalRepository = animalRepository;
        this.service = service;
    }

    @GetMapping("/all")
    List<ImageInfoDto> getAllImgInfo() {

        return service.getAllImgInfo();
    }



    // post automaticamente deleta a imagem antiga e faz upload da nova

    @PostMapping("/{animalId}")
    Long uploadImage(@RequestParam MultipartFile multipartImage,
                     @PathVariable Long animalId,
                     @RequestHeader("x-api-key") String key) throws Exception {

        ApiKeyValidator.check(key);

        return service.uploadImage(multipartImage, animalId);
    }

    @GetMapping(value = "/{id}", produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<ByteArrayResource> downloadImage(@PathVariable Long id) {
        //TODO mover tudo isso para service
        byte[] imageBytes = imageDpRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND))
                .getContent();

        ByteArrayResource resource = new ByteArrayResource(imageBytes);

        return  ResponseEntity
                .ok()
                .contentLength(imageBytes.length)
                .contentType(MediaType.IMAGE_JPEG)
                .body(resource);
    }

    @DeleteMapping("/{animalId}")
    void deleteImage(@PathVariable Long animalId,
                     @RequestHeader("x-api-key")String key) {
        ApiKeyValidator.check(key);

         service.deleteImg(animalId);
    }


}


