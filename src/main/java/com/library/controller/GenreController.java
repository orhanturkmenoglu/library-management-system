package com.library.controller;

import com.library.payload.dto.GenreDTO;
import com.library.service.GenreService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/genres")
public class GenreController {
    private final GenreService genreService;

    public GenreController(GenreService genreService) {
        this.genreService = genreService;
    }

    @PostMapping
    public ResponseEntity<GenreDTO> createGenre(@RequestBody GenreDTO genreDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(genreService.createGenre(genreDTO));
    }

    @GetMapping
    public ResponseEntity<List<GenreDTO>> getAllGenres() {
        return ResponseEntity.status(HttpStatus.OK)
                .body(genreService.getAllGenres());
    }

    @GetMapping("/{id}")
    public ResponseEntity<GenreDTO> getGenreById(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(genreService.getGenreById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<GenreDTO> updateGenre(@PathVariable Long id,
                                                @RequestBody GenreDTO genreDTO) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(genreService.updateGenre(id, genreDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGenre(@PathVariable Long id) {
        genreService.deleteGenre(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @DeleteMapping("/{id}/hard")
    public ResponseEntity<Void> hardDeleteGenre(@PathVariable Long id) {
        genreService.hardDeleteGenre(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }


    @GetMapping("/top-level")
    public ResponseEntity<List<GenreDTO>> getTopLevelGenres() {
        return ResponseEntity.status(HttpStatus.OK)
                .body(genreService.getTopLevelGenres());
    }

    @GetMapping("/{id}/book-count")
    public ResponseEntity<?> getBookCountByGenre(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(genreService.getBookCountByGenre(id));
    }


}
