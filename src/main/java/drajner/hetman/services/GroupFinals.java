package drajner.hetman.services;

import drajner.hetman.errors.UnfinishedFightException;
import drajner.hetman.errors.WrongAmountException;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;

import java.util.ArrayList;

@Log4j2
@Getter
@Setter
public class GroupFinals extends Group{

    FinalsTreeNode finalFight;
    FinalsTreeNode thirdPlaceFight;

    public GroupFinals(){super();}

    public GroupFinals(ArrayList<TournamentParticipant> tournamentParticipants){
       super(tournamentParticipants);
    }

    public void evaluateGroup(float modifier) throws UnfinishedFightException {
        log.info("Evaluating current tree.");
        finalFight.fillNode();
        thirdPlaceFight.fillNode();
        log.info("Evaluated current tree.");
    }

    public void autoGenerateFights() throws WrongAmountException{
        if(groupParticipants.size() < 4) throw new WrongAmountException("There need to be 4 or more participants in finals");
        finalFight = new FinalsTreeNode();
        thirdPlaceFight = new FinalsTreeNode(true);
        finalFight.setUpTree(groupParticipants);
        thirdPlaceFight.setFirstChildNode(finalFight.getFirstChildNode());
        thirdPlaceFight.setSecondChildNode(finalFight.getSecondChildNode());
        log.info("Auto generated finals.");
    }
}
