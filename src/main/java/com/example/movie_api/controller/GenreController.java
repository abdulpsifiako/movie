package com.example.movie_api.controller;

import com.example.movie_api.dto.ApiResponse;
import com.example.movie_api.dto.GenreDTO;
import com.example.movie_api.service.GenreService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/genre")
@Tag(name = "Genre Management", description = "CRUD operations for genre")
public class GenreController {
    @Autowired
    private GenreService genreService;

    @GetMapping
    public ResponseEntity<?> getAllGenres() {
        List<GenreDTO> genres = genreService.getAllGenres();
        return ResponseEntity.ok(new ApiResponse<>(200, "Success", genres));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getGenreById(@PathVariable Long id) {
        ApiResponse<List<GenreDTO>> response = genreService.getGenreById(id);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @PostMapping
    public ResponseEntity<?> createGenre(@RequestBody GenreDTO genreDTO) {
        ApiResponse<List<GenreDTO>> response = genreService.createGenre(genreDTO);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateGenre(@PathVariable Long id, @RequestBody GenreDTO genreDTO) {
        ApiResponse<List<GenreDTO>> response = genreService.updateGenre(id, genreDTO);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteGenre(@PathVariable Long id) {
        ApiResponse<String> response = genreService.deleteGenre(id);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
}
