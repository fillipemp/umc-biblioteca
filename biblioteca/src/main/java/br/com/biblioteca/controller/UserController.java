package br.com.biblioteca.controller;

import br.com.biblioteca.model.User;
import br.com.biblioteca.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // lista todos os operadores
    @GetMapping
    public String list(Model model) {
        model.addAttribute("users", userService.findAll());
        return "users/list";
    }

    // abre o formulário de cadastro
    @GetMapping("/new")
    public String newForm(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("action", "/users");
        model.addAttribute("isNew", true);
        return "users/form";
    }

    // salva o novo operador
    @PostMapping
    public String create(@ModelAttribute User user,
                         @RequestParam String password,
                         @RequestParam(required = false) String confirmPassword,
                         Model model,
                         RedirectAttributes redirectAttributes) {

        // validações antes de salvar
        if (user.getName() == null || user.getName().isBlank()) {
            model.addAttribute("user", user);
            model.addAttribute("action", "/users");
            model.addAttribute("isNew", true);
            model.addAttribute("fieldError", "Nome é obrigatório.");
            return "users/form";
        }
        if (password == null || password.length() < 6) {
            model.addAttribute("user", user);
            model.addAttribute("action", "/users");
            model.addAttribute("isNew", true);
            model.addAttribute("fieldError", "A senha deve ter pelo menos 6 caracteres.");
            return "users/form";
        }
        if (!password.equals(confirmPassword)) {
            model.addAttribute("user", user);
            model.addAttribute("action", "/users");
            model.addAttribute("isNew", true);
            model.addAttribute("fieldError", "As senhas não coincidem.");
            return "users/form";
        }
        if (userService.emailExists(user.getEmail(), "")) {
            model.addAttribute("user", user);
            model.addAttribute("action", "/users");
            model.addAttribute("isNew", true);
            model.addAttribute("fieldError", "Já existe um usuário com este e-mail.");
            return "users/form";
        }

        userService.create(user, password);
        redirectAttributes.addFlashAttribute("success", "Operador criado com sucesso!");
        return "redirect:/users";
    }

    // carrega os dados do operador para editar
    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable String id, Model model) {
        model.addAttribute("user", userService.findById(id));
        model.addAttribute("action", "/users/" + id + "/edit");
        model.addAttribute("isNew", false);
        return "users/form";
    }

    // salva as alterações do operador
    @PostMapping("/{id}/edit")
    public String update(@PathVariable String id,
                         @ModelAttribute User user,
                         @RequestParam(required = false) String password,
                         @RequestParam(required = false) String confirmPassword,
                         Model model,
                         RedirectAttributes redirectAttributes) {

        if (user.getName() == null || user.getName().isBlank()) {
            model.addAttribute("user", user);
            model.addAttribute("action", "/users/" + id + "/edit");
            model.addAttribute("isNew", false);
            model.addAttribute("fieldError", "Nome é obrigatório.");
            return "users/form";
        }
        if (password != null && !password.isBlank()) {
            if (password.length() < 6) {
                model.addAttribute("user", user);
                model.addAttribute("action", "/users/" + id + "/edit");
                model.addAttribute("isNew", false);
                model.addAttribute("fieldError", "A senha deve ter pelo menos 6 caracteres.");
                return "users/form";
            }
            if (!password.equals(confirmPassword)) {
                model.addAttribute("user", user);
                model.addAttribute("action", "/users/" + id + "/edit");
                model.addAttribute("isNew", false);
                model.addAttribute("fieldError", "As senhas não coincidem.");
                return "users/form";
            }
        }
        if (userService.emailExists(user.getEmail(), id)) {
            model.addAttribute("user", user);
            model.addAttribute("action", "/users/" + id + "/edit");
            model.addAttribute("isNew", false);
            model.addAttribute("fieldError", "Já existe outro usuário com este e-mail.");
            return "users/form";
        }

        userService.update(id, user, password);
        redirectAttributes.addFlashAttribute("success", "Operador atualizado com sucesso!");
        return "redirect:/users";
    }

    // remove o operador
    @PostMapping("/{id}/delete")
    public String delete(@PathVariable String id, RedirectAttributes redirectAttributes) {
        try {
            userService.deleteById(id);
            redirectAttributes.addFlashAttribute("success", "Operador removido com sucesso!");
        } catch (IllegalStateException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/users";
    }
}
