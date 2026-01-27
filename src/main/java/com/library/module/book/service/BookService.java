package com.library.module.book.service;

import com.library.module.book.dto.BookDTO;
import com.library.module.book.request.BookSearchRequest;
import com.library.shared.payload.response.PageResponse;

import java.util.List;

public interface BookService {

    BookDTO createBook(BookDTO bookDTO);

    List<BookDTO> createBooksBulk(List<BookDTO> bookDTOList);

    BookDTO getBookById(Long id);

    BookDTO getBookByIsbn(String isbn);

    BookDTO updateBook(Long id, BookDTO bookDTO);

    void deleteBook(Long id);

    void hardDeleteBook(Long id);

    PageResponse<BookDTO> searchBooksWithFilters(BookSearchRequest bookSearchRequest);

    long getTotalActiveBooks();

    long getTotalAvailableBooks();
}
