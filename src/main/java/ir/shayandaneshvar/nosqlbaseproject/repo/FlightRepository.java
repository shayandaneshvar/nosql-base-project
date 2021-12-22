package ir.shayandaneshvar.nosqlbaseproject.repo;

import ir.shayandaneshvar.nosqlbaseproject.model.Flight;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FlightRepository extends JpaRepository<Flight, Long> {
}
