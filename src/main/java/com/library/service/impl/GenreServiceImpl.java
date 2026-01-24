package com.library.service.impl;

import com.library.modal.Genre;
import com.library.payload.dto.GenreDTO;
import com.library.repository.GenreRepository;
import com.library.service.GenreService;
import org.springframework.stereotype.Service;

@Service
public class GenreServiceImpl implements GenreService {

    private final GenreRepository genreRepository;

    public GenreServiceImpl(GenreRepository genreRepository) {
        this.genreRepository = genreRepository;
    }

    @Override
    public GenreDTO createGenre(GenreDTO genreDTO) {

        // DTO -> Entity
        Genre genre = Genre.builder()
                .name(genreDTO.getName())
                .code(genreDTO.getCode())
                .description(genreDTO.getDescription())
                .displayOrder(genreDTO.getDisplayOrder())
                .active(true)
                .build();

        // Parent genre varsa set et
        if (genreDTO.getParentGenreId() != null) {
            Genre parentGenre = genreRepository.findById(genreDTO.getParentGenreId())
                    .orElseThrow(() -> new RuntimeException("Parent genre not found"));
            genre.setParentGenre(parentGenre);
        }

        // DB'ye kaydet
        Genre savedGenre = genreRepository.save(genre);

        // Parent kontrolü (NPE önlemek için)
        Genre parent = savedGenre.getParentGenre();

        // Entity -> DTO
        return GenreDTO.builder()
                .id(savedGenre.getId())
                .name(savedGenre.getName())
                .code(savedGenre.getCode())
                .description(savedGenre.getDescription())
                .displayOrder(savedGenre.getDisplayOrder())
                .active(savedGenre.getActive())
                .parentGenreId(parent != null ? parent.getId() : null)
                .parentGenreName(parent != null ? parent.getName() : null)
                .countOfBooks(null)
                .createdAt(savedGenre.getCreatedAt())
                .updatedAt(savedGenre.getUpdatedAt())
                .build();
    }
}
