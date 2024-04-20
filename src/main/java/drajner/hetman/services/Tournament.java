package drajner.hetman.services;

import drajner.hetman.errors.DuplicateException;
import drajner.hetman.errors.OneFinalsException;
import drajner.hetman.errors.WrongAmountException;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.java.Log;
import lombok.extern.log4j.Log4j2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

@Log4j2
@Getter
@Setter
public class Tournament {

    private String name;
    private ArrayList<TournamentParticipant> participants;
    private ArrayList<Group> groups;

    private GroupFinals finals;

    public Tournament(String name){
        this.name = name;
        this.participants = new ArrayList<>();
        this.groups = new ArrayList<>();
        log.info(String.format("Created tournament: %s", name));
    }

    public Group getGroup(int number){return groups.get(number);}

    public void addParticipant(TournamentParticipant newParticipant){
        participants.add(newParticipant);
        log.info(String.format("Added '%s' to '%s' tournament.", newParticipant.getName(), name));
    }

    public void addParticipant(Person person){
        TournamentParticipant newParticipant = new TournamentParticipant(person);
        participants.add(newParticipant);
        log.info(String.format("Added '%s' to '%s' tournament.", newParticipant.getName(), name));
    }

    public void removeParticipant(int number){
        TournamentParticipant removedParticipant = participants.get(number);
        log.info(String.format("Removing '%s' from '%s' tournament.", removedParticipant.getName(), name));
        participants.remove(number);
        for (Group group: groups) {
            group.getGroupParticipants().remove(removedParticipant);
            for (Fight fight: group.getFights()){
                if(fight.getFirstParticipant() == removedParticipant){
                    fight.setFirstParticipant(null);
                }
                if(fight.getSecondParticipant() == removedParticipant){
                    fight.setSecondParticipant(null);
                }
            }
        }
        log.info("Removed");
    }

    public void replaceParticipant(int number, TournamentParticipant participant){
        log.info(String.format("Replacing '%s' participant with '%s' in '%s' tournament.", number, participant.getName(), name));
        participants.set(number, participant);
        log.info("Replaced");
    }

    public TournamentParticipant getParticipant(int number){return participants.get(number);}

    public void addGroupPool(){
        GroupPool newGroup = new GroupPool();
        groups.add(newGroup);
        log.info(String.format("Added new group to '%s' tournament.", name));
    }

    public void addGroupLadder() throws OneFinalsException{
        if(finals == null) {
            GroupFinals newGroup = new GroupFinals();
            groups.add(newGroup);
            log.info(String.format("Added finals to '%s' tournament.", name));
        }
        else{
            throw new OneFinalsException("There can be only one finals group!");
        }
    }

    public void sortParticipants(){
        Collections.sort(participants, Comparator
                .comparing(TournamentParticipant::getWins)
                .thenComparing(TournamentParticipant::getScore)
                .thenComparing(Comparator.comparingDouble(TournamentParticipant::getDoubles).reversed())
                .thenComparing(Comparator.comparingDouble(TournamentParticipant::getCards).reversed()));
        log.info(String.format("%s tournament participants list is sorted.", name));
    }


    public ArrayList<TournamentParticipant> getGroupWinners(int ladderSize) throws WrongAmountException{
        if(ladderSize > participants.size()) throw new WrongAmountException("Ladder to big for this tournament");
        if(ladderSize < 4) throw new WrongAmountException("Ladder too small");

        sortParticipants();

        ArrayList<TournamentParticipant> groupWinners = new ArrayList<>();
        int arrayIterator = 0;
        for(int i=0; i<ladderSize;i++){

            if(participants.get(i).getStatus() != CompetitorStatus.DISQUALIFIED){
                groupWinners.add(participants.get(arrayIterator));
            }else{
                i--;
            }
            arrayIterator += 1;
        }
        return groupWinners;
    }

    public void createLadder(int size) throws WrongAmountException{
        ArrayList<TournamentParticipant> groupWinners = getGroupWinners(size);
        finals = new GroupFinals(groupWinners);
        log.info(String.format("Created finals for '%s' tournament.", name));
    }

    public void createGroups(int numberOfGroups) throws WrongAmountException, DuplicateException {

        ArrayList<Group> newGroups = new ArrayList<>();
        if(numberOfGroups >= (participants.size() / 2)) throw new WrongAmountException("Too many groups for participants amount");
        for(int i=0;i<numberOfGroups;i++){
            newGroups.add(new GroupPool());
        }
        TournamentParticipant tpToBeAdded;
        for(int i=0; i < participants.size();){
            for(Group group: newGroups){
                try{
                    tpToBeAdded = participants.get(i);
                    group.addParticipant(tpToBeAdded);
                    i++;
                }catch(IndexOutOfBoundsException e) {
                    break;
                }
            }
        }
        groups = new ArrayList<>(newGroups);
        log.info(String.format("Created groups for '%s' tournament.", name));
    }


}
