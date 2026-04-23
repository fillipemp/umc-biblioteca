package br.com.biblioteca.repository;

import br.com.biblioteca.model.Reader;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface ReaderRepository extends MongoRepository<Reader, String> {
    List<Reader> findByNameContainingIgnoreCase(String name);
    List<Reader> findByActiveTrue();
    boolean existsByEmail(String email);
}
