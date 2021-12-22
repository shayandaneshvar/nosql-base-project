package ir.shayandaneshvar.nosqlbaseproject.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import lombok.experimental.Accessors;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;

@Getter
@Setter
@ToString
@MappedSuperclass
@NoArgsConstructor
@Accessors(chain = true)
public class BaseEntity<ID extends Serializable> implements Serializable {
    @Id
    @JsonIgnore
    @GeneratedValue(strategy = GenerationType.AUTO)
    private ID id;
}
