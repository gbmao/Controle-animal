package com.projeto.controleanimal.controller.image;

import com.projeto.controleanimal.dto.imageDto.ImageInfoDto;
import com.projeto.controleanimal.repository.AnimalRepository;
import com.projeto.controleanimal.repository.ImageDpRepository;
import com.projeto.controleanimal.security.service.CustomUserDetails;
import com.projeto.controleanimal.service.ImageService;
import com.projeto.controleanimal.service.UserService;
import com.projeto.controleanimal.util.ApiKeyValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/images")
//@CrossOrigin(origins = "*")
public class ImageController {

    private final ImageDpRepository imageDpRepository;
    private final AnimalRepository animalRepository;
    private ImageService service;
    private final UserService userService;

    @Autowired
    public ImageController(ImageDpRepository imageDpRepository, AnimalRepository animalRepository, ImageService service, UserService userService) {
        this.imageDpRepository = imageDpRepository;
        this.animalRepository = animalRepository;
        this.service = service;
        this.userService = userService;
    }

    //TODO criado apenas para testes, remover antes do deploy
    @GetMapping("/all")
    List<ImageInfoDto> getAllImgInfo() {

        return service.getAllImgInfo();
    }


    // post automaticamente deleta a imagem antiga e faz upload da nova

    @PostMapping("/{animalId}")
    Long uploadImage(@RequestParam MultipartFile multipartImage,
                     @PathVariable Long animalId,
                     @RequestHeader("x-api-key") String key,
                     @AuthenticationPrincipal CustomUserDetails user) throws Exception {

        userService.checkAnimalId(user.getId(), animalId);
        ApiKeyValidator.check(key);

        return service.uploadImage(multipartImage, animalId);
    }

    @GetMapping(value = "/{animalId}", produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<ByteArrayResource> downloadImage(@PathVariable Long animalId,
                                                           @RequestHeader("x-api-key") String key,
                                                           @AuthenticationPrincipal CustomUserDetails user) {
        ApiKeyValidator.check(key);
        userService.checkAnimalId(user.getId(), animalId);

        return service.getImgByAnimalId(animalId);
    }

    @DeleteMapping("/{animalId}")
    void deleteImage(@PathVariable Long animalId,
                     @RequestHeader("x-api-key") String key,
                     @AuthenticationPrincipal CustomUserDetails user) {

        userService.checkAnimalId(user.getId(), animalId);
        ApiKeyValidator.check(key);

        service.deleteImg(animalId);
    }


}


