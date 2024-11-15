package com.example.movie_api.service;

import com.example.movie_api.dto.ApiResponse;
import com.example.movie_api.dto.GenreDTO;
import com.example.movie_api.entity.Genre;
import com.example.movie_api.mapper.GenreMapper;
import com.example.movie_api.repository.GenreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class GenreService {

    @Autowired
    private GenreRepository genreRepository;

    public List<GenreDTO> getAllGenres() {
        return genreRepository.findAll().stream()
            .map(GenreMapper.INSTANCE::toGenreDTO)
            .collect(Collectors.toList());
    }

    public ApiResponse<List<GenreDTO>> getGenreById(Long id) {
        try {
            Optional<Genre> genre = genreRepository.findById(id);

            return genre.map(value -> {
                    List<GenreDTO> genreList = Collections.singletonList(GenreMapper.INSTANCE.toGenreDTO(value));
                    return new ApiResponse<>(200, "Success", genreList);
                })
                .orElseGet(() -> new ApiResponse<>(404, "Genre not found", null));
        } catch (Exception e) {
            return new ApiResponse<>(500, "Error fetching genre: " + e.getMessage(), null);
        }
    }

    @Transactional
    public ApiResponse<List<GenreDTO>> createGenre(GenreDTO genreDTO) {
        try {
            Genre genre = GenreMapper.INSTANCE.toGenre(genreDTO);
            Genre savedGenre = genreRepository.save(genre);

            GenreDTO savedGenreDTO = GenreMapper.INSTANCE.toGenreDTO(savedGenre);
            List<GenreDTO> genreList = new ArrayList<>();
            genreList.add(savedGenreDTO);
            return new ApiResponse<>(201, "Genre created successfully", genreList);
        } catch (Exception e) {
            return new ApiResponse<>(500, "Error creating genre: " + e.getMessage(), null);
        }
    }

    @Transactional
    public ApiResponse<List<GenreDTO>> updateGenre(Long id, GenreDTO genreDTO) {
        try {
            Optional<Genre> genreOpt = genreRepository.findById(id);
            if (genreOpt.isPresent()) {
                Genre genre = genreOpt.get();
                genre.setName(genreDTO.getName());
                Genre updatedGenre = genreRepository.save(genre);
                List<GenreDTO> genreList = Collections.singletonList(GenreMapper.INSTANCE.toGenreDTO(updatedGenre));

                return new ApiResponse<>(200, "Genre updated successfully", genreList);
            }
            return new ApiResponse<>(404, "Genre not found", null);
        } catch (Exception e) {
            return new ApiResponse<>(500, "Error updating genre: " + e.getMessage(), null);
        }
    }

    @Transactional
    public ApiResponse<String> deleteGenre(Long id) {
        try {
            if (genreRepository.existsById(id)) {
                genreRepository.deleteById(id);
                return new ApiResponse<>(200, "Genre deleted successfully", null);
            }
            return new ApiResponse<>(404, "Genre not found", null);
        } catch (Exception e) {
            return new ApiResponse<>(500, "Error deleting genre: " + e.getMessage(), null);
        }
    }
}

