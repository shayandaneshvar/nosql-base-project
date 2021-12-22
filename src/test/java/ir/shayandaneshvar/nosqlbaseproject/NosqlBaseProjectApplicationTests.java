package ir.shayandaneshvar.nosqlbaseproject;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.ResourceUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Scanner;

@SpringBootTest
class NosqlBaseProjectApplicationTests {

    @Test
    void contextLoads() throws FileNotFoundException {
        File f = ResourceUtils.getFile("classpath:countries.txt");
        FileInputStream fis = new FileInputStream(f);
        Scanner scanner = new Scanner(f);
        while (scanner.hasNextLine()){
            System.out.println(scanner.nextLine());
        }
    }

}
