package com.library.repository;

import com.library.modal.Genre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GenreRepository extends JpaRepository<Genre,Long> {

    List<Genre> findByActiveTrueOrderByDisplayOrderAsc();

    List<Genre> findByParentGenreIsNullAndActiveTrueOrderByDisplayOrderAsc();

    List<Genre> findByParentGenreIdAndActiveTrueOrderByDisplayOrderAsc(Long parentGenreId);

    long countByActiveTrue();

    /*@Query("""
            SELECT COUNT(b) FROM book b WHERE  B.genre.id = :genreId
        """)
    long countBooksByGenre(@Param("genreId") Long genreId);
    */

}
