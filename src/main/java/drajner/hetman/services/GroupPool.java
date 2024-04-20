package drajner.hetman.services;

import drajner.hetman.errors.UnfinishedFightException;
import drajner.hetman.errors.WrongAmountException;
import lombok.extern.log4j.Log4j2;

import java.util.ArrayList;

@Log4j2
public class GroupPool extends Group{

    public void evaluateGroup(float modifier) throws UnfinishedFightException {
        log.info("Evaluating group.");
        for(Fight fight: fights){
            if(fight.getStatus() == FightStatus.FINISHED){
                fight.evaluateFight(modifier);
                fight.setStatus(FightStatus.EVALUATED);
            }
        }
        log.info("Evaluated group.");
    }

    public void autoGenerateFights() throws WrongAmountException {
        int groupSize = groupParticipants.size();
        ArrayList<TournamentParticipant> tempParticipantList = new ArrayList<>(groupParticipants);
        int roundAmount = groupSize;
        if(roundAmount / 2 == 0) roundAmount -= 1;
        for (int round = 0; round < (roundAmount - 1); round++) {
            for (int i = 0; i < (groupSize / 2); i++) {
                TournamentParticipant firstFighter = tempParticipantList.get(i);
                TournamentParticipant secondFighter = tempParticipantList.get(groupSize - 1 - i);
                Fight fightInGroup = new Fight(firstFighter, secondFighter);
                fights.add(fightInGroup);
            }

            TournamentParticipant lastFighter = tempParticipantList.get(groupSize - 1);
            for (int i = groupSize - 1; i > 1; i--) {
                tempParticipantList.set(i, tempParticipantList.get(i - 1));
            }
            tempParticipantList.set(1, lastFighter);
        }
        log.info("Auto generated group fights.");
    }
}
