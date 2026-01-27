package com.library.module.genre.mapper;

import com.library.module.genre.model.Genre;
import com.library.module.genre.dto.GenreDTO;
import com.library.module.genre.repository.GenreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GenreMapper {

    private final GenreRepository genreRepository;

    public GenreDTO toDTO(Genre genre) {
        if (genre == null) {
            return null;
        }

        GenreDTO genreDTO = GenreDTO.builder()
                .id(genre.getId())
                .name(genre.getName())
                .code(genre.getCode())
                .description(genre.getDescription())
                .displayOrder(genre.getDisplayOrder())
                .active(genre.getActive())
                .countOfBooks(null)
                .createdAt(genre.getCreatedAt())
                .updatedAt(genre.getUpdatedAt())
                .build();

        if (genre.getParentGenre() != null) {
            genreDTO.setParentGenreId(genre.getParentGenre().getId());
            genreDTO.setParentGenreName(genre.getParentGenre().getName());
        }

        if (genre.getSubGenres() != null && !genre.getSubGenres().isEmpty()) {
            genre.setSubGenres(genre.getSubGenres().stream()
                    .filter(subGenre -> subGenre.getActive())
                    .toList());
        }

        return genreDTO;
    }

    public Genre toEntity(GenreDTO genreDTO) {
        if (genreDTO == null) {
            return null;
        }

        Genre genre = Genre.builder()
                .name(genreDTO.getName())
                .code(genreDTO.getCode())
                .description(genreDTO.getDescription())
                .displayOrder(genreDTO.getDisplayOrder())
                .active(true)
                .build();

        if (genreDTO.getParentGenreId() != null) {
            genreRepository.findById(genreDTO.getParentGenreId())
                    .ifPresent(genre::setParentGenre);
        }

        return genre;
    }

    public void updateEntityFromDTO(GenreDTO genreDTO, Genre genre) {
        if (genreDTO == null || genre == null) {
            return;
        }

        genre.setName(genreDTO.getName());
        genre.setCode(genreDTO.getCode());
        genre.setDescription(genreDTO.getDescription());
        genre.setDisplayOrder(genreDTO.getDisplayOrder() != null ? genreDTO.getDisplayOrder() : 0);
        genre.setActive(genreDTO.getActive());

        if (genreDTO.getParentGenreId() != null) {
            genreRepository.findById(genreDTO.getParentGenreId())
                    .ifPresent(genre::setParentGenre);
        }
    }
}
