package drajner.hetman.services;

public class TournamentParticipant {
    private Person person;
    private CompetitorStatus status;
    private float score;
    private float wins;
    private float doubles;
    private int cards;
    private int ranking;


    public TournamentParticipant(Person person){
        this.person = person;
        this.status = CompetitorStatus.COMPETING;
        this.score = 0;
        this.wins = 0;
        this.doubles = 0;
        this.cards = 0;
    }

    public TournamentParticipant(Person person, CompetitorStatus status, float score, float wins, float doubles, int cards){
        this.person = person;
        this.status = status;
        this.score = score;
        this.wins = wins;
        this.doubles = doubles;
        this.cards = cards;
    }

    public Person getCompetitor() {
        return person;
    }

    public String getName(){return (person.getName() + " " + person.getSurname());}

    public CompetitorStatus getStatus() {
        return status;
    }

    public float getScore() {
        return score;
    }

    public float getDoubles() {
        return doubles;
    }

    public float getWins() {
        return wins;
    }

    public int getCards() {
        return cards;
    }

    public int getRanking() {
        return ranking;
    }

    public void setRanking(int ranking) {
        this.ranking = ranking;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public void setStatus(CompetitorStatus status) {
        this.status = status;
    }

    public void setScore(float score) {
        this.score = score;
    }

    public void addScore(float score) {
        this.score += score;
    }

    public void addWin() {
        this.wins += 1;
    }

    public void disqualify(){
        this.status = CompetitorStatus.DISQUALIFIED;
    }

    public void addDoubles(float doubles){
        this.doubles = doubles;
    }

    public void addCards(int cards){
        this.cards += cards;
    }
}
