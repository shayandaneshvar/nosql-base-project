package ir.shayandaneshvar.nosqlbaseproject.controller;

import ir.shayandaneshvar.nosqlbaseproject.bootstrap.Bootstrapper;
import ir.shayandaneshvar.nosqlbaseproject.controller.dto.Transaction;
import ir.shayandaneshvar.nosqlbaseproject.model.Flight;
import ir.shayandaneshvar.nosqlbaseproject.model.Location;
import ir.shayandaneshvar.nosqlbaseproject.model.Person;
import ir.shayandaneshvar.nosqlbaseproject.repo.FlightRepository;
import ir.shayandaneshvar.nosqlbaseproject.repo.LocationRepository;
import ir.shayandaneshvar.nosqlbaseproject.repo.PersonRepository;
import ir.shayandaneshvar.nosqlbaseproject.util.GeneratorTool;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/")
public class DataController {
    private final PersonRepository personRepository;
    private final FlightRepository flightRepository;
    private final LocationRepository locationRepository;
    private Queue<Flight> flights = new LinkedList<>();
    private Queue<Transaction> transactions = new LinkedList<>();
    private static final Lock lock = new ReentrantLock();
    private static final Lock transactionLock = new ReentrantLock();
    private boolean paused = false;

    @GetMapping("/events/flights")
    public Collection<Flight> getFlightEvents(@RequestParam(defaultValue = "true") Boolean consume) {
        lock.lock();
        List<Flight> result = new ArrayList<>(flights);
        if (consume) {
            flights = new LinkedList<>();
        }
        lock.unlock();
        return result;
    }

    @GetMapping("/events/transactions")
    public Collection<Transaction> getTransactionEvents(@RequestParam(defaultValue = "true") Boolean consume) {
        transactionLock.lock();
        List<Transaction> result = new ArrayList<>(transactions);
        if (consume) {
            transactions = new LinkedList<>();
        }
        transactionLock.unlock();
        return result;
    }

    @GetMapping("/events/transactions/count")
    public Integer getNumberOfUnconsumedTransactionEvents() {
        return transactions.size();
    }

    @GetMapping("/events/flights/count")
    public Integer getNumberOfUnconsumedFlightEvents() {
        return flights.size();
    }

    @PostMapping("/events/pause")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void pauseDataGeneration() {
        paused = true;
    }

    @PostMapping("/events/continue")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void continueDataGeneration() {
        paused = false;
    }

    @Transactional(isolation = Isolation.READ_COMMITTED, timeout = 0, propagation = Propagation.SUPPORTS)
    @Scheduled(initialDelay = 500, fixedRate = 307)
    public void transactionDataGeneratorJob() {
        if (paused) return;
        List<Person> people = personRepository.findByBalanceGreaterThanEqual(new BigDecimal(100))
                .stream().skip(5).collect(Collectors.toList());

        people.parallelStream().forEach(p -> {
            BigDecimal amount = BigDecimal.valueOf(-ThreadLocalRandom.current().nextDouble(5, 90));
            Transaction transaction = new Transaction()
                    .setType(
                            Transaction.Type.randomNegative(Transaction.Type.WITHDRAW_FOR_FLIGHT))
                    .setAmount(amount)
                    .setPerson(p)
                    .setTimestamp(Instant.now()
                            .minus(7 * 365, ChronoUnit.DAYS)
                            .plus((System.currentTimeMillis() - Bootstrapper.startTime) / 200, ChronoUnit.DAYS)
                            .toEpochMilli());
            p.withdrawFromBalance(-amount.doubleValue());
            personRepository.save(p);
            transactionLock.lock();
            transactions.add(transaction);
            transactionLock.unlock();
        });


        people = personRepository.findByBalanceLessThanEqual(new BigDecimal(600))
                .stream().skip(5).collect(Collectors.toList());

        people.parallelStream().forEach(p -> {
            BigDecimal amount = BigDecimal.valueOf(ThreadLocalRandom.current().nextDouble(50, 500));
            Transaction transaction = new Transaction()
                    .setType(Transaction.Type.randomPositive(Transaction.Type.WITHDRAW_FOR_FLIGHT))
                    .setAmount(amount)
                    .setPerson(p)
                    .setTimestamp(Instant.now()
                            .minus(7 * 365, ChronoUnit.DAYS)
                            .plus((System.currentTimeMillis() - Bootstrapper.startTime) / 200, ChronoUnit.DAYS)
                            .toEpochMilli());
            p.depositToBalance(amount.doubleValue());
            personRepository.save(p);
            transactionLock.lock();
            transactions.add(transaction);
            transactionLock.unlock();
        });

    }

    @Transactional(isolation = Isolation.READ_COMMITTED, timeout = 0, propagation = Propagation.SUPPORTS)
    @Scheduled(initialDelay = 550, fixedRate = 411)
    public void flightDataGeneratorJob() {
        if (paused) return;
        List<Person> people = personRepository
                .findByLocation(GeneratorTool.randomElement(Bootstrapper.getCountries()));
        if (!people.isEmpty()) {
            Location from = locationRepository
                    .findByName(people.get(0).getLocation().getName());
            Location to = locationRepository
                    .findByName(GeneratorTool.randomElement(Bootstrapper
                            .getCountries(), from).getName());
            double price = 100 * ThreadLocalRandom.current().nextDouble(1, 5);
            people = people.parallelStream()
                    .filter(z -> z.getBalance().doubleValue() >= price)
                    .map(z -> z.setLocation(to))
                    .map(Person::incrementNumberOfTrips)
                    .map(p -> p.withdrawFromBalance(price))
                    .map(personRepository::save)
                    .collect(Collectors.toList());
            people.parallelStream().forEach(z -> {
                Transaction transaction = new Transaction().setAmount(new BigDecimal(price))
                        .setPerson(z).setType(Transaction.Type.WITHDRAW_FOR_FLIGHT)
                        .setTimestamp(Instant.now()
                                .minus(7 * 365, ChronoUnit.DAYS)
                                .plus((System.currentTimeMillis() - Bootstrapper.startTime) / 200, ChronoUnit.DAYS)
                                .toEpochMilli());
                transactionLock.lock();
                transactions.add(transaction);
                transactionLock.unlock();
            });
            Flight flight = new Flight()
                    .setPassengers(people)
                    .setFrom(from)
                    .setTo(to)
                    .setDate(new Date(Instant.now()
                            .minus(7 * 365, ChronoUnit.DAYS)
                            .plus((System.currentTimeMillis() - Bootstrapper.startTime) / 200, ChronoUnit.DAYS)
                            .toEpochMilli()));
            flightRepository.save(flight);
            lock.lock();
            flights.add(flight);
            lock.unlock();
        }
        if (people.size() < 2) flightDataGeneratorJob();
    }
}
