package drajner.hetman.requests;

import drajner.hetman.entities.FightEntity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FightReport {

    private String username;
    Long fightId;
    Long firstParticipantId;
    Long secondParticipantId;
    int firstParticipantPoints;
    int secondParticipantPoints;
    int firstParticipantCards;
    int secondParticipantCards;
    int doubles;
    Long winner;

}
