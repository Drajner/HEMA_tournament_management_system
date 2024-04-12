package drajner.hetman.services;

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

    public TournamentParticipant getFirstCompetitor() {
        return firstParticipant;
    }

    public TournamentParticipant getSecondCompetitor() {
        return secondParticipant;
    }

    public int getFirstParticipantPoints() {
        return firstParticipantPoints;
    }

    public int getSecondParticipantPoints() {
        return secondParticipantPoints;
    }

    public int getDoubles() {
        return doubles;
    }

    public FightStatus getStatus() {
        return status;
    }

    public TournamentParticipant getWinner() {
        return winner;
    }

    public void setFirstCompetitor(TournamentParticipant firstParticipant) {
        this.firstParticipant = firstParticipant;
    }

    public void setFirstParticipantPoints(int firstParticipantPoints) {
        this.firstParticipantPoints = firstParticipantPoints;
    }

    public void setSecondCompetitor(TournamentParticipant secondParticipant) {
        this.secondParticipant = secondParticipant;
    }

    public void setSecondParticipantPoints(int secondParticipantPoints) {
        this.secondParticipantPoints = secondParticipantPoints;
    }

    public void setStatus(FightStatus status) {
        this.status = status;
    }

    public void setDoubles(int doubles) {
        this.doubles = doubles;
    }

    public void setWinner(TournamentParticipant winner) {
        this.winner = winner;
    }

    public void evaluateFight(float modifier){
        if(status == FightStatus.FINISHED) {
            evaluateParticipant(firstParticipant, firstParticipantPoints, secondParticipantPoints, firstParticipantCards, modifier);
            evaluateParticipant(secondParticipant, secondParticipantPoints, firstParticipantPoints, secondParticipantCards, modifier);
            if (winner != null) {
                winner.addWin();
            }
        }else{
            System.out.println("Unfinished fight");
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
