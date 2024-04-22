package drajner.hetman.entities;

import drajner.hetman.services.FightStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name="fights")
@Getter
@Setter
public class FightEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne
    @JoinColumn(name = "firstParticipantId")
    TournamentParticipantEntity firstParticipant;

    @ManyToOne
    @JoinColumn(name = "secondParticipantId")
    TournamentParticipantEntity secondParticipant;

    int firstParticipantPoints;
    int secondParticipantPoints;
    int firstParticipantCards;
    int secondParticipantCards;
    int doubles;

    @Enumerated(EnumType.ORDINAL)
    FightStatus status;

    @ManyToOne
    @JoinColumn(name = "winnerId")
    TournamentParticipantEntity winner;

    @ManyToOne
    @JoinColumn(name = "groupId")
    GroupEntity group;
}
