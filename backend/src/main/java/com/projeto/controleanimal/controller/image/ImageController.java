package com.projeto.controleanimal.controller.image;

import com.projeto.controleanimal.dto.imageDto.ImageInfoDto;
import com.projeto.controleanimal.model.Animal;
import com.projeto.controleanimal.repository.AnimalRepository;
import com.projeto.controleanimal.repository.ImageDpRepository;
import com.projeto.controleanimal.service.ImageService;
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
@CrossOrigin(origins = "*")
public class ImageController {

    private final ImageDpRepository imageDpRepository;
    private final AnimalRepository animalRepository;
    private ImageService service;

    private final String secret = System.getenv("API_SECRET");


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

    //TODO exigir senha para postar imagem
    @PostMapping("/{animalId}")
    Long uploadImage(@RequestParam MultipartFile multipartImage,
                     @PathVariable Long animalId,
                     @RequestHeader("x-api-key") String key) throws Exception {

        if (!secret.equals(key)) { //TODO colocar isso em um metodo para ser chamado e evitar repeticao
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Não autorizado");
        }

        return service.uploadImage(multipartImage, animalId);
    }

    @GetMapping(value = "/{id}", produces = MediaType.IMAGE_JPEG_VALUE)
//    Resource downloadImage(@PathVariable Long id) {
//        byte[] image = imageDpRepository.findById(id)
//                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND))
//                .getContent();
//
//        return new ByteArrayResource(image);
//    }
    public ResponseEntity<ByteArrayResource> downloadImage(@PathVariable Long id) {

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
}


