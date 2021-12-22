package ir.shayandaneshvar.nosqlbaseproject;

import ir.shayandaneshvar.nosqlbaseproject.util.GeneratorTool;
import org.ajbrown.namemachine.NameGenerator;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

public class BaseTest {

    @Test
    void test() {
        NameGenerator generator = new NameGenerator();
        generator.generateNames(1000).stream().skip(990).forEach(System.out::println);

    }




    @Test
    void test2() {

    }

}
