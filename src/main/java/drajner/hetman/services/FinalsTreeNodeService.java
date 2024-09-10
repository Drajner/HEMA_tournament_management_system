package drajner.hetman.services;

import drajner.hetman.entities.FightEntity;
import drajner.hetman.entities.FinalsTreeNodeEntity;
import drajner.hetman.entities.TournamentParticipantEntity;
import drajner.hetman.errors.UnfinishedFightException;
import drajner.hetman.repositories.FightRepo;
import drajner.hetman.repositories.FinalsTreeNodeRepo;
import drajner.hetman.status.FightStatus;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Log4j2
public class FinalsTreeNodeService {

    @Autowired
    FinalsTreeNodeRepo finalsTreeNodeRepo;

    @Autowired
    FightRepo fightRepo;

    @Autowired
    FightService fightService;

    public void fillNode(FinalsTreeNodeEntity selectedNode) throws UnfinishedFightException {


        if(selectedNode.getFight().getStatus() != FightStatus.EVALUATED) {
            if (selectedNode.getFight().getStatus() == FightStatus.PENDING) {
                System.out.println(selectedNode.getFirstChildNode().getFight().getStatus());
                System.out.println(selectedNode.getSecondChildNode().getFight().getStatus());
                System.out.println(selectedNode.getFirstChildNode() != null);
                System.out.println(selectedNode.getSecondChildNode() != null);

                if(selectedNode.getFirstChildNode() != null){
                    FinalsTreeNodeEntity firstChildNode = selectedNode.getFirstChildNode();
                    fillNode(selectedNode.getFirstChildNode());
                    if (firstChildNode.getFight().getStatus() == FightStatus.FINISHED || firstChildNode.getFight().getStatus() == FightStatus.EVALUATED) {
                        fightService.evaluateFight(firstChildNode.getFight(), 1);
                        if (selectedNode.isReverted()) selectedNode.getFight().setFirstParticipant(firstChildNode.getFight().findLoser());
                        else selectedNode.getFight().setFirstParticipant(firstChildNode.getFight().getWinner());
                    }

                }

                if(selectedNode.getSecondChildNode() != null){
                    FinalsTreeNodeEntity secondChildNode = selectedNode.getSecondChildNode();
                    fillNode(selectedNode.getSecondChildNode());
                    if (secondChildNode.getFight().getStatus() == FightStatus.FINISHED || secondChildNode.getFight().getStatus() == FightStatus.EVALUATED) {
                        fightService.evaluateFight(secondChildNode.getFight(), 1);
                        if (selectedNode.isReverted()) selectedNode.getFight().setSecondParticipant(secondChildNode.getFight().findLoser());
                        else selectedNode.getFight().setSecondParticipant(secondChildNode.getFight().getWinner());
                    }

                }

            } else if(selectedNode.getFight().getStatus() == FightStatus.FINISHED){
                fightService.evaluateFight(selectedNode.getFight(), 1);
            }
            finalsTreeNodeRepo.save(selectedNode);
        }
    }

    public void setUpTree(FinalsTreeNodeEntity node, List<TournamentParticipantEntity> nodeParticipants){
        if(nodeParticipants.size() == 2){
            node.setFight(new FightEntity(nodeParticipants.get(0), nodeParticipants.get(1)));
            fightRepo.save(node.getFight());
            finalsTreeNodeRepo.save(node);
            log.info(String.format("Set up elimination fight for '%s' and '%s'. Fight id: '%s'" ,
                                    nodeParticipants.get(0).getName(),
                                    nodeParticipants.get(1).getName(),
                                    node.getFight().getId()));
        }
        else if(nodeParticipants.size() == 3){
            node.setFight(new FightEntity(nodeParticipants.get(0)));
            List<TournamentParticipantEntity> newNodeParticipants = new ArrayList<>();
            newNodeParticipants.add(nodeParticipants.get(1));
            newNodeParticipants.add(nodeParticipants.get(2));

            fightRepo.save(node.getFight());
            finalsTreeNodeRepo.save(node);

            node.setFirstChildNode(new FinalsTreeNodeEntity());
            setUpTree(node.getFirstChildNode(), newNodeParticipants);

            fightRepo.save(node.getFight());
            finalsTreeNodeRepo.save(node.getFirstChildNode());
            finalsTreeNodeRepo.save(node);
            log.info(String.format("Set up awaiting elimination fight for '%s'. Fight id: '%s'" ,
                                    nodeParticipants.get(0).getName(), node.getFight().getId()));
        }
        else{
            boolean revert = false;
            ArrayList<TournamentParticipantEntity> firstChildNodeParticipants = new ArrayList<>();
            ArrayList<TournamentParticipantEntity> secondChildNodeParticipants = new ArrayList<>();
            for(int i = 0; i < nodeParticipants.size();i++){
                if(i % 4 == 1) revert = true;
                if(i % 4 == 3) revert = false;
                if(!revert) firstChildNodeParticipants.add(nodeParticipants.get(i));
                else secondChildNodeParticipants.add(nodeParticipants.get(i));
            }

            node.setFirstChildNode(new FinalsTreeNodeEntity());
            finalsTreeNodeRepo.save(node.getFirstChildNode());
            setUpTree(node.getFirstChildNode(), firstChildNodeParticipants);
            finalsTreeNodeRepo.save(node.getFirstChildNode());

            node.setSecondChildNode(new FinalsTreeNodeEntity());
            finalsTreeNodeRepo.save(node.getFirstChildNode());
            setUpTree(node.getSecondChildNode(), secondChildNodeParticipants);
            finalsTreeNodeRepo.save(node.getSecondChildNode());

            node.setFight(new FightEntity());
            fightRepo.save(node.getFight());
            finalsTreeNodeRepo.save(node);

            log.info(String.format("Set up node for '%s' people. Empty fight id: '%s'",
                                    nodeParticipants.size(), node.getFight().getId()));
        }
    }

    public void purgeTree(FinalsTreeNodeEntity node){
        finalsTreeNodeRepo.deleteById(node.getNodeId());
        log.info(String.format("Deleted tree starting from node '%s'", node.getNodeId()));
    }
    public void purgeFights(FinalsTreeNodeEntity node){
        FinalsTreeNodeEntity firstChildNode = node.getFirstChildNode();
        FinalsTreeNodeEntity secondChildNode = node.getSecondChildNode();
        Long fightId = node.getFight().getId();
        node.setFight(null);
        finalsTreeNodeRepo.save(node);
        fightRepo.deleteById(fightId);

        if(firstChildNode != null){
            if(firstChildNode.getFight() != null) purgeFights(node.getFirstChildNode());
        }
        if(secondChildNode != null){
            if(secondChildNode.getFight() != null) purgeFights(node.getSecondChildNode());
        }
        log.info(String.format("Purging fight '%s' from node '%s'", fightId, node.getNodeId()));
    }
}
