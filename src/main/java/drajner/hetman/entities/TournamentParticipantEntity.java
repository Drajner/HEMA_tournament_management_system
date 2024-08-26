package drajner.hetman.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import drajner.hetman.status.CompetitorStatus;
import drajner.hetman.requests.Person;
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

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name="tournamentId", nullable=false)
    TournamentEntity tournament;


    @JsonIgnore
    @ManyToMany
    @JoinTable(
            name="groupParticipation",
            joinColumns=@JoinColumn(name="participantId"),
            inverseJoinColumns=@JoinColumn(name="groupId")
    )
    List<GroupEntity> groupParticipations;


    public TournamentParticipantEntity(){}

    public TournamentParticipantEntity(Person person){
        this.name = person.getName();
        this.surname = person.getSurname();
        this.club = person.getTeamName();
        this.status = CompetitorStatus.COMPETING;
        this.score = 0;
        this.wins = 0;
        this.doubles = 0;
        this.cards = 0;
        this.ranking = 0;
    }

    public String getFullName(){return (name + " " + surname);}

    public void addScore(float score) {
        this.score += score;
    }

    public void addWin() {
        this.wins += 1;
    }

    public void disqualify(){
        this.status = CompetitorStatus.DISQUALIFIED;
    }

    public void compete(){this.status = CompetitorStatus.COMPETING;}

    public void eliminate(){this.status = CompetitorStatus.ELIMINATED;}

    public void addDoubles(float doubles){
        this.doubles += doubles;
    }

    public void addCards(int cards){
        this.cards += cards;
    }

}
