package com.example.movie_api.service;

import com.example.movie_api.dto.ApiResponse;
import com.example.movie_api.dto.GenreDTO;
import com.example.movie_api.dto.MovieBody;
import com.example.movie_api.dto.MovieDTO;
import com.example.movie_api.entity.Genre;
import com.example.movie_api.entity.Movie;
import com.example.movie_api.mapper.MovieMapper;
import com.example.movie_api.repository.GenreRepository;
import com.example.movie_api.repository.MovieRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class MovieService {
    @Autowired
    private MovieRepository movieRepository;
    @Autowired
    private GenreRepository genreRepository;

    public List<MovieDTO> getAllMovies() {
        return movieRepository.findAll().stream()
            .map(MovieMapper.INSTANCE::toMovieDTO)
            .collect(Collectors.toList());
    }

    public ApiResponse<List<MovieDTO>> getMovieById(Long id) {
        try {
            Optional<Movie> movie = movieRepository.findById(id);
            return movie.map(value -> {
                List<MovieDTO> movieList = Collections.singletonList(MovieMapper.INSTANCE.toMovieDTO(value));
                return new ApiResponse<>(200, "Success", movieList);
            }).orElseGet(() -> new ApiResponse<>(404, "Movie not found", null));
        } catch (Exception e) {
            return new ApiResponse<>(500, "Error fetching movie: " + e.getMessage(), null);
        }
    }

    @Transactional
    public ApiResponse<List<MovieDTO>> createMovie(MovieBody movieBody) {
        try {
            Movie movie = new Movie();
            movie.setTitle(movieBody.getTitle());
            movie.setSummary(movieBody.getSummary());
            movie.setDirector(movieBody.getDirector());

            Set<Genre> genres = new HashSet<>();
            if (movieBody.getIdGenres() != null) {
                for (Long genreId : movieBody.getIdGenres()) {
                    genreRepository.findById(genreId).ifPresent(genres::add);
                }
            }
            movie.setGenres(genres);

            Movie savedMovie = movieRepository.save(movie);
            List<MovieDTO> movieList = Collections.singletonList(MovieMapper.INSTANCE.toMovieDTO(savedMovie));
            return new ApiResponse<>(201, "Movie created successfully", movieList);
        } catch (Exception e) {
            return new ApiResponse<>(500, "Error creating movie: " + e.getMessage(), null);
        }
    }

    @Transactional
    public ApiResponse<List<MovieDTO>> updateMovie(Long id, MovieBody movieBody) {
        try {
            Optional<Movie> movieOpt = movieRepository.findById(id);
            if (movieOpt.isPresent()) {
                Movie movie = movieOpt.get();
                movie.setTitle(movieBody.getTitle());
                movie.setSummary(movieBody.getSummary());
                movie.setDirector(movieBody.getDirector());

                // Mengatur genre jika ada
                Set<Genre> genres = new HashSet<>();
                if (movieBody.getIdGenres() != null) {
                    List<Long> genreIds = movieBody.getIdGenres();
                    genreIds.forEach(genreId ->
                        genreRepository.findById(genreId).ifPresent(genres::add)
                    );
                }
                movie.setGenres(genres);

                Movie updatedMovie = movieRepository.save(movie);

                List<MovieDTO> movieDTOList = Collections.singletonList(MovieMapper.INSTANCE.toMovieDTO(updatedMovie));
                return new ApiResponse<>(200, "Movie updated successfully", movieDTOList);
            }

            return new ApiResponse<>(404, "Movie not found", null);
        } catch (Exception e) {
            return new ApiResponse<>(500, "Error updating movie: " + e.getMessage(), null);
        }
    }


    @Transactional
    public ApiResponse<String> deleteMovie(Long id) {
        try {
            if (movieRepository.existsById(id)) {
                movieRepository.deleteById(id);
                return new ApiResponse<>(200, "Movie deleted successfully", null);
            }
            return new ApiResponse<>(404, "Movie not found", null);
        } catch (Exception e) {
            return new ApiResponse<>(500, "Error deleting movie: " + e.getMessage(), null);
        }
    }

    @Transactional
    public ApiResponse<List<MovieDTO>> searchMovies(String title) {
        try {
            if (title == null || title.trim().isEmpty()) {
                return new ApiResponse<>(400, "Title parameter cannot be empty", null);
            }

            // Mencari film berdasarkan judul
            List<Movie> movies = movieRepository.findByTitleContainingIgnoreCase(title);

            // Memetakan data film ke MovieDTO
            List<MovieDTO> movieDTOs = movies.stream()
                .map(movie -> {
                    // Memetakan genre menjadi List<GenreDTO>
                    List<GenreDTO> genreDTOs = movie.getGenres().stream()
                        .map(genre -> {
                            GenreDTO genreDTO = new GenreDTO();
                            genreDTO.setId(genre.getId()); // Set ID Genre
                            genreDTO.setName(genre.getName()); // Set Nama Genre
                            return genreDTO;
                        })
                        .collect(Collectors.toList());

                    // Membuat MovieDTO dan set data film
                    MovieDTO dto = new MovieDTO();
                    dto.setId(movie.getId());
                    dto.setTitle(movie.getTitle());
                    dto.setSummary(movie.getSummary());
                    dto.setDirector(movie.getDirector());
                    dto.setGenres(genreDTOs); // Set List<GenreDTO>

                    return dto;
                })
                .collect(Collectors.toList());

            // Mengembalikan respons API
            if (movieDTOs.isEmpty()) {
                return new ApiResponse<>(404, "No movies found with the given title", null);
            }

            return new ApiResponse<>(200, "Movies found", movieDTOs);
        } catch (Exception e) {
            return new ApiResponse<>(500, "Error occurred while searching movies: " + e.getMessage(), null);
        }
    }
}
