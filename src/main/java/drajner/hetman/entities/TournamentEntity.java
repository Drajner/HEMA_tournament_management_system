package drajner.hetman.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

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

    @OneToMany(mappedBy="tournamentParticipation")
    List<TournamentParticipantEntity> participants;

    @OneToMany(mappedBy="tournament")
    List<GroupEntity> groups;

    @ManyToOne
    @JoinColumn(name="finalFight")
    FinalsTreeNodeEntity finalFight;

    @ManyToOne
    @JoinColumn(name="thirdPlaceFight")
    FinalsTreeNodeEntity thirdPlaceFight;
}
