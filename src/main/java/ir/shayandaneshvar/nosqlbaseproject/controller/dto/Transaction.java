package ir.shayandaneshvar.nosqlbaseproject.controller.dto;

import ir.shayandaneshvar.nosqlbaseproject.model.Person;
import ir.shayandaneshvar.nosqlbaseproject.util.GeneratorTool;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@Accessors(chain = true)
public class Transaction {
    private BigDecimal amount;
    private Person person;
    private Type type;
    private long timestamp;

    public enum Type {
        DEPOSIT_FROM_BANK, DEPOSIT_FROM_PANEL, WITHDRAW_FOR_FLIGHT, WITHDRAW_FOR_BUS_TRIP, WITHDRAW_FOR_HOTEL;


        public static Type random(Type... except) {
            return GeneratorTool.randomElement(Arrays.stream(Type.values())
                    .filter(z -> Arrays.stream(except).noneMatch(x -> x.equals(z)))
                    .collect(Collectors.toList()));
        }

        public static Type randomPositive(Type... except) {
            return GeneratorTool.randomElement(Arrays.stream(new Type[]
                    {Type.DEPOSIT_FROM_BANK, Type.DEPOSIT_FROM_PANEL})
                    .filter(z -> Arrays.stream(except).noneMatch(x -> x.equals(z)))
                    .collect(Collectors.toList()));
        }

        public static Type randomNegative(Type... except) {
            return GeneratorTool.randomElement(Arrays.stream(new Type[]
                    {Type.WITHDRAW_FOR_BUS_TRIP, Type.WITHDRAW_FOR_FLIGHT, Type.WITHDRAW_FOR_HOTEL})
                    .filter(z -> Arrays.stream(except).noneMatch(x -> x.equals(z)))
                    .collect(Collectors.toList()));
        }
    }

}
