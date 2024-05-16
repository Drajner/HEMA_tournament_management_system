package drajner.hetman.services;

import drajner.hetman.entities.FinalsTreeNodeEntity;
import drajner.hetman.entities.GroupEntity;
import drajner.hetman.entities.TournamentEntity;
import drajner.hetman.entities.TournamentParticipantEntity;
import drajner.hetman.errors.UnfinishedFightException;
import drajner.hetman.errors.WrongAmountException;
import drajner.hetman.repositories.FinalsTreeNodeRepo;
import drajner.hetman.repositories.GroupRepo;
import drajner.hetman.repositories.TournamentParticipantsRepo;
import drajner.hetman.repositories.TournamentRepo;
import lombok.extern.log4j.Log4j2;

import java.util.*;

@Log4j2
public class TournamentService {
    static TournamentRepo tournamentRepo;
    static TournamentParticipantsRepo tournamentParticipantsRepo;
    static GroupRepo groupRepo;
    static FinalsTreeNodeRepo finalsTreeNodeRepo;

    static public TournamentEntity searchForTournament(Long tournamentId){
        Optional<TournamentEntity> selectedTournamentSearch = tournamentRepo.findById(tournamentId);
        if (selectedTournamentSearch.isEmpty()) throw new NoSuchElementException("No tournament of this ID exists");
        return selectedTournamentSearch.get();
    }

    static public void addParticipant(Long tournamentId, Person person){
        TournamentEntity selectedTournament = searchForTournament(tournamentId);
        TournamentParticipantEntity newParticipant = new TournamentParticipantEntity(person);
        selectedTournament.getParticipants().add(newParticipant);
        tournamentRepo.save(selectedTournament);
    }

    static public void removeParticipant(Long participantId){
        tournamentParticipantsRepo.deleteById(participantId);
    }

    static public void replaceParticipant(TournamentParticipantEntity replacingTournamentParticipant){
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
    }

    static public void addGroupPool(Long tournamentId){
        TournamentEntity selectedTournament = searchForTournament(tournamentId);
        GroupEntity newGroup = new GroupEntity();
        selectedTournament.getGroups().add(newGroup);
        tournamentRepo.save(selectedTournament);
    }

    static public void sortParticipants(TournamentEntity selectedTournament){
        Collections.sort(selectedTournament.getParticipants(), Comparator
                .comparing(TournamentParticipantEntity::getWins)
                .thenComparing(TournamentParticipantEntity::getScore)
                .thenComparing(Comparator.comparingDouble(TournamentParticipantEntity::getDoubles).reversed())
                .thenComparing(Comparator.comparingDouble(TournamentParticipantEntity::getCards).reversed()));

        int rankingPosition = 1;
        for (TournamentParticipantEntity participant: selectedTournament.getParticipants()) {
            participant.setRanking(rankingPosition);
            rankingPosition++;
            tournamentParticipantsRepo.save(participant);
        }

        log.info(String.format("%s tournament participants list is sorted.", selectedTournament.getName()));
    }

    static public List<TournamentParticipantEntity> getGroupWinners(TournamentEntity selectedTournament, int winnersNumber) throws WrongAmountException{

        if(winnersNumber > selectedTournament.getParticipants().size()) throw new WrongAmountException("Ladder to big for this tournament");
        if(winnersNumber < 4) throw new WrongAmountException("Ladder too small");

        sortParticipants(selectedTournament);

        ArrayList<TournamentParticipantEntity> groupWinners = new ArrayList<>();
        int arrayIterator = 0;
        for(int i=0; i<winnersNumber;i++){

            if(selectedTournament.getParticipants().get(i).getStatus() != CompetitorStatus.DISQUALIFIED){
                groupWinners.add(selectedTournament.getParticipants().get(arrayIterator));
            }else{
                i--;
            }
            arrayIterator += 1;
        }
        return groupWinners;
    }

    static public void createLadder(Long tournamentId, int size) throws WrongAmountException{

        if(size < 4) throw new WrongAmountException("There need to be 4 or more participants in finals");

        TournamentEntity selectedTournament = searchForTournament(tournamentId);

        List<TournamentParticipantEntity> groupWinners = getGroupWinners(selectedTournament, size);

        selectedTournament.setFinalFight(new FinalsTreeNodeEntity());
        selectedTournament.setThirdPlaceFight(new FinalsTreeNodeEntity(true));

        FinalsTreeNodeService.setUpTree(selectedTournament.getFinalFight(), groupWinners);
        finalsTreeNodeRepo.save(selectedTournament.getFinalFight());

        selectedTournament.getThirdPlaceFight().setFirstChildNode(selectedTournament.getFinalFight().getFirstChildNode());
        selectedTournament.getThirdPlaceFight().setSecondChildNode(selectedTournament.getFinalFight().getSecondChildNode());
        finalsTreeNodeRepo.save(selectedTournament.getThirdPlaceFight());

        tournamentRepo.save(selectedTournament);
        log.info(String.format("Created finals for '%s' tournament.", selectedTournament.getName()));
    }

    static public void evaluateFinals(Long tournamentId) throws UnfinishedFightException {
        TournamentEntity selectedTournament = searchForTournament(tournamentId);
        if(selectedTournament.getFinalFight() != null && selectedTournament.getThirdPlaceFight() != null){
            log.info("Evaluating current tree.");
            FinalsTreeNodeService.fillNode(selectedTournament.getFinalFight());
            finalsTreeNodeRepo.save(selectedTournament.getFinalFight());
            FinalsTreeNodeService.fillNode(selectedTournament.getThirdPlaceFight());
            finalsTreeNodeRepo.save(selectedTournament.getThirdPlaceFight());
            tournamentRepo.save(selectedTournament);
            log.info("Evaluated current tree.");
        }else{
            throw new NoSuchElementException("This tournament has no finals to evaluate");
        }

    }

    static public void createGroups(Long tournamentId, int numberOfGroups) throws WrongAmountException{

        TournamentEntity selectedTournament = searchForTournament(tournamentId);

        ArrayList<GroupEntity> newGroups = new ArrayList<>();
        if(numberOfGroups >= (selectedTournament.getParticipants().size() / 2)) throw new WrongAmountException("Too many groups for participants amount");
        for(int i=0;i<numberOfGroups;i++){
            newGroups.add(new GroupEntity());
        }
        TournamentParticipantEntity tpToBeAdded;
        for(int i=0; i < selectedTournament.getParticipants().size();){
            for(GroupEntity group: newGroups){
                try{
                    tpToBeAdded = selectedTournament.getParticipants().get(i);
                    group.getGroupParticipants().add(tpToBeAdded);
                    i++;
                }catch(IndexOutOfBoundsException e) {
                    break;
                }
            }
        }
        for(GroupEntity group: newGroups){
            groupRepo.save(group);
        }
        selectedTournament.setGroups(new ArrayList<>(newGroups));
        tournamentRepo.save(selectedTournament);
        log.info(String.format("Created groups for '%s' tournament.", selectedTournament.getName()));
    }

    static public List<FinalsTreeNodeEntity> getFinals(Long tournamentId){
        TournamentEntity selectedTournament = searchForTournament(tournamentId);
        List<FinalsTreeNodeEntity> finalsNodes = new ArrayList<>();
        finalsNodes.add(selectedTournament.getFinalFight());
        finalsNodes.add(selectedTournament.getThirdPlaceFight());
        return finalsNodes;
    }
}
