package br.com.biblioteca.controller;

import br.com.biblioteca.model.Book;
import br.com.biblioteca.service.BookService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/books")
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping
    public String list(Model model, @RequestParam(required = false) String search) {
        model.addAttribute("books", bookService.search(search));
        model.addAttribute("search", search);
        return "books/list";
    }

    @GetMapping("/new")
    public String newForm(Model model) {
        model.addAttribute("book", new Book());
        model.addAttribute("action", "/books");
        return "books/form";
    }

    @PostMapping
    public String create(@Valid @ModelAttribute Book book, BindingResult result,
                         Model model, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("action", "/books");
            return "books/form";
        }
        bookService.save(book);
        redirectAttributes.addFlashAttribute("success", "Livro cadastrado com sucesso!");
        return "redirect:/books";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable String id, Model model) {
        model.addAttribute("book", bookService.findById(id));
        model.addAttribute("action", "/books/" + id + "/edit");
        return "books/form";
    }

    @PostMapping("/{id}/edit")
    public String update(@PathVariable String id, @Valid @ModelAttribute Book book,
                         BindingResult result, Model model, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("action", "/books/" + id + "/edit");
            return "books/form";
        }
        book.setId(id);
        bookService.update(book);
        redirectAttributes.addFlashAttribute("success", "Livro atualizado com sucesso!");
        return "redirect:/books";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable String id, RedirectAttributes redirectAttributes) {
        bookService.deleteById(id);
        redirectAttributes.addFlashAttribute("success", "Livro removido com sucesso!");
        return "redirect:/books";
    }
}
