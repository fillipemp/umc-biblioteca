package br.com.biblioteca.controller;

import br.com.biblioteca.model.Reader;
import br.com.biblioteca.service.ReaderService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/readers")
public class ReaderController {

    private final ReaderService readerService;

    public ReaderController(ReaderService readerService) {
        this.readerService = readerService;
    }

    @GetMapping
    public String list(Model model, @RequestParam(required = false) String search) {
        model.addAttribute("readers", readerService.search(search));
        model.addAttribute("search", search);
        return "readers/list";
    }

    @GetMapping("/new")
    public String newForm(Model model) {
        model.addAttribute("reader", new Reader());
        model.addAttribute("action", "/readers");
        return "readers/form";
    }

    @PostMapping
    public String create(@Valid @ModelAttribute Reader reader, BindingResult result,
                         Model model, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("action", "/readers");
            return "readers/form";
        }
        readerService.save(reader);
        redirectAttributes.addFlashAttribute("success", "Leitor cadastrado com sucesso!");
        return "redirect:/readers";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable String id, Model model) {
        model.addAttribute("reader", readerService.findById(id));
        model.addAttribute("action", "/readers/" + id + "/edit");
        return "readers/form";
    }

    @PostMapping("/{id}/edit")
    public String update(@PathVariable String id, @Valid @ModelAttribute Reader reader,
                         BindingResult result, Model model, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("action", "/readers/" + id + "/edit");
            return "readers/form";
        }
        reader.setId(id);
        readerService.save(reader);
        redirectAttributes.addFlashAttribute("success", "Leitor atualizado com sucesso!");
        return "redirect:/readers";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable String id, RedirectAttributes redirectAttributes) {
        readerService.deleteById(id);
        redirectAttributes.addFlashAttribute("success", "Leitor removido com sucesso!");
        return "redirect:/readers";
    }
}
