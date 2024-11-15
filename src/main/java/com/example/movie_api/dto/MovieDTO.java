package com.example.movie_api.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class MovieDTO {
    private Long id;
    private String title;
    private String summary;
    private String director;
    private List<GenreDTO> genres;
}
