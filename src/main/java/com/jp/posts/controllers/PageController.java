package com.jp.posts.controllers;

import com.jp.posts.dtos.request.PageRequest;
import com.jp.posts.dtos.request.PostRequest;
import com.jp.posts.dtos.response.PageResponse;
import com.jp.posts.services.PageService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/page")
@AllArgsConstructor
public class PageController {

    private final PageService pageService;

    @GetMapping("/{title}")
    public ResponseEntity<PageResponse> getPage(@PathVariable String title) {
        return ResponseEntity.ok(this.pageService.readByTitle(title));
    }

    @PostMapping
    public ResponseEntity<PageResponse> postPage(@RequestBody PageRequest pageRequest, HttpServletRequest request) {
        pageRequest.setTitle(normalizeTitle(pageRequest.getTitle()));

        final var pageCreated = pageService.create(pageRequest);

        String baseUrl = request.getRequestURL().toString();
        URI newLocation = URI.create(baseUrl + "/" + pageCreated.getTitle());

        return ResponseEntity.created(newLocation).build();
    }

    @PutMapping("/{title}")
    public ResponseEntity<PageResponse> updatePage(@PathVariable String title, @RequestBody PageRequest request) {
        return ResponseEntity.ok(this.pageService.update(request, title));
    }

    @DeleteMapping("/{title}")
    public ResponseEntity<Void> deletePage(@PathVariable String title) {
        this.pageService.delete(title);
        return ResponseEntity.noContent().build();
    }

    private String normalizeTitle(String title) {
        if (title.contains(" ")) {
            return title.replaceAll(" ", "-");
        } else {
            return title;
        }
    }

    @PostMapping("/{title}/posts")
    public ResponseEntity<PageResponse> postPage(@RequestBody PostRequest request, @PathVariable String title) {
        return ResponseEntity.ok(this.pageService.createPost(request, title));
    }

    @DeleteMapping("/{title}/post/{idPost}")
    public ResponseEntity<Void> deletePage(@PathVariable String title, @PathVariable Long idPost) {
        this.pageService.deletePost(idPost, title);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/img/upload")
    public ResponseEntity<String> upload(@RequestParam(value = "file") MultipartFile file) {

        try {
            // Ruta relativa al proyecto (puedes ajustarla según tus necesidades).
            String baseDir = System.getProperty("user.dir") + File.separator + "src" + File.separator +
                    "main" + File.separator + "resources" + File.separator + "static" + File.separator + "img";

            Path path = Paths.get(baseDir);

            // Crear el directorio si no existe.
            if (!Files.exists(path)) {
                Files.createDirectories(path); // createDirectories para crear múltiples directorios si es necesario.
            }

            // Nombre completo del archivo.
            String fullName = path.toString() + File.separator + file.getOriginalFilename();

            // Guardar el archivo.
            file.transferTo(new File(fullName));

            return ResponseEntity.ok("Upload success on: " + fullName);

        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Cannot upload image");
        }
    }
}
