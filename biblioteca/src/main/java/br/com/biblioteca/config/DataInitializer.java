package br.com.biblioteca.config;

import br.com.biblioteca.model.User;
import br.com.biblioteca.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

// Cria o admin padrão na primeira vez que a aplicação sobe.
// Trocar a senha após avaliação.
@Component
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        if (!userRepository.existsByEmail("admin@admin.com")) {
            User admin = new User();
            admin.setName("Administrador");
            admin.setEmail("admin@admin.com");
            admin.setPassword(passwordEncoder.encode("Admin@123"));
            admin.setRole("ADMIN");
            userRepository.save(admin);
            System.out.println(">>> Usuário admin criado: admin@admin.com / Admin@123");
        }
    }
}
