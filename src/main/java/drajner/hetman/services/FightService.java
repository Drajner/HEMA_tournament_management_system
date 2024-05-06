package drajner.hetman.services;

import drajner.hetman.entities.FightEntity;
import drajner.hetman.entities.TournamentEntity;
import drajner.hetman.errors.UnfinishedFightException;
import drajner.hetman.repositories.FightRepo;
import drajner.hetman.repositories.TournamentParticipantsRepo;
import drajner.hetman.entities.TournamentParticipantEntity;
import lombok.extern.log4j.Log4j2;

import java.util.NoSuchElementException;
import java.util.Optional;

@Log4j2
public class FightService {

    static FightRepo fightRepo;

    static TournamentParticipantsRepo tournamentParticipantsRepo;

    public static void evaluateFight(Long id, float modifier) throws UnfinishedFightException{
        Optional<FightEntity> search = fightRepo.findById(id);
        if (search.isEmpty()) throw new NoSuchElementException("No fight of this ID exists");
        FightEntity selectedFight = search.get();
        evaluateFight(selectedFight, modifier);
    }


    public static void evaluateFight(FightEntity fight, float modifier) throws UnfinishedFightException {
        if(fight.getStatus() == FightStatus.FINISHED) {
            evaluateParticipant(fight.getFirstParticipant(),
                                fight.getFirstParticipantPoints(),
                                fight.getSecondParticipantPoints(),
                                fight.getDoubles(),
                                fight.getFirstParticipantCards(), modifier);
            evaluateParticipant(fight.getSecondParticipant(),
                                fight.getSecondParticipantPoints(),
                                fight.getFirstParticipantPoints(),
                                fight.getDoubles(),
                                fight.getSecondParticipantCards(), modifier);
            if (fight.getWinner() != null) {
                fight.getWinner().addWin();
            }
            fight.setStatus(FightStatus.EVALUATED);
            fightRepo.save(fight);
            log.info(String.format("Evaluated fight between '%s' and '%s'.", fight.getFirstParticipant().getName(), fight.getSecondParticipant().getName()));
        }else if(fight.getStatus() == FightStatus.PENDING){
            throw new UnfinishedFightException("This fight is not yet finished!");
        }
    }

    public static void evaluateParticipant(TournamentParticipantEntity participant, int points, int oppPoints, int doubles,  int cards, float modifier){
        float participantScore = points - oppPoints - doubles;
        participantScore = participantScore * modifier;
        participant.addScore(participantScore);
        participant.addDoubles(doubles);
        participant.addCards(cards);
        tournamentParticipantsRepo.save(participant);
        log.info(String.format("Scores added to '%s' ranking.", participant.getName()));
    }
}
