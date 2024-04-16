package drajner.hetman.services;

import drajner.hetman.errors.UnfinishedFightException;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Fight {

    private TournamentParticipant firstParticipant;
    private TournamentParticipant secondParticipant;
    private int firstParticipantPoints;
    private int secondParticipantPoints;
    private int firstParticipantCards;
    private int secondParticipantCards;
    private int doubles;
    private FightStatus status;
    private TournamentParticipant winner;


    public Fight(){}

    public Fight(TournamentParticipant firstParticipant){
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
    public Fight(TournamentParticipant firstParticipant, TournamentParticipant secondParticipant){
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

    public Fight(TournamentParticipant firstParticipant, TournamentParticipant secondParticipant, int firstParticipantPoints, int secondParticipantPoints, int doubles, FightStatus status, TournamentParticipant winner){
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

    public void setFirstParticipantPoints(int firstParticipantPoints) {
        this.firstParticipantPoints = firstParticipantPoints;
    }

    public TournamentParticipant getLoser() throws UnfinishedFightException{
        if(!(status == FightStatus.FINISHED)){
            throw new UnfinishedFightException("Fight is not yet finished");
        }
        if(winner == firstParticipant) return secondParticipant;
        else return firstParticipant;
    }

    public void evaluateFight(float modifier) throws UnfinishedFightException{
        if(status == FightStatus.FINISHED) {
            evaluateParticipant(firstParticipant, firstParticipantPoints, secondParticipantPoints, firstParticipantCards, modifier);
            evaluateParticipant(secondParticipant, secondParticipantPoints, firstParticipantPoints, secondParticipantCards, modifier);
            if (winner != null) {
                winner.addWin();
            }
            setStatus(FightStatus.EVALUATED);
        }else{
            throw new UnfinishedFightException("This fight is not yet finished!");
        }
    }

    public void evaluateParticipant(TournamentParticipant participant, int points, int oppPoints,  int cards, float modifier){
        float participantScore = points - oppPoints - doubles;
        participantScore = participantScore * modifier;
        participant.addScore(participantScore);
        participant.addDoubles(doubles);
        participant.addCards(cards);
    }
}
