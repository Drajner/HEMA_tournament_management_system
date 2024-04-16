package drajner.hetman.services;

import drajner.hetman.errors.UnfinishedFightException;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@Setter
public class FinalsTreeNode {
    Fight nodeFight;
    FinalsTreeNode firstChildNode;
    FinalsTreeNode secondChildNode;
    boolean reverted = false;

    FinalsTreeNode(){};
    FinalsTreeNode(boolean reverted){
        this.reverted = reverted;
    }

    void fillNode() throws UnfinishedFightException {
        if (firstChildNode.getNodeFight().getStatus() == FightStatus.FINISHED) {
            firstChildNode.getNodeFight().evaluateFight(1); //to be solved
            if(!reverted) nodeFight.setFirstParticipant(firstChildNode.getNodeFight().getWinner());
            else nodeFight.setFirstParticipant(firstChildNode.getNodeFight().getWinner());
        }
        if (secondChildNode.getNodeFight().getStatus() == FightStatus.FINISHED) {
            secondChildNode.getNodeFight().evaluateFight(1);
            if(!reverted) nodeFight.setSecondParticipant(secondChildNode.getNodeFight().getWinner());
            else nodeFight.setSecondParticipant(secondChildNode.getNodeFight().getWinner());
        }
    }

    void setUpTree(ArrayList<TournamentParticipant> nodeParticipants){
        if(nodeParticipants.size() == 2){
            nodeFight = new Fight(nodeParticipants.get(0), nodeParticipants.get(1));
        }
        else if(nodeParticipants.size() == 3){
            nodeFight = new Fight(nodeParticipants.get(0));
            ArrayList<TournamentParticipant> newNodeParticipants = new ArrayList<>();
            newNodeParticipants.add(nodeParticipants.get(1));
            newNodeParticipants.add(nodeParticipants.get(2));
            firstChildNode = new FinalsTreeNode();
            firstChildNode.setUpTree(newNodeParticipants);
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
        }
    }
}
