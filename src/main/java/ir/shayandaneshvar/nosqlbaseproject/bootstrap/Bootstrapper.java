package ir.shayandaneshvar.nosqlbaseproject.bootstrap;

import ir.shayandaneshvar.nosqlbaseproject.model.Location;
import ir.shayandaneshvar.nosqlbaseproject.model.Person;
import ir.shayandaneshvar.nosqlbaseproject.repo.LocationRepository;
import ir.shayandaneshvar.nosqlbaseproject.repo.PersonRepository;
import ir.shayandaneshvar.nosqlbaseproject.util.GeneratorTool;
import lombok.RequiredArgsConstructor;
import org.ajbrown.namemachine.Gender;
import org.ajbrown.namemachine.Name;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class Bootstrapper implements CommandLineRunner {
    private final LocationRepository locationRepository;
    private final PersonRepository personRepository;

    private static List<Location> countries = new ArrayList<>();
    public static final long startTime;

    static {
        startTime = System.currentTimeMillis();
    }

    public static List<Location> getCountries() {
        return countries;
    }

    @Override
    public void run(String... args) {
        System.out.println("args ():" + Arrays.toString(args));
        int size = Arrays.stream(args).filter(z -> z.contains("size="))
                .map(z -> z.replace("size=", ""))
                .map(Integer::parseInt)
                .findAny()
                .orElse(2500);
        System.out.println("Bootstrapping...");
        List<Name> names = GeneratorTool.generatePeople(size - 1);
        names.add(new Name("John", "Wick", Gender.MALE));

        countries = GeneratorTool.getCountries()
                .parallelStream()
                .map(z -> new Location().setName(z))
                .map(locationRepository::save)
                .collect(Collectors.toList());

        List<Person> people = names.stream().map(p ->
                new Person()
                        .setFirstname(p.getFirstName())
                        .setLastname(p.getLastName())
                        .setBalance(new BigDecimal(500))
                        .setGender(p.getGender())
                        .setNumberOfTrips(0L)
                        .setLocation(GeneratorTool.randomElement(countries)))
                .map(personRepository::save)
                .collect(Collectors.toList());


    }
}
