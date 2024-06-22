package drajner.hetman.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
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

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name="tournamentId", nullable = false)
    TournamentEntity tournament;

    @JsonManagedReference
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
        this.groupFights = new ArrayList<>();
    }

    public GroupEntity(TournamentEntity tournament){
        this.tournament = tournament;
        this.groupParticipants = new ArrayList<>();
        this.groupFights = new ArrayList<>();
    }

}
