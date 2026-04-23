package br.com.biblioteca.controller;

import br.com.biblioteca.model.LoanStatus;
import br.com.biblioteca.repository.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {

    private final BookRepository bookRepository;
    private final ReaderRepository readerRepository;
    private final LoanRepository loanRepository;
    private final ReservationRepository reservationRepository;

    public DashboardController(BookRepository bookRepository, ReaderRepository readerRepository,
                               LoanRepository loanRepository, ReservationRepository reservationRepository) {
        this.bookRepository = bookRepository;
        this.readerRepository = readerRepository;
        this.loanRepository = loanRepository;
        this.reservationRepository = reservationRepository;
    }

    @GetMapping("/")
    public String dashboard(Model model) {
        model.addAttribute("totalBooks", bookRepository.count());
        model.addAttribute("totalReaders", readerRepository.count());
        model.addAttribute("activeLoans", loanRepository.findByStatus(LoanStatus.ACTIVE).size());
        model.addAttribute("totalReservations", reservationRepository.count());
        return "dashboard";
    }

    @GetMapping("/login")
    public String login() {
        return "auth/login";
    }
}
