package br.com.biblioteca.service;

import br.com.biblioteca.model.*;
import br.com.biblioteca.repository.LoanRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;

@Service
public class LoanService {

    private final LoanRepository loanRepository;
    private final BookService bookService;
    private final ReaderService readerService;

    public LoanService(LoanRepository loanRepository,
                       BookService bookService,
                       ReaderService readerService) {
        this.loanRepository = loanRepository;
        this.bookService = bookService;
        this.readerService = readerService;
    }

    public List<Loan> findAll() {
        return loanRepository.findAll();
    }

    public List<Loan> findActive() {
        return loanRepository.findByStatus(LoanStatus.ACTIVE);
    }

    public Loan findById(String id) {
        return loanRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Empréstimo não encontrado: " + id));
    }

    // Reduz availableQuantity ao criar empréstimo
    public Loan create(String bookId, String readerId) {
        Book book = bookService.findById(bookId);
        Reader reader = readerService.findById(readerId);

        if (book.getAvailableQuantity() <= 0) {
            throw new IllegalStateException("Livro indisponível para empréstimo");
        }

        book.setAvailableQuantity(book.getAvailableQuantity() - 1);
        bookService.update(book);

        Loan loan = new Loan();
        loan.setBookId(bookId);
        loan.setReaderId(readerId);
        loan.setBookTitle(book.getTitle());
        loan.setReaderName(reader.getName());
        loan.setLoanDate(LocalDate.now());
        loan.setExpectedReturnDate(LocalDate.now().plusDays(14));
        loan.setStatus(LoanStatus.ACTIVE);

        return loanRepository.save(loan);
    }

    // Aumenta availableQuantity ao devolver
    public Loan returnLoan(String loanId) {
        Loan loan = findById(loanId);

        if (loan.getStatus() != LoanStatus.ACTIVE) {
            throw new IllegalStateException("Empréstimo já foi devolvido");
        }

        loan.setActualReturnDate(LocalDate.now());
        loan.setStatus(LoanStatus.RETURNED);

        Book book = bookService.findById(loan.getBookId());
        book.setAvailableQuantity(book.getAvailableQuantity() + 1);
        bookService.update(book);

        return loanRepository.save(loan);
    }
}
