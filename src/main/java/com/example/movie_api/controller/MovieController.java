package com.example.movie_api.controller;

import com.example.movie_api.dto.ApiResponse;
import com.example.movie_api.dto.MovieBody;
import com.example.movie_api.dto.MovieDTO;
import com.example.movie_api.service.MovieService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/movie")
@Tag(name = "Movie Management", description = "CRUD operations for movies")
public class MovieController {
    @Autowired
    private MovieService movieService;

    @GetMapping
    public ResponseEntity<?> getAllMovies() {
        List<MovieDTO> movies = movieService.getAllMovies();
        return ResponseEntity.ok(new ApiResponse<>(200, "Success", movies));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getMovieById(@PathVariable Long id) {
        ApiResponse<List<MovieDTO>> response = movieService.getMovieById(id);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @PostMapping
    public ResponseEntity<ApiResponse<List<MovieDTO>>> createMovie(@RequestBody MovieBody movieBody) {
        ApiResponse<List<MovieDTO>> response = movieService.createMovie(movieBody);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<List<MovieDTO>>> updateMovie(@PathVariable Long id, @RequestBody MovieBody movieBody) {
        ApiResponse<List<MovieDTO>> response = movieService.updateMovie(id, movieBody);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteMovie(@PathVariable Long id) {
        ApiResponse<String> response = movieService.deleteMovie(id);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/search")
    public ResponseEntity<?> searchMovies(@RequestParam String title) {
        ApiResponse<List<MovieDTO>> response = movieService.searchMovies(title);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
}
