package com.library.service.impl;

import com.library.exception.GenreException;
import com.library.mapper.GenreMapper;
import com.library.modal.Genre;
import com.library.payload.dto.GenreDTO;
import com.library.repository.GenreRepository;
import com.library.service.GenreService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GenreServiceImpl implements GenreService {

    private final GenreRepository genreRepository;
    private final GenreMapper genreMapper;


    @Override
    public GenreDTO createGenre(GenreDTO genreDTO) {

        // DTO -> Entity
        Genre genre = genreMapper.toEntity(genreDTO);

        // DB'ye kaydet
        Genre savedGenre = genreRepository.save(genre);

        // Entity -> DTO
        return genreMapper.toDTO(savedGenre);
    }

    @Override
    public List<GenreDTO> getAllGenres() {
        return genreRepository.findAll()
                .stream()
                .map(genreMapper::toDTO)
                .toList();
    }

    @Override
    public GenreDTO getGenreById(Long id) {
        Genre genre = genreRepository.findById(id)
                .orElseThrow(() -> new GenreException("Genre not found with id: " + id));

        return genreMapper.toDTO(genre);
    }


    @Override
    public GenreDTO updateGenre(Long id, GenreDTO genreDTO) {
        Genre existingGenre = genreRepository.findById(id)
                .orElseThrow(() -> new GenreException("Genre not found with id: " + id));

        genreMapper.updateEntityFromDTO(genreDTO, existingGenre);

        Genre updatedGenre = genreRepository.save(existingGenre);

        return genreMapper.toDTO(updatedGenre);
    }

    @Override
    public void deleteGenre(Long id) {
        Genre existingGenre = genreRepository.findById(id)
                .orElseThrow(() -> new GenreException("Genre not found with id: " + id));

        existingGenre.setActive(false);
        genreRepository.save(existingGenre);
    }

    @Override
    public void hardDeleteGenre(Long id) {
        Genre existingGenre = genreRepository.findById(id)
                .orElseThrow(() -> new GenreException("Genre not found with id: " + id));

        genreRepository.delete(existingGenre);
    }

    @Override
    public List<GenreDTO> getAllActiveGenresWithSubGenres() {
        return genreRepository
                .findByParentGenreIsNullAndActiveTrueOrderByDisplayOrderAsc()
                .stream()
                .map(genreMapper::toDTO)
                .toList();
    }

    @Override
    public List<GenreDTO> getTopLevelGenres() {
        return genreRepository
                .findByParentGenreIsNullAndActiveTrueOrderByDisplayOrderAsc()
                .stream()
                .map(genreMapper::toDTO)
                .toList();
    }

    @Override
    public Page<GenreDTO> searchGenres(String searchTerm, Pageable pageable) {
        return null;
    }

    @Override
    public long getTotalActiveGenres() {
        return genreRepository.countByActiveTrue();
    }

    @Override
    public long getBookCountByGenre(Long genreId) {
        return 0;
    }
}
