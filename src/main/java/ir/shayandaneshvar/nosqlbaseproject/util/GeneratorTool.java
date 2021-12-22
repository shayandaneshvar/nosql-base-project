package ir.shayandaneshvar.nosqlbaseproject.util;

import lombok.experimental.UtilityClass;
import org.ajbrown.namemachine.Name;
import org.ajbrown.namemachine.NameGenerator;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@UtilityClass
public class GeneratorTool {
    private final NameGenerator nameGenerator = new NameGenerator();

    public List<Name> generatePeople(int count) {
        return nameGenerator.generateNames(count);
    }

    public List<String> getCountries() {
        return fetchFromFile("countries.txt");
    }

    public List<String> getAirlines() {
        return fetchFromFile("airlines.txt");
    }

    private List<String> fetchFromFile(String filename) {
        List<String> result = new ArrayList<>();
        Resource resource = new ClassPathResource(filename);
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(resource.getInputStream()))) {
            reader.lines().forEach(result::add);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    public <T> T randomElement(List<T> list) {
        return list.get(
                ThreadLocalRandom.current().nextInt(0, list.size()));
    }

    public <T> T randomElement(List<T> list, T duplicate) {
        T element = randomElement(list);
        if (element.equals(duplicate)) {
            return randomElement(list, duplicate);
        }
        return element;
    }


}
