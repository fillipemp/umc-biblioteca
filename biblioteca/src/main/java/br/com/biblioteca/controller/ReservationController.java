package br.com.biblioteca.controller;

import br.com.biblioteca.service.BookService;
import br.com.biblioteca.service.ReaderService;
import br.com.biblioteca.service.ReservationService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/reservations")
public class ReservationController {

    private final ReservationService reservationService;
    private final BookService bookService;
    private final ReaderService readerService;

    public ReservationController(ReservationService reservationService,
                                 BookService bookService, ReaderService readerService) {
        this.reservationService = reservationService;
        this.bookService = bookService;
        this.readerService = readerService;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("reservations", reservationService.findAll());
        return "reservations/list";
    }

    @GetMapping("/new")
    public String newForm(Model model) {
        model.addAttribute("books", bookService.findAll());
        model.addAttribute("readers", readerService.findAllActive());
        return "reservations/form";
    }

    @PostMapping
    public String create(@RequestParam String bookId, @RequestParam String readerId,
                         RedirectAttributes redirectAttributes) {
        try {
            reservationService.create(bookId, readerId);
            redirectAttributes.addFlashAttribute("success", "Reserva criada com sucesso!");
        } catch (IllegalStateException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/reservations";
    }

    @PostMapping("/{id}/cancel")
    public String cancel(@PathVariable String id, RedirectAttributes redirectAttributes) {
        reservationService.cancel(id);
        redirectAttributes.addFlashAttribute("success", "Reserva cancelada.");
        return "redirect:/reservations";
    }
}
