package drajner.hetman.services;

import drajner.hetman.errors.UnfinishedFightException;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;

import java.util.ArrayList;

@Log4j2
@Getter
@Setter
public class FinalsTreeNode {
    Fight nodeFight;
    FinalsTreeNode firstChildNode;
    FinalsTreeNode secondChildNode;
    boolean reverted = false;

    FinalsTreeNode(){nodeFight = new Fight();}
    FinalsTreeNode(boolean reverted){
        nodeFight = new Fight();
        this.reverted = reverted;
    }

    void fillNode() throws UnfinishedFightException {
        if(nodeFight.getStatus() != FightStatus.EVALUATED) {
            if (nodeFight.getStatus() == FightStatus.PENDING) {
                firstChildNode.fillNode();
                if (firstChildNode.getNodeFight().getStatus() == FightStatus.FINISHED || firstChildNode.getNodeFight().getStatus() == FightStatus.EVALUATED) {
                    firstChildNode.getNodeFight().evaluateFight(1); //to be solved
                    if (!reverted) nodeFight.setFirstParticipant(firstChildNode.getNodeFight().findLoser());
                    else nodeFight.setFirstParticipant(firstChildNode.getNodeFight().getWinner());
                }
                secondChildNode.fillNode();
                if (secondChildNode.getNodeFight().getStatus() == FightStatus.FINISHED || firstChildNode.getNodeFight().getStatus() == FightStatus.EVALUATED) {
                    secondChildNode.getNodeFight().evaluateFight(1);
                    if (!reverted) nodeFight.setSecondParticipant(secondChildNode.getNodeFight().findLoser());
                    else nodeFight.setSecondParticipant(secondChildNode.getNodeFight().getWinner());
                }
            } else if(nodeFight.getStatus() == FightStatus.FINISHED){
                nodeFight.evaluateFight(1);
            }
        }
    }

    void setUpTree(ArrayList<TournamentParticipant> nodeParticipants){
        if(nodeParticipants.size() == 2){
            nodeFight = new Fight(nodeParticipants.get(0), nodeParticipants.get(1));
            log.info(String.format("Set up elimination fight for '%s' and '%s'." , nodeParticipants.get(0).getName(), nodeParticipants.get(1).getName()));
        }
        else if(nodeParticipants.size() == 3){
            nodeFight = new Fight(nodeParticipants.get(0));
            ArrayList<TournamentParticipant> newNodeParticipants = new ArrayList<>();
            newNodeParticipants.add(nodeParticipants.get(1));
            newNodeParticipants.add(nodeParticipants.get(2));
            firstChildNode = new FinalsTreeNode();
            firstChildNode.setUpTree(newNodeParticipants);
            log.info(String.format("Set up awaiting elimination fight for '%s'." , nodeParticipants.get(0).getName()));
        }
        else{
            boolean revert = false;
            ArrayList<TournamentParticipant> firstChildNodeParticipants = new ArrayList<>();
            ArrayList<TournamentParticipant> secondChildNodeParticipants = new ArrayList<>();
            for(int i = 0; i < nodeParticipants.size();i++){
                if(i % 4 == 1) revert = true;
                if(i % 4 == 3) revert = false;
                if(!revert) firstChildNodeParticipants.add(nodeParticipants.get(i));
                else secondChildNodeParticipants.add(nodeParticipants.get(i));
            }
            firstChildNode = new FinalsTreeNode();
            firstChildNode.setUpTree(firstChildNodeParticipants);
            secondChildNode = new FinalsTreeNode();
            secondChildNode.setUpTree(secondChildNodeParticipants);
            log.info(String.format("Set up node for '%s' people.", nodeParticipants.size()));
        }
    }
}
