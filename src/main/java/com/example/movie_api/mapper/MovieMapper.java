package com.example.movie_api.mapper;

import com.example.movie_api.dto.MovieDTO;
import com.example.movie_api.entity.Movie;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface MovieMapper {
    MovieMapper INSTANCE = Mappers.getMapper(MovieMapper.class);
    MovieDTO toMovieDTO(Movie movie);
    Movie toMovie(MovieDTO movieDTO);
}
