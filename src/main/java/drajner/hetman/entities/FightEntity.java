package drajner.hetman.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import drajner.hetman.errors.UnfinishedFightException;
import drajner.hetman.status.FightStatus;
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

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "groupId")
    GroupEntity group;

    @JsonProperty("groupId")
    public Long getGroupId() {
        return group != null ? group.getGroupId() : null;
    }

    public FightEntity(){
        this.firstParticipant = null;
        this.secondParticipant = null;
        this.firstParticipantPoints = 0;
        this.secondParticipantPoints = 0;
        this.firstParticipantCards = 0;
        this.secondParticipantCards = 0;
        this.doubles = 0;
        this.status = FightStatus.PENDING;
        this.winner = null;
    }

    public FightEntity(TournamentParticipantEntity firstParticipant){
        this.firstParticipant = firstParticipant;
        this.secondParticipant = null;
        this.firstParticipantPoints = 0;
        this.secondParticipantPoints = 0;
        this.firstParticipantCards = 0;
        this.secondParticipantCards = 0;
        this.doubles = 0;
        this.status = FightStatus.PENDING;
        this.winner = null;
    }
    public FightEntity(TournamentParticipantEntity firstParticipant, TournamentParticipantEntity secondParticipant){
        this.firstParticipant = firstParticipant;
        this.secondParticipant = secondParticipant;
        this.firstParticipantPoints = 0;
        this.secondParticipantPoints = 0;
        this.firstParticipantCards = 0;
        this.secondParticipantCards = 0;
        this.doubles = 0;
        this.status = FightStatus.PENDING;
        this.winner = null;
    }

    public FightEntity(TournamentParticipantEntity firstParticipant, TournamentParticipantEntity secondParticipant, int firstParticipantPoints, int secondParticipantPoints, int doubles, FightStatus status, TournamentParticipantEntity winner){
        this.firstParticipant = firstParticipant;
        this.secondParticipant = secondParticipant;
        this.firstParticipantPoints = firstParticipantPoints;
        this.secondParticipantPoints = secondParticipantPoints;
        this.firstParticipantCards = 0;
        this.secondParticipantCards = 0;
        this.doubles = doubles;
        this.status = status;
        this.winner = winner;
    }

    public TournamentParticipantEntity findLoser() throws UnfinishedFightException {
        if(status == FightStatus.PENDING){
            throw new UnfinishedFightException("Fight is not yet finished");
        }
        if(winner == firstParticipant) return secondParticipant;
        else return firstParticipant;
    }
}
