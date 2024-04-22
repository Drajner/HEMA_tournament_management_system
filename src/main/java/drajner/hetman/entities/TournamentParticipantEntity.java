package drajner.hetman.entities;

import drajner.hetman.services.CompetitorStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name="tournamentParticipants")
@Getter
@Setter
public class TournamentParticipantEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    Long participantId;
    String name;
    String surname;
    String club;

    @Enumerated(EnumType.ORDINAL)
    CompetitorStatus status;

    float score;
    float wins;
    float doubles;
    int cards;
    int ranking;

    @ManyToOne
    @JoinColumn(name="tournamentId", nullable=false)
    TournamentEntity tournamentParticipation;

    @ManyToMany
    @JoinTable(
            name="groupParticipation",
            joinColumns=@JoinColumn(name="participantId"),
            inverseJoinColumns=@JoinColumn(name="groupId")
    )
    List<GroupEntity> groupParticipations;

}
