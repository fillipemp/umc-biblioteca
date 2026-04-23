package br.com.biblioteca.service;

import br.com.biblioteca.model.User;
import br.com.biblioteca.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User findById(String id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado: " + id));
    }

    // criptografa a senha antes de salvar o novo operador
    public User create(User user, String rawPassword) {
        user.setPassword(passwordEncoder.encode(rawPassword));
        return userRepository.save(user);
    }

    // atualiza os dados; só troca a senha se uma nova for informada
    public User update(String id, User data, String newPassword) {
        User existing = findById(id);
        existing.setName(data.getName());
        existing.setEmail(data.getEmail());
        existing.setRole(data.getRole());
        if (newPassword != null && !newPassword.isBlank()) {
            existing.setPassword(passwordEncoder.encode(newPassword));
        }
        return userRepository.save(existing);
    }

    public void deleteById(String id) {
        User user = findById(id);
        if ("ADMIN".equals(user.getRole())) {
            long adminCount = userRepository.countByRole("ADMIN");
            if (adminCount <= 1) {
                throw new IllegalStateException(
                    "Não é possível remover o único administrador do sistema.");
            }
        }
        userRepository.deleteById(id);
    }

    public boolean emailExists(String email, String excludeId) {
        return userRepository.findByEmail(email)
                .map(u -> !u.getId().equals(excludeId))
                .orElse(false);
    }
}
