package com.projeto.controleanimal.controller.image;

import com.projeto.controleanimal.model.Animal;
import com.projeto.controleanimal.repository.AnimalRepository;
import com.projeto.controleanimal.repository.ImageDpRepository;
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

@RestController
@RequestMapping("/images")
@CrossOrigin(origins = "*")
public class ImageController {

    private final ImageDpRepository imageDpRepository;
    private final AnimalRepository animalRepository;


    @Autowired
    public ImageController(ImageDpRepository imageDpRepository, AnimalRepository animalRepository) {
        this.imageDpRepository = imageDpRepository;
        this.animalRepository = animalRepository;
    }

    //TODO exigir senha para postar imagem
    @PostMapping("/{animalId}")
    Long uploadImage(@RequestParam MultipartFile multipartImage,
                     @PathVariable Long animalId) throws Exception {

        //aqui instancio o animal baseado no id que recebo
        Animal animal = animalRepository.findById(animalId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Animal nao encontrado"));



        Image dbImage = new Image();
        dbImage.setName(multipartImage.getOriginalFilename()); // nao sei bem como funciona existe a opcao de pegar com getName
        dbImage.setContent(multipartImage.getBytes());
        dbImage.setAnimal(animal); // aqui estou prendendo a imagem ao animal instanciado, aparentemente o cascade faz o restante

//        Image savedImage = imageDpRepository.save(dbImage);

        animal.setImage(dbImage); // setando a imagem ao animal
        System.out.println("AQUIIIIIIIIIIIIIIIIIIIIIIIII");
        System.out.println("AQUIIIIIIIIIIIIIIIIIIIIIIIII");
        System.out.println("AQUIIIIIIIIIIIIIIIIIIIIIIIII");
//        System.out.println("CONTENT CLASS = " + dbImage.getAnimal().getId() + " :" + dbImage.getContent().getClass() + "  :" + dbImage.getName());
        System.out.println("CONTENT CLASS = " + dbImage.getContent().getClass());

        System.out.println("ANIMAL ID  = " + animal.getId());
        System.out.println("IMAGE NAME = " + dbImage.getName());
        System.out.println("CONTENT LENGTH = " + dbImage.getContent().length);
        System.out.println("CONTENT CLASS = " + dbImage.getContent().getClass());


//        Image savedImage = imageDpRepository.save(dbImage);
        Animal saved = animalRepository.save(animal); // salvando o animal ao banco de dados

        return saved.getImage().getId();
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


