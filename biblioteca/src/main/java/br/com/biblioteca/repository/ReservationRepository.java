package br.com.biblioteca.repository;

import br.com.biblioteca.model.Reservation;
import br.com.biblioteca.model.ReservationStatus;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface ReservationRepository extends MongoRepository<Reservation, String> {
    List<Reservation> findByStatus(ReservationStatus status);
    List<Reservation> findByReaderIdAndStatus(String readerId, ReservationStatus status);
}
