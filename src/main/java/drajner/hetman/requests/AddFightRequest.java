package drajner.hetman.requests;

import lombok.Getter;

@Getter
public class AddFightRequest {
    Long firstParticipantId;
    Long secondParticipantId;
}
