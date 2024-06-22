package drajner.hetman.services;

import drajner.hetman.entities.TournamentEntity;
import drajner.hetman.entities.TournamentParticipantEntity;
import drajner.hetman.repositories.TournamentParticipantsRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class ParticipantService {

    @Autowired
    TournamentParticipantsRepo tournamentParticipantsRepo;

    public TournamentParticipantEntity searchForParticipant(Long participantId){
        Optional<TournamentParticipantEntity> search = tournamentParticipantsRepo.findById(participantId);
        if (search.isEmpty()) throw new NoSuchElementException("No participant of this ID exists");
        return search.get();
    }

    public void disqualify(Long participantId){
        TournamentParticipantEntity participant = searchForParticipant(participantId);
        participant.disqualify();
        tournamentParticipantsRepo.save(participant);
    }

    public void compete(Long participantId){
        TournamentParticipantEntity participant = searchForParticipant(participantId);
        participant.compete();
        tournamentParticipantsRepo.save(participant);
    }

    public void eliminate(Long participantId){
        TournamentParticipantEntity participant = searchForParticipant(participantId);
        participant.eliminate();
        tournamentParticipantsRepo.save(participant);
    }


}
