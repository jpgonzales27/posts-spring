package com.jp.posts.controllers;

import com.jp.posts.dtos.request.PageRequest;
import com.jp.posts.dtos.response.PageResponse;
import com.jp.posts.services.PageService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

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

    @PutMapping ("/{title}")
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
}
