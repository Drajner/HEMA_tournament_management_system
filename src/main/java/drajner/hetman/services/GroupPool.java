package drajner.hetman.services;

import java.util.ArrayList;

public class GroupPool extends Group{

    private int fightsPerCompetitor;

    public void evaluateGroup(float modifier){
        for(Fight fight: fights){
            if(fight.getStatus() == FightStatus.FINISHED){
                fight.evaluateFight(modifier);
            }
        }
    }


    public void autoGenerateFights(){
        int groupSize = groupParticipants.size();
        for(int round = 0; round < fightsPerCompetitor; round++){
            for(int i = 0; i < groupSize / 2 ; i++){
                TournamentParticipant firstFighter = groupParticipants.get(i);
                TournamentParticipant secondFighter = groupParticipants.get(groupSize - 1 - i);
                Fight fightInGroup = new Fight(firstFighter, secondFighter);
            }

            TournamentParticipant lastFighter = groupParticipants.get(groupSize - 1);
            for(int i = groupSize - 1; i > 1 ; i--){
                groupParticipants.set(i, groupParticipants.get(i - 1));
            }
            groupParticipants.set(1, lastFighter);
        }
    }
}
