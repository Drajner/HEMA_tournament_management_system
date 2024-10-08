package drajner.hetman.services;

import drajner.hetman.entities.*;
import drajner.hetman.errors.OneFinalsException;
import drajner.hetman.errors.UnfinishedFightException;
import drajner.hetman.errors.WrongAmountException;
import drajner.hetman.repositories.*;
import drajner.hetman.requests.Person;
import drajner.hetman.status.CompetitorStatus;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Log4j2
public class TournamentService {

    @Autowired
    TournamentRepo tournamentRepo;
    @Autowired
    TournamentParticipantsRepo tournamentParticipantsRepo;
    @Autowired
    GroupRepo groupRepo;
    @Autowired
    FightRepo fightRepo;
    @Autowired
    FinalsTreeNodeRepo finalsTreeNodeRepo;
    @Autowired
    FinalsTreeNodeService finalsTreeNodeService;

    public TournamentEntity searchForTournament(Long tournamentId){
        log.info(String.format("Searching for tournament of id: %s", tournamentId));
        Optional<TournamentEntity> selectedTournamentSearch = tournamentRepo.findById(tournamentId);
        if (selectedTournamentSearch.isEmpty()) throw new NoSuchElementException("No tournament of this ID exists");
        return selectedTournamentSearch.get();
    }

    public void addParticipant(Long tournamentId, Person person){
        TournamentEntity selectedTournament = searchForTournament(tournamentId);
        TournamentParticipantEntity newParticipant = new TournamentParticipantEntity(person);
        newParticipant.setTournament(selectedTournament);
        selectedTournament.getParticipants().add(newParticipant);
        tournamentParticipantsRepo.save(newParticipant);
        tournamentRepo.save(selectedTournament);
        log.info(String.format("Added participant %s: %s to tournament %s",
                                newParticipant.getParticipantId(),
                                newParticipant.getFullName(), tournamentId));
    }

    public void removeParticipant(Long participantId){
        tournamentParticipantsRepo.deleteById(participantId);
        log.info(String.format("Deleted participant %s", participantId));
    }

    public void replaceParticipant(TournamentParticipantEntity replacingTournamentParticipant){
        Optional<TournamentParticipantEntity> search = tournamentParticipantsRepo.findById(replacingTournamentParticipant.getParticipantId());
        if(search.isEmpty()){
            throw new NoSuchElementException("No participant of this ID exists");
        }
        TournamentParticipantEntity tournamentParticipant = search.get();
        tournamentParticipant.setName(replacingTournamentParticipant.getName());
        tournamentParticipant.setSurname(replacingTournamentParticipant.getSurname());
        tournamentParticipant.setClub(replacingTournamentParticipant.getClub());
        tournamentParticipant.setStatus(replacingTournamentParticipant.getStatus());
        tournamentParticipant.setScore(replacingTournamentParticipant.getScore());
        tournamentParticipant.setWins(replacingTournamentParticipant.getWins());
        tournamentParticipant.setDoubles(replacingTournamentParticipant.getDoubles());
        tournamentParticipant.setCards(replacingTournamentParticipant.getCards());
        tournamentParticipant.setRanking(replacingTournamentParticipant.getRanking());
        tournamentParticipantsRepo.save(tournamentParticipant);
        log.info(String.format("Changed data of participant %s", tournamentParticipant.getParticipantId()));
    }

    public void addGroupPool(Long tournamentId){
        TournamentEntity selectedTournament = searchForTournament(tournamentId);
        GroupEntity newGroup = new GroupEntity();
        newGroup.setTournament(selectedTournament);
        selectedTournament.getGroups().add(newGroup);
        groupRepo.save(newGroup);
        tournamentRepo.save(selectedTournament);
        log.info(String.format("Added new group to tournament %s", tournamentId));
    }

    public List<TournamentParticipantEntity> sortParticipants(TournamentEntity selectedTournament){

        List<TournamentParticipantEntity> sortedParticipants = selectedTournament.getParticipants();
        Collections.sort(sortedParticipants, Comparator.comparingDouble(TournamentParticipantEntity::getWins).reversed()
                .thenComparing(Comparator.comparingDouble(TournamentParticipantEntity::getScore).reversed())
                .thenComparing(TournamentParticipantEntity::getDoubles)
                .thenComparing(TournamentParticipantEntity::getCards));

        int rankingPosition = 1;
        for (TournamentParticipantEntity participant: selectedTournament.getParticipants()) {
            participant.setRanking(rankingPosition);
            rankingPosition++;
            tournamentParticipantsRepo.save(participant);
        }

        log.info(String.format("%s tournament participants list is sorted.", selectedTournament.getName()));
        return sortedParticipants;
    }

    public List<TournamentParticipantEntity> getGroupWinners(TournamentEntity selectedTournament, int winnersNumber) throws WrongAmountException{

        if(winnersNumber > selectedTournament.getParticipants().size()) throw new WrongAmountException("Ladder to big for this tournament");
        if(winnersNumber < 4) throw new WrongAmountException("Ladder too small");

        List<TournamentParticipantEntity> sortedParticipants = sortParticipants(selectedTournament);
        ArrayList<TournamentParticipantEntity> groupWinners = new ArrayList<>();

        int arrayIterator = 0;
        for(int i=0; i<winnersNumber;i++){

            if(sortedParticipants.get(i).getStatus() != CompetitorStatus.DISQUALIFIED){
                groupWinners.add(sortedParticipants.get(arrayIterator));
            }else{
                i--;
            }
            arrayIterator += 1;
        }
        log.info(String.format("Decided %s best participants of tournament %s",
                                winnersNumber, selectedTournament.getTournamentId()));
        return groupWinners;
    }

    public void createLadder(Long tournamentId, int size) throws WrongAmountException, OneFinalsException{

        if(size < 4) throw new WrongAmountException("There need to be 4 or more participants in finals");

        TournamentEntity selectedTournament = searchForTournament(tournamentId);
        if(selectedTournament.getFinalFight() != null) throw new OneFinalsException("Ladder is already created. You need to clear existing ladder to create new one.");

        List<TournamentParticipantEntity> groupWinners = getGroupWinners(selectedTournament, size);

        selectedTournament.setFinalFight(new FinalsTreeNodeEntity());
        finalsTreeNodeRepo.save(selectedTournament.getFinalFight());
        tournamentRepo.save(selectedTournament);
        selectedTournament.setThirdPlaceFight(new FinalsTreeNodeEntity(true));
        finalsTreeNodeRepo.save(selectedTournament.getThirdPlaceFight());
        tournamentRepo.save(selectedTournament);

        finalsTreeNodeService.setUpTree(selectedTournament.getFinalFight(), groupWinners);
        finalsTreeNodeRepo.save(selectedTournament.getFinalFight());

        selectedTournament.getThirdPlaceFight().setFirstChildNode(selectedTournament.getFinalFight().getFirstChildNode());
        selectedTournament.getThirdPlaceFight().setSecondChildNode(selectedTournament.getFinalFight().getSecondChildNode());
        selectedTournament.getThirdPlaceFight().setFight(new FightEntity());
        fightRepo.save(selectedTournament.getThirdPlaceFight().getFight());
        finalsTreeNodeRepo.save(selectedTournament.getThirdPlaceFight());

        tournamentRepo.save(selectedTournament);
        log.info(String.format("Created ladder containing %s for '%s' tournament.", size,selectedTournament.getName()));
    }

    public void evaluateFinals(Long tournamentId) throws UnfinishedFightException {
        TournamentEntity selectedTournament = searchForTournament(tournamentId);
        if(selectedTournament.getFinalFight() != null && selectedTournament.getThirdPlaceFight() != null){
            log.info("Evaluating current tree.");
            finalsTreeNodeService.fillNode(selectedTournament.getFinalFight());
            finalsTreeNodeRepo.save(selectedTournament.getFinalFight());
            finalsTreeNodeService.fillNode(selectedTournament.getThirdPlaceFight());
            finalsTreeNodeRepo.save(selectedTournament.getThirdPlaceFight());
            tournamentRepo.save(selectedTournament);
            log.info("Evaluated current tree.");
        }else{
            throw new NoSuchElementException("This tournament has no finals to evaluate");
        }

    }

    public void createGroups(Long tournamentId, int numberOfGroups) throws WrongAmountException{

        TournamentEntity selectedTournament = searchForTournament(tournamentId);

        ArrayList<GroupEntity> newGroups = new ArrayList<>();
        if(numberOfGroups > (selectedTournament.getParticipants().size() / 2)) throw new WrongAmountException("Too many groups for participants amount");
        for(int i=0;i<numberOfGroups;i++){
            newGroups.add(groupRepo.save(new GroupEntity(selectedTournament)));
        }
        for(int i=0; i < selectedTournament.getParticipants().size();){
            for(GroupEntity group: newGroups){
                try{
                    group.getGroupParticipants().add(selectedTournament.getParticipants().get(i));
                    selectedTournament.getParticipants().get(i).getGroupParticipations().add(group);
                    groupRepo.save(group);
                    i++;
                }catch(IndexOutOfBoundsException e) {
                    break;
                }
            }
        }
        float defaultModifierDivisor = newGroups.get(0).getGroupParticipants().size();
        for(TournamentParticipantEntity tp: selectedTournament.getParticipants()){
            tournamentParticipantsRepo.save(tp);
        }
        float newModifier;
        for(GroupEntity group: newGroups){
            newModifier = defaultModifierDivisor / group.getGroupParticipants().size();
            group.setModifier(newModifier);
            group.setTournament(selectedTournament);
            tournamentRepo.save(selectedTournament);
            groupRepo.save(group);
        }
        selectedTournament.setGroups(new ArrayList<>(newGroups));
        tournamentRepo.save(selectedTournament);
        log.info(String.format("Created groups for '%s' tournament.", selectedTournament.getName()));
    }

    public List<FinalsTreeNodeEntity> getFinals(Long tournamentId){
        TournamentEntity selectedTournament = searchForTournament(tournamentId);
        List<FinalsTreeNodeEntity> finalsNodes = new ArrayList<>();
        finalsNodes.add(selectedTournament.getFinalFight());
        finalsNodes.add(selectedTournament.getThirdPlaceFight());
        return finalsNodes;
    }

    public void purgeFinals(Long tournamentId){
        log.info(String.format("Purging finals of tournament %s.", tournamentId));
        TournamentEntity selectedTournament = searchForTournament(tournamentId);
        FinalsTreeNodeEntity finalFight = selectedTournament.getFinalFight();
        FinalsTreeNodeEntity thirdPlaceFight = selectedTournament.getThirdPlaceFight();
        selectedTournament.setFinalFight(null);
        selectedTournament.setThirdPlaceFight(null);
        tournamentRepo.save(selectedTournament);
        finalsTreeNodeService.purgeFights(finalFight);
        finalsTreeNodeService.purgeFights(thirdPlaceFight);
        finalsTreeNodeService.purgeTree(finalFight);
        finalsTreeNodeService.purgeTree(thirdPlaceFight);
        log.info(String.format("Purged finals of tournament %s.", tournamentId));

    }
}
