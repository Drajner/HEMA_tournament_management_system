package drajner.hetman.services;

import drajner.hetman.errors.UnfinishedFightException;
import drajner.hetman.errors.WrongAmountException;

import java.util.ArrayList;

public class GroupFinals extends Group{

    FinalsTreeNode finalFight;
    FinalsTreeNode thirdPlaceFight;

    public GroupFinals(){super();}

    public GroupFinals(ArrayList<TournamentParticipant> tournamentParticipants){
       super(tournamentParticipants);
    }

    public void evaluateGroup(float modifier) throws UnfinishedFightException {

    }

    public void autoGenerateFights() throws WrongAmountException{
        if(groupParticipants.size() < 4) throw new WrongAmountException("There need to be 4 or more participants in finals");
        finalFight = new FinalsTreeNode();
        thirdPlaceFight = new FinalsTreeNode(true);
        finalFight.setUpTree(groupParticipants);
        thirdPlaceFight.setFirstChildNode(finalFight.getFirstChildNode());
        thirdPlaceFight.setSecondChildNode(finalFight.getSecondChildNode());
    }
}
