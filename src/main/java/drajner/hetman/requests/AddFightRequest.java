package drajner.hetman.requests;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddFightRequest {
    Long firstParticipantId;
    Long secondParticipantId;
}
