package com.library.mapper;

import com.library.exception.BookException;
import com.library.modal.Book;
import com.library.modal.Genre;
import com.library.payload.dto.BookDTO;
import com.library.repository.GenreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BookMapper {

    private final GenreRepository genreRepository;

    public BookDTO toDTO(Book book) {

        if (book == null) {
            return null;
        }

        BookDTO bookDTO = BookDTO.builder()
                .id(book.getId())
                .title(book.getTitle())
                .author(book.getAuthor())
                .isbn(book.getIsbn())
                .genreId(book.getGenre().getId())
                .genreName(book.getGenre().getName())
                .genreCode(book.getGenre().getCode())
                .publisher(book.getPublisher())
                .publishedDate(book.getPublishedDate())
                .language(book.getLanguage())
                .pages(book.getPages())
                .description(book.getDescription())
                .totalCopies(book.getTotalCopies())
                .availableCopies(book.getAvailableCopies())
                .price(book.getPrice())
                .coverImageUrl(book.getCoverImageUrl())
                .active(book.getActive())
                .createdAt(book.getCreatedAt())
                .updatedAt(book.getUpdatedAt())
                .build();

        return bookDTO;
    }


    public Book toEntity(BookDTO bookDTO) {

        if (bookDTO == null) {
            return null;
        }

        Book book = new Book();
        mapCommonFields(bookDTO, book);

        return book;
    }

    public BookDTO updateEntityFromDTO(BookDTO bookDTO, Book existingBook) {

        if (bookDTO == null || existingBook == null) {
            return null;
        }

        mapCommonFields(bookDTO, existingBook);
        return toDTO(existingBook);
    }


    private void mapCommonFields(BookDTO bookDTO, Book book) {

        book.setId(bookDTO.getId());
        book.setTitle(bookDTO.getTitle());
        book.setAuthor(bookDTO.getAuthor());

        // Map genre -fetch from database using genreId
        mapGenre(bookDTO.getGenreId(), book);

        book.setPublisher(bookDTO.getPublisher());
        book.setPublishedDate(bookDTO.getPublishedDate());
        book.setLanguage(bookDTO.getLanguage());
        book.setPages(bookDTO.getPages());
        book.setDescription(bookDTO.getDescription());
        book.setTotalCopies(bookDTO.getTotalCopies());
        book.setAvailableCopies(bookDTO.getAvailableCopies());
        book.setPrice(bookDTO.getPrice());
        book.setCoverImageUrl(bookDTO.getCoverImageUrl());
        book.setActive(bookDTO.getActive() != null ? bookDTO.getActive() : true);
    }

    private void mapGenre(Long genreId, Book book) {
        if (genreId == null) {
            return;
        }
        Genre genre = genreRepository.findById(genreId)
                .orElseThrow(() -> new BookException("Genre id not found"));

        book.setGenre(genre);
    }

}
