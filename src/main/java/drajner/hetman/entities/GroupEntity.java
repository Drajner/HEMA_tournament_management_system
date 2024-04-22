package drajner.hetman.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name="groups")
@Getter
@Setter
public class GroupEntity {

    @Id
    @GeneratedValue
    Long id;

    @ManyToOne
    @JoinColumn(name="tournamentId", nullable = false)
    TournamentEntity tournamentEntity;

    @ManyToMany(mappedBy = "groupParticipations")
    List<GroupEntity> groupParticipants;

    @OneToMany(mappedBy = "fights")
    List<FightEntity> groupFights;

    float modifier;


}
