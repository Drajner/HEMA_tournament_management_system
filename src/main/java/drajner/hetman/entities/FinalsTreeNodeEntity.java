package drajner.hetman.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name="finalsTreeNodes")
@Getter
@Setter
public class FinalsTreeNodeEntity {

    @Id
    @GeneratedValue
    Long nodeId;

    @ManyToOne
    @JoinColumn(name="fightId")
    FightEntity fight;

    @ManyToOne
    @JoinColumn(name="firstChildNodeId")
    FinalsTreeNodeEntity firstChildNode;

    @ManyToOne
    @JoinColumn(name="secondChildNodeId")
    FinalsTreeNodeEntity secondChildNode;
}
