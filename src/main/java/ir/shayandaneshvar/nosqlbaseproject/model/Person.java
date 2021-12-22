package ir.shayandaneshvar.nosqlbaseproject.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.ajbrown.namemachine.Gender;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Objects;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity(name = "person")
@Accessors(chain = true)
@Table(name = "people")
public class Person extends BaseEntity<Long> {
    private String firstname;
    private String lastname;
    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
    private Location location;
    @Enumerated(EnumType.STRING)
    private Gender gender;
    @JsonIgnore
    private Long numberOfTrips = 0L;
    private BigDecimal balance = new BigDecimal(0);

    public Person withdrawFromBalance(double value) {
        balance = balance.subtract(new BigDecimal(value));
        return this;
    }

    public Person depositToBalance(double value) {
        balance = balance.add(new BigDecimal(value));
        return this;
    }

    public Person incrementNumberOfTrips() {
        numberOfTrips++;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o))
            return false;
        Person person = (Person) o;
        return getId() != null && Objects.equals(getId(), person.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
