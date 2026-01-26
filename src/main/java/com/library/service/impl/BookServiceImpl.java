package com.library.service.impl;

import com.library.exception.BookException;
import com.library.mapper.BookMapper;
import com.library.modal.Book;
import com.library.payload.dto.BookDTO;
import com.library.payload.request.BookSearchRequest;
import com.library.payload.response.PageResponse;
import com.library.repository.BookRepository;
import com.library.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final BookMapper bookMapper;

    @Override
    public BookDTO createBook(BookDTO bookDTO) {
        if (bookRepository.existsByIsbn(bookDTO.getIsbn())) {
            throw new BookException("Book with ISBN already exists: " + bookDTO.getIsbn());
        }

        Book book = bookMapper.toEntity(bookDTO);

        // total and available copies should be same when creating a new book
        book.isAvailableCopiesValid();

        Book savedBook = bookRepository.save(book);
        return bookMapper.toDTO(savedBook);
    }

    @Override
    public List<BookDTO> createBooksBulk(List<BookDTO> bookDTOList) {

        List<BookDTO> createdBooks = new ArrayList<>();
        for (BookDTO bookDTO : bookDTOList) {
            BookDTO book = createBook(bookDTO);
            createdBooks.add(book);
        }
        return createdBooks;
    }

    @Override
    public BookDTO getBookById(Long id) {
        Book existingBook = bookRepository.findById(id)
                .orElseThrow(() -> new BookException("Book with ID " + id + " not found"));

        return bookMapper.toDTO(existingBook);
    }

    @Override
    public BookDTO getBookByIsbn(String isbn) {
        Book existingBookIsbn = bookRepository.findByIsbn(isbn)
                .orElseThrow(() -> new BookException("Book with ISBN " + isbn + " not found"));

        return bookMapper.toDTO(existingBookIsbn);
    }

    @Override
    public BookDTO updateBook(Long id, BookDTO bookDTO) {
        Book existingBook = bookRepository.findById(id)
                .orElseThrow(() -> new BookException("Book with ID " + id + " not found"));

        bookMapper.updateEntityFromDTO(bookDTO, existingBook);

        existingBook.isAvailableCopiesValid();

        bookRepository.save(existingBook);

        return bookMapper.toDTO(existingBook);
    }

    @Override
    public void deleteBook(Long id) {
        Book existingBook = bookRepository.findById(id)
                .orElseThrow(() -> new BookException("Book with ID " + id + " not found"));

        existingBook.setActive(false);

        bookRepository.save(existingBook);
    }

    @Override
    public void hardDeleteBook(Long id) {

        Book existingBook = bookRepository.findById(id)
                .orElseThrow(() -> new BookException("Book with ID " + id + " not found"));

        bookRepository.delete(existingBook);
    }

    @Override
    public PageResponse<BookDTO> searchBooksWithFilters(BookSearchRequest bookSearchRequest) {

       Pageable pageable = createPageable(bookSearchRequest.getPage(),
               bookSearchRequest.getSize(),
               bookSearchRequest.getSortBy(),
               bookSearchRequest.getSortDirection());


        Page<Book> books = bookRepository.searchBooksWithFilters(
                bookSearchRequest.getSearchTerm(),
                bookSearchRequest.getGenreId(),
                bookSearchRequest.getAvailableOnly(),
                pageable
        );

       return convertToPageResponse(books);
     }

    @Override
    public long getTotalActiveBooks() {
        return bookRepository.countByActiveTrue();
    }

    @Override
    public long getTotalAvailableBooks() {
        return bookRepository.countAvailableBooks();
    }

    private Pageable createPageable(int page, int size, String sortBy, String sortDirection) {
        size = Math.min(size, 10);
        size = Math.max(size, 1);

        Sort sort = sortDirection.equalsIgnoreCase("ASC")
                ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        return PageRequest.of(page, size, sort);

    }

    private PageResponse<BookDTO> convertToPageResponse(Page<Book> books) {
        List<BookDTO> bookDTOs = books.getContent()
                .stream()
                .map(bookMapper::toDTO)
                .toList();

        return new PageResponse<>(bookDTOs,
                books.getNumber(),
                books.getSize(),
                books.getTotalElements(),
                books.getTotalPages(),
                books.isLast(),
                books.isFirst(),
                books.isEmpty());
    }
}
