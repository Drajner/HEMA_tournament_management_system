package drajner.hetman.entities;

import drajner.hetman.services.TournamentParticipant;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="groups")
@Getter
@Setter
public class GroupEntity {

    @Id
    @GeneratedValue
    Long groupId;

    @ManyToOne
    @JoinColumn(name="tournamentId", nullable = false)
    TournamentEntity tournament;

    @ManyToMany(mappedBy = "groupParticipations")
    List<TournamentParticipantEntity> groupParticipants;

    @OneToMany(mappedBy = "group")
    List<FightEntity> groupFights;

    float modifier;

    public GroupEntity(){
        groupParticipants = new ArrayList<>();
        groupFights = new ArrayList<>();
    }

    public GroupEntity(ArrayList<TournamentParticipantEntity> groupParticipants){
        this.groupParticipants = groupParticipants;
        this.fights = new ArrayList<>();
    }

}
