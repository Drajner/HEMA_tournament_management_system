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

    boolean reverted;

    @ManyToOne
    @JoinColumn(name="fightId")
    FightEntity fight;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="firstChildNodeId")
    FinalsTreeNodeEntity firstChildNode;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="secondChildNodeId")
    FinalsTreeNodeEntity secondChildNode;

    public FinalsTreeNodeEntity(){}
    public FinalsTreeNodeEntity(boolean reverted){
        this.reverted = reverted;
    }
}
