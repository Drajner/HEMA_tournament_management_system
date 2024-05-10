package drajner.hetman.services;

import drajner.hetman.entities.GroupEntity;
import drajner.hetman.entities.TournamentEntity;
import drajner.hetman.entities.TournamentParticipantEntity;
import drajner.hetman.errors.DuplicateException;
import drajner.hetman.errors.UnfinishedFightException;
import drajner.hetman.errors.WrongAmountException;
import drajner.hetman.repositories.FightRepo;
import drajner.hetman.repositories.GroupRepo;
import drajner.hetman.entities.FightEntity;
import drajner.hetman.repositories.TournamentParticipantsRepo;
import lombok.extern.log4j.Log4j2;

import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Optional;

@Log4j2
public class GroupService {

    GroupRepo groupRepo;
    FightRepo fightRepo;

    TournamentParticipantsRepo tournamentParticipantsRepo;

    public GroupEntity searchForGroup(Long groupId){
        Optional<GroupEntity> search = groupRepo.findById(groupId);
        if (search.isEmpty()) throw new NoSuchElementException("No tournament of this ID exists");
        return search.get();
    }



    public void autoGenerateFights(Long groupId) throws WrongAmountException {

        GroupEntity selectedGroup = searchForGroup(groupId);

        int groupSize = selectedGroup.getGroupParticipants().size();
        ArrayList<TournamentParticipantEntity> tempParticipantList = new ArrayList<>(selectedGroup.getGroupParticipants());
        int roundAmount = groupSize;
        if(roundAmount / 2 == 0) roundAmount -= 1;
        for (int round = 0; round < (roundAmount - 1); round++) {
            for (int i = 0; i < (groupSize / 2); i++) {
                TournamentParticipantEntity firstFighter = tempParticipantList.get(i);
                TournamentParticipantEntity secondFighter = tempParticipantList.get(groupSize - 1 - i);
                FightEntity fightInGroup = new FightEntity(firstFighter, secondFighter);
                selectedGroup.getGroupFights().add(fightInGroup);
                fightRepo.save(fightInGroup);
            }

            TournamentParticipantEntity lastFighter = tempParticipantList.get(groupSize - 1);
            for (int i = groupSize - 1; i > 1; i--) {
                tempParticipantList.set(i, tempParticipantList.get(i - 1));
            }
            tempParticipantList.set(1, lastFighter);
        }
        groupRepo.save(selectedGroup);
        log.info("Auto generated group fights.");
    }

    public void evaluateGroup(Long groupId, float modifier) throws UnfinishedFightException {

        GroupEntity selectedGroup = searchForGroup(groupId);

        log.info("Evaluating group.");
        for(FightEntity fight: selectedGroup.getGroupFights()){
            if(fight.getStatus() == FightStatus.FINISHED){

                FightService.evaluateFight(fight, modifier);
                fight.setStatus(FightStatus.EVALUATED);
                fightRepo.save(fight);

            }
        }
        groupRepo.save(selectedGroup);
        log.info("Evaluated group.");
    }

    public void addFight(Long groupId, FightEntity fight){

        GroupEntity selectedGroup = searchForGroup(groupId);

        selectedGroup.getGroupFights().add(fight);
        groupRepo.save(selectedGroup);
        log.info(String.format("Added fight between '%s' and '%s' to group.", fight.getFirstParticipant().getName(), fight.getSecondParticipant().getName()));
    }

    public void addParticipant(Long groupId, TournamentParticipantEntity participant) throws DuplicateException {

        GroupEntity selectedGroup = searchForGroup(groupId);

        selectedGroup.getGroupParticipants().add(participant);
        groupRepo.save(selectedGroup);
        log.info(String.format("Added '%s' to group.", participant.getName()));
    }

    public void deleteParticipant(Long groupId, Long participantId){

        GroupEntity selectedGroup = searchForGroup(groupId);

        Optional<TournamentParticipantEntity> search = tournamentParticipantsRepo.findById(participantId);
        if (search.isEmpty()) throw new NoSuchElementException("No participant of this ID exists");
        TournamentParticipantEntity participant = search.get();

        if(selectedGroup.getGroupParticipants().contains(participant)){
            selectedGroup.getGroupParticipants().remove(participant);
            groupRepo.save(selectedGroup);
        }else{
            throw new NoSuchElementException("This participant does not belong to this group.");
        }

        log.info(String.format("Removing '%s' from group.", participant.getName()));
    }

}
