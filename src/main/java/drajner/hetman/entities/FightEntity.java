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
    @GeneratedValue
    Long id;

    @ManyToOne
    @JoinColumn(name = "participantId")
    TournamentParticipantEntity firstParticipant;

    @ManyToOne
    @JoinColumn(name = "participantId")
    TournamentParticipantEntity secondParticipant;

    int firstParticipantPoints;
    int secondParticipantPoints;
    int firstParticipantCards;
    int secondParticipantCards;
    int doubles;

    @Enumerated(EnumType.ORDINAL)
    FightStatus status;

    @ManyToOne
    @JoinColumn(name = "participantId")
    TournamentParticipantEntity winner;

    @ManyToOne
    @JoinColumn(name = "groupId")
    GroupEntity groupId;
}
