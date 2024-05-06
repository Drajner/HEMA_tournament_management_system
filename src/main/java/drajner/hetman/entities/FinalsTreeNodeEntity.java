package drajner.hetman.entities;

import drajner.hetman.services.Fight;
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

    @ManyToOne
    @JoinColumn(name="firstChildNodeId")
    FinalsTreeNodeEntity firstChildNode;

    @ManyToOne
    @JoinColumn(name="secondChildNodeId")
    FinalsTreeNodeEntity secondChildNode;

    public FinalsTreeNodeEntity(){this.fight = new FightEntity();}
    public FinalsTreeNodeEntity(boolean reverted){
        this.fight = new FightEntity();
        this.reverted = reverted;
    }
}
