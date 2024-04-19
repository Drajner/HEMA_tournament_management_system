package drajner.hetman.services;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TournamentParticipant {
    private Person person;
    private CompetitorStatus status;
    private float score;
    private float wins;
    private float doubles;
    private int cards;
    private int ranking;

    public TournamentParticipant(){}

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

    public String getName(){return (person.getName() + " " + person.getSurname());}

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
