package drajner.hetman.services;

public class Fight {

    private Competitor firstCompetitor;
    private Competitor secondCompetitor;
    private int firstCompetitorScore;
    private int secondCompetitorScore;
    private int doubles;
    private FightStatus status;
    private Competitor winner;

    public Fight(Competitor firstCompetitor, Competitor secondCompetitor){
        this.firstCompetitor = firstCompetitor;
        this.secondCompetitor = secondCompetitor;
        this.firstCompetitorScore = 0;
        this.secondCompetitorScore = 0;
        this.doubles = 0;
        this.status = FightStatus.PENDING;
        this.winner = null;
    }

    public Fight(Competitor firstCompetitor, Competitor secondCompetitor, int firstCompetitorScore, int secondCompetitorScore, int doubles, FightStatus status, Competitor winner){
        this.firstCompetitor = firstCompetitor;
        this.secondCompetitor = secondCompetitor;
        this.firstCompetitorScore = firstCompetitorScore;
        this.secondCompetitorScore = secondCompetitorScore;
        this.doubles = doubles;
        this.status = status;
        this.winner = winner;
    }

    public Competitor getFirstCompetitor() {
        return firstCompetitor;
    }

    public Competitor getSecondCompetitor() {
        return secondCompetitor;
    }

    public int getFirstCompetitorScore() {
        return firstCompetitorScore;
    }

    public int getSecondCompetitorScore() {
        return secondCompetitorScore;
    }

    public int getDoubles() {
        return doubles;
    }

    public FightStatus getStatus() {
        return status;
    }

    public Competitor getWinner() {
        return winner;
    }

    public void setFirstCompetitor(Competitor firstCompetitor) {
        this.firstCompetitor = firstCompetitor;
    }

    public void setFirstCompetitorScore(int firstCompetitorScore) {
        this.firstCompetitorScore = firstCompetitorScore;
    }

    public void setSecondCompetitor(Competitor secondCompetitor) {
        this.secondCompetitor = secondCompetitor;
    }

    public void setSecondCompetitorScore(int secondCompetitorScore) {
        this.secondCompetitorScore = secondCompetitorScore;
    }

    public void setStatus(FightStatus status) {
        this.status = status;
    }

    public void setDoubles(int doubles) {
        this.doubles = doubles;
    }

    public void setWinner(Competitor winner) {
        this.winner = winner;
    }
}