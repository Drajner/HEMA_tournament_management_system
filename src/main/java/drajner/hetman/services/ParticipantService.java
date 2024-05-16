package drajner.hetman.services;

import drajner.hetman.entities.TournamentEntity;
import drajner.hetman.entities.TournamentParticipantEntity;
import drajner.hetman.repositories.TournamentParticipantsRepo;

import java.util.NoSuchElementException;
import java.util.Optional;

public class ParticipantService {

    static TournamentParticipantsRepo tournamentParticipantsRepo;

    public static TournamentParticipantEntity searchForParticipant(Long participantId){
        Optional<TournamentParticipantEntity> search = tournamentParticipantsRepo.findById(participantId);
        if (search.isEmpty()) throw new NoSuchElementException("No participant of this ID exists");
        return search.get();
    }

    public static void disqualify(Long participantId){
        TournamentParticipantEntity participant = searchForParticipant(participantId);
        participant.disqualify();
        tournamentParticipantsRepo.save(participant);
    }

    public static void compete(Long participantId){
        TournamentParticipantEntity participant = searchForParticipant(participantId);
        participant.compete();
        tournamentParticipantsRepo.save(participant);
    }

    public static void eliminate(Long participantId){
        TournamentParticipantEntity participant = searchForParticipant(participantId);
        participant.eliminate();
        tournamentParticipantsRepo.save(participant);
    }


}
