package br.com.biblioteca.service;

import br.com.biblioteca.model.Book;
import br.com.biblioteca.repository.BookRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class BookService {

    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public List<Book> findAll() {
        return bookRepository.findAll();
    }

    public Book findById(String id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Livro não encontrado: " + id));
    }

    public Book save(Book book) {
        // Na criação, disponível = total
        if (book.getId() == null) {
            book.setAvailableQuantity(book.getTotalQuantity());
        }
        return bookRepository.save(book);
    }

    public Book update(Book book) {
        return bookRepository.save(book);
    }

    public void deleteById(String id) {
        bookRepository.deleteById(id);
    }

    public List<Book> search(String title) {
        if (title == null || title.isBlank()) return findAll();
        return bookRepository.findByTitleContainingIgnoreCase(title);
    }
}
