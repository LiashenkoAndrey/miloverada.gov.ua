package gov.milove.domain.digitalQueue;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@Entity
@Table("services")
@Getter
@Setter
public class Service {

    private String name;

    public String description;

    @OneToMany
    private List<Record> records;
}


