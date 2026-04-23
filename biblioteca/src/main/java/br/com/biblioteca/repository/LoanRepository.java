package br.com.biblioteca.repository;

import br.com.biblioteca.model.Loan;
import br.com.biblioteca.model.LoanStatus;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface LoanRepository extends MongoRepository<Loan, String> {
    List<Loan> findByStatus(LoanStatus status);
    List<Loan> findByReaderIdAndStatus(String readerId, LoanStatus status);
}
