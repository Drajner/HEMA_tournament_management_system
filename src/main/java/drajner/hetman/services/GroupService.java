package drajner.hetman.services;

import drajner.hetman.entities.GroupEntity;
import drajner.hetman.entities.TournamentEntity;
import drajner.hetman.entities.TournamentParticipantEntity;
import drajner.hetman.errors.DuplicateException;
import drajner.hetman.errors.ImpossibleFightException;
import drajner.hetman.errors.UnfinishedFightException;
import drajner.hetman.errors.WrongAmountException;
import drajner.hetman.repositories.FightRepo;
import drajner.hetman.repositories.GroupRepo;
import drajner.hetman.entities.FightEntity;
import drajner.hetman.repositories.TournamentParticipantsRepo;
import drajner.hetman.requests.AddFightRequest;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@Log4j2
public class GroupService {

    @Autowired
    GroupRepo groupRepo;
    @Autowired
    FightRepo fightRepo;
    @Autowired
    TournamentParticipantsRepo tournamentParticipantsRepo;

    @Autowired
    ParticipantService participantService;
    @Autowired
    FightService fightService;

    public GroupEntity searchForGroup(Long groupId){
        Optional<GroupEntity> search = groupRepo.findById(groupId);
        if (search.isEmpty()) throw new NoSuchElementException("No tournament of this ID exists");
        return search.get();
    }

    public void autoGenerateFights(Long groupId) throws WrongAmountException {

        GroupEntity selectedGroup = searchForGroup(groupId);
        ArrayList<TournamentParticipantEntity> tempParticipantList = new ArrayList<>(selectedGroup.getGroupParticipants());

        if(selectedGroup.getGroupParticipants().size() % 2 == 1) tempParticipantList.add(null);
        int groupSize = tempParticipantList.size();

        for (int round = 0; round < groupSize -1; round++) {
            for (int i = 0; i < (groupSize / 2); i++) {
                TournamentParticipantEntity firstFighter = tempParticipantList.get(i);
                TournamentParticipantEntity secondFighter = tempParticipantList.get(groupSize - 1 - i);
                if(firstFighter == null || secondFighter == null) continue;
                FightEntity fightInGroup = new FightEntity(firstFighter, secondFighter);
                fightInGroup.setGroup(selectedGroup);
                selectedGroup.getGroupFights().add(fightInGroup);
                fightRepo.save(fightInGroup);
                tournamentParticipantsRepo.save(firstFighter);
                tournamentParticipantsRepo.save(secondFighter);
            }

            TournamentParticipantEntity swappedFighter = tempParticipantList.remove(1);
            tempParticipantList.add(swappedFighter);
        }
    }

    public void evaluateGroup(Long groupId, float modifier) throws UnfinishedFightException {

        GroupEntity selectedGroup = searchForGroup(groupId);

        log.info("Evaluating group.");
        for(FightEntity fight: selectedGroup.getGroupFights()){
            if(fight.getStatus() == FightStatus.FINISHED){

                fightService.evaluateFight(fight, modifier);
                fight.setStatus(FightStatus.EVALUATED);
                fightRepo.save(fight);

            }
        }
        groupRepo.save(selectedGroup);
        log.info("Evaluated group.");
    }

    public void addFight(Long groupId, AddFightRequest addFightRequest) throws ImpossibleFightException{

        GroupEntity selectedGroup = searchForGroup(groupId);
        List<TournamentParticipantEntity> groupParticipants =  selectedGroup.getGroupParticipants();
        TournamentParticipantEntity firstParticipant = participantService.searchForParticipant(addFightRequest.getFirstParticipantId());
        TournamentParticipantEntity secondParticipant = participantService.searchForParticipant(addFightRequest.getSecondParticipantId());
        if(!(groupParticipants.contains(firstParticipant) && groupParticipants.contains(secondParticipant))) throw new ImpossibleFightException("Two participants are not in this group.");

        FightEntity createdFight = new FightEntity(firstParticipant, secondParticipant);
        createdFight.setGroup(selectedGroup);
        fightRepo.save(createdFight);
        selectedGroup.getGroupFights().add(createdFight);
        groupRepo.save(selectedGroup);
        log.info(String.format("Added fight between '%s' and '%s' to group.", createdFight.getFirstParticipant().getName(), createdFight.getSecondParticipant().getName()));
    }

    public void addParticipant(Long groupId, Long participantsId) throws DuplicateException{
        TournamentParticipantEntity participant = participantService.searchForParticipant(participantsId);
        addParticipant(groupId, participant);
    }

    public void addParticipant(Long groupId, TournamentParticipantEntity participant) throws DuplicateException {

        GroupEntity selectedGroup = searchForGroup(groupId);

        selectedGroup.getGroupParticipants().add(participant);
        participant.getGroupParticipations().add(selectedGroup);
        groupRepo.save(selectedGroup);
        tournamentParticipantsRepo.save(participant);
        log.info(String.format("Added '%s' to group.", participant.getName()));
    }

    public void deleteParticipant(Long groupId, Long participantId){

        GroupEntity selectedGroup = searchForGroup(groupId);

        TournamentParticipantEntity participant = participantService.searchForParticipant(participantId);

        if(selectedGroup.getGroupParticipants().contains(participant)){
            selectedGroup.getGroupParticipants().remove(participant);
            participant.getGroupParticipations().remove(selectedGroup);
            groupRepo.save(selectedGroup);
            tournamentParticipantsRepo.save(participant);
        }else{
            throw new NoSuchElementException("This participant does not belong to this group.");
        }

        log.info(String.format("Removing '%s' from group.", participant.getName()));
    }

    public void deleteGroup(Long groupId){
        GroupEntity selectedGroup = searchForGroup(groupId);

        for (FightEntity fight: selectedGroup.getGroupFights()) {
            fightRepo.deleteById(fight.getId());
        }
        groupRepo.deleteById(groupId);
    }



}
