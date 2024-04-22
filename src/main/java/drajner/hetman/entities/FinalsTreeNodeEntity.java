package drajner.hetman.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name="finalsTreeNodes")
@Getter
@Setter
public class FinalsTreeNodeEntity {

    @Id
    @GeneratedValue
    Long id;

}
