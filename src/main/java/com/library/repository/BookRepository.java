package com.library.repository;

import com.library.modal.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    Optional<Book> findByIsbn(String isbn);

    boolean existsByIsbn(String isbn);


    @Query("""
                SELECT b FROM Book b where :searchTerm IS NULL OR
                        lower(b.title) like lower(concat('%',:searchTerm,'%')) OR 
                        lower(b.author) like lower(concat('%',:searchTerm,'%')) OR
                        lower(b.isbn)  like lower(concat('%',:searchTerm,'%')) OR 
                        (:genreId IS NULL OR b.genre.id=:genreId) AND
                        (:availableOnly = false OR b.availableCopies > 0) AND 
                        b.active = true
            """)
    Page<Book> searchBooksWithFilters(
            @Param("searchTerm") String searchTerm,
            @Param("genreId") Long genreId,
            @Param("availableOnly") boolean availableOnly,
            Pageable pageable
    );

    long countByActiveTrue();

    @Query("""
            SELECT COUNT(b) FROM Book b WHERE b.availableCopies>0 and b.active = true
            """)
    long countAvailableBooks();
}
