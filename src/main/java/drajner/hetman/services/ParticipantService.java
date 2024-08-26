package drajner.hetman.services;

import drajner.hetman.entities.TournamentEntity;
import drajner.hetman.entities.TournamentParticipantEntity;
import drajner.hetman.repositories.TournamentParticipantsRepo;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@Log4j2
public class ParticipantService {

    @Autowired
    TournamentParticipantsRepo tournamentParticipantsRepo;

    public TournamentParticipantEntity searchForParticipant(Long participantId){
        log.info(String.format("Searching for participant of id: '%s'", participantId));
        Optional<TournamentParticipantEntity> search = tournamentParticipantsRepo.findById(participantId);
        if (search.isEmpty()) throw new NoSuchElementException("No participant of this ID exists");
        return search.get();
    }

    public void disqualify(Long participantId){
        TournamentParticipantEntity participant = searchForParticipant(participantId);
        participant.disqualify();
        tournamentParticipantsRepo.save(participant);
        log.info(String.format("Participant '%s' is now disqualified", participantId));
    }

    public void compete(Long participantId){
        TournamentParticipantEntity participant = searchForParticipant(participantId);
        participant.compete();
        tournamentParticipantsRepo.save(participant);
        log.info(String.format("Participant '%s' is now competing", participantId));
    }

    public void eliminate(Long participantId){
        TournamentParticipantEntity participant = searchForParticipant(participantId);
        participant.eliminate();
        tournamentParticipantsRepo.save(participant);
        log.info(String.format("Participant '%s' is now eliminated", participantId));
    }
}
