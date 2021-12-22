package ir.shayandaneshvar.nosqlbaseproject.repo;

import ir.shayandaneshvar.nosqlbaseproject.model.Location;
import ir.shayandaneshvar.nosqlbaseproject.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {
    List<Person> findByLocation(Location location);
    List<Person> findByBalanceLessThanEqual(BigDecimal balance);
    List<Person> findByBalanceGreaterThanEqual(BigDecimal balance);
}
