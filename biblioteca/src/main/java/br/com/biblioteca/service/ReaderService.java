package br.com.biblioteca.service;

import br.com.biblioteca.model.Reader;
import br.com.biblioteca.repository.ReaderRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ReaderService {

    private final ReaderRepository readerRepository;

    public ReaderService(ReaderRepository readerRepository) {
        this.readerRepository = readerRepository;
    }

    public List<Reader> findAll() {
        return readerRepository.findAll();
    }

    public List<Reader> findAllActive() {
        return readerRepository.findByActiveTrue();
    }

    public Reader findById(String id) {
        return readerRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Leitor não encontrado: " + id));
    }

    public Reader save(Reader reader) {
        return readerRepository.save(reader);
    }

    public void deleteById(String id) {
        readerRepository.deleteById(id);
    }

    public List<Reader> search(String name) {
        if (name == null || name.isBlank()) return findAll();
        return readerRepository.findByNameContainingIgnoreCase(name);
    }
}
