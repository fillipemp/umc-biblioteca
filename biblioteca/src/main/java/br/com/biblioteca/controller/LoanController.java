package br.com.biblioteca.controller;

import br.com.biblioteca.service.BookService;
import br.com.biblioteca.service.LoanService;
import br.com.biblioteca.service.ReaderService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/loans")
public class LoanController {

    private final LoanService loanService;
    private final BookService bookService;
    private final ReaderService readerService;

    public LoanController(LoanService loanService, BookService bookService,
                          ReaderService readerService) {
        this.loanService = loanService;
        this.bookService = bookService;
        this.readerService = readerService;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("loans", loanService.findAll());
        return "loans/list";
    }

    @GetMapping("/new")
    public String newForm(Model model) {
        model.addAttribute("books", bookService.findAll());
        model.addAttribute("readers", readerService.findAllActive());
        return "loans/form";
    }

    @PostMapping
    public String create(@RequestParam String bookId, @RequestParam String readerId,
                         RedirectAttributes redirectAttributes) {
        try {
            loanService.create(bookId, readerId);
            redirectAttributes.addFlashAttribute("success", "Empréstimo registrado com sucesso!");
        } catch (IllegalStateException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/loans";
    }
}
