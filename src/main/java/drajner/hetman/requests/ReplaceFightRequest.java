package drajner.hetman.requests;

import drajner.hetman.services.FightStatus;
import lombok.Getter;

@Getter
public class ReplaceFightRequest {

    Long fightId;
    Long firstParticipantId;
    Long secondParticipantId;
    int firstParticipantPoints;
    int secondParticipantPoints;
    int firstParticipantCards;
    int secondParticipantCards;
    int doubles;
    FightStatus status;
    Long winner;

}
