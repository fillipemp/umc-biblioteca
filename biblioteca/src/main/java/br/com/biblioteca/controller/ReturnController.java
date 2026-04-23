package br.com.biblioteca.controller;

import br.com.biblioteca.service.LoanService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/returns")
public class ReturnController {

    private final LoanService loanService;

    public ReturnController(LoanService loanService) {
        this.loanService = loanService;
    }

    // Lista empréstimos ativos para registrar devolução
    @GetMapping
    public String list(Model model) {
        model.addAttribute("activeLoans", loanService.findActive());
        return "returns/list";
    }

    @PostMapping("/{loanId}")
    public String registerReturn(@PathVariable String loanId,
                                 RedirectAttributes redirectAttributes) {
        try {
            loanService.returnLoan(loanId);
            redirectAttributes.addFlashAttribute("success", "Devolução registrada com sucesso!");
        } catch (IllegalStateException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/returns";
    }
}
