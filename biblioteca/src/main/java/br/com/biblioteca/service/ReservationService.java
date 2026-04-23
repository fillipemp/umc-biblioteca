package br.com.biblioteca.service;

import br.com.biblioteca.model.*;
import br.com.biblioteca.repository.ReservationRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final BookService bookService;
    private final ReaderService readerService;

    public ReservationService(ReservationRepository reservationRepository,
                              BookService bookService,
                              ReaderService readerService) {
        this.reservationRepository = reservationRepository;
        this.bookService = bookService;
        this.readerService = readerService;
    }

    public List<Reservation> findAll() {
        return reservationRepository.findAll();
    }

    public Reservation findById(String id) {
        return reservationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Reserva não encontrada: " + id));
    }

    // Reserva não reduz estoque; apenas garante intenção. Empréstimo é que reduz.
    public Reservation create(String bookId, String readerId) {
        Book book = bookService.findById(bookId);
        Reader reader = readerService.findById(readerId);

        if (book.getAvailableQuantity() <= 0) {
            throw new IllegalStateException("Livro indisponível para reserva");
        }

        Reservation reservation = new Reservation();
        reservation.setBookId(bookId);
        reservation.setReaderId(readerId);
        reservation.setBookTitle(book.getTitle());
        reservation.setReaderName(reader.getName());
        reservation.setReservationDate(LocalDate.now());
        reservation.setExpirationDate(LocalDate.now().plusDays(3));
        reservation.setStatus(ReservationStatus.PENDING);

        return reservationRepository.save(reservation);
    }

    public void cancel(String id) {
        Reservation reservation = findById(id);
        reservation.setStatus(ReservationStatus.CANCELLED);
        reservationRepository.save(reservation);
    }

    // Marca reserva como convertida quando um empréstimo é gerado a partir dela
    public void markAsConverted(String id) {
        Reservation reservation = findById(id);
        reservation.setStatus(ReservationStatus.CONVERTED);
        reservationRepository.save(reservation);
    }
}
