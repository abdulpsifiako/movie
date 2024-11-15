package com.example.movie_api.mapper;

import com.example.movie_api.dto.GenreDTO;
import com.example.movie_api.entity.Genre;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface GenreMapper {
    GenreMapper INSTANCE = Mappers.getMapper(GenreMapper.class);

    GenreDTO toGenreDTO(Genre genre);
    Genre toGenre(GenreDTO genreDTO);
}
