package drajner.hetman.requests;

import lombok.Getter;

@Getter
public class FightDetails {
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
