package com.library.service;

import com.library.payload.dto.GenreDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface GenreService {

    GenreDTO createGenre(GenreDTO genreDTO);

    List<GenreDTO> getAllGenres();
    
    GenreDTO getGenreById(Long id);
    
    GenreDTO updateGenre(Long id, GenreDTO genreDTO);
    
    void deleteGenre(Long id);
    
    void hardDeleteGenre(Long id);
    
    List<GenreDTO> getAllActiveGenresWithSubGenres();

    List<GenreDTO> getTopLevelGenres();


    Page<GenreDTO> searchGenres(String searchTerm, Pageable pageable);


    long getTotalActiveGenres();

    long getBookCountByGenre(Long genreId);


}
