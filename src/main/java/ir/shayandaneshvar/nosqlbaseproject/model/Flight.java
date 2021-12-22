package ir.shayandaneshvar.nosqlbaseproject.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Table(name = "flights")
@Entity(name = "flight")
@Accessors(chain = true)
public class Flight extends BaseEntity<Long> {
    @ManyToOne
    private Location from;
    @ManyToOne
    private Location to;
    @Temporal(value = TemporalType.DATE)
    private Date date;
    @ToString.Exclude
    @ManyToMany(cascade = {})
    private List<Person> passengers = new ArrayList<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o))
            return false;
        Flight flight = (Flight) o;
        return getId() != null && Objects.equals(getId(), flight.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
