package drajner.hetman.requests;

import drajner.hetman.services.Fight;
import drajner.hetman.services.FightStatus;
import drajner.hetman.services.TournamentParticipant;
import lombok.Getter;

@Getter
public class FightReport {

    private String username;
    private int tournamentNumber;
    private int groupNumber;
    private int fightNumber;
    private int firstParticipantNumber;
    private int secondParticipantNumber;
    private int firstParticipantPoints;
    private int secondParticipantPoints;
    private int firstParticipantCards;
    private int secondParticipantCards;
    private int doubles;
    private FightStatus status;
    private int winnerNumber;

    public FightReport(String username, int tournamentNumber, int groupNumber, int fightNumber,
                       int firstParticipantNumber, int secondParticipantNumber,
                       int firstParticipantPoints, int secondParticipantPoints, int firstParticipantCards,
                       int secondParticipantCards, int doubles, FightStatus fightStatus, int winnerNumber){
        this.username = username;
        this.tournamentNumber = tournamentNumber;
        this.groupNumber = groupNumber;
        this.fightNumber = fightNumber;
        this.firstParticipantNumber = firstParticipantNumber;
        this.secondParticipantNumber = secondParticipantNumber;
        this.firstParticipantPoints = firstParticipantPoints;
        this.secondParticipantPoints = secondParticipantPoints;
        this.firstParticipantCards = firstParticipantCards;
        this.secondParticipantCards = secondParticipantCards;
        this.doubles = doubles;
        this.status = fightStatus;
        this.winnerNumber = winnerNumber;
    }

}
