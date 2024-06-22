package drajner.hetman.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="tournaments")
@Getter
@Setter
public class TournamentEntity {

    @Id
    @GeneratedValue
    Long tournamentId;
    String name;

    @JsonManagedReference
    @OneToMany(mappedBy="tournament")
    List<TournamentParticipantEntity> participants;

    @JsonManagedReference
    @OneToMany(mappedBy="tournament")
    List<GroupEntity> groups;


    @JsonManagedReference
    @ManyToOne
    @JoinColumn(name="finalFight")
    FinalsTreeNodeEntity finalFight;

    @JsonManagedReference
    @ManyToOne
    @JoinColumn(name="thirdPlaceFight")
    FinalsTreeNodeEntity thirdPlaceFight;

    public TournamentEntity(){}
    public TournamentEntity(String name){
        this.name = name;
    }
}
