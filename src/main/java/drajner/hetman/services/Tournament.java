package drajner.hetman.services;

import lombok.extern.java.Log;
import lombok.extern.log4j.Log4j2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

@Log4j2
public class Tournament {

    private String name;
    private ArrayList<TournamentParticipant> participants;
    private ArrayList<Group> groups;

    public Tournament(String name){
        this.name = name;
        this.participants = new ArrayList<>();
        this.groups = new ArrayList<>();
        log.debug(String.format("Created tournament: %s", name));
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<TournamentParticipant> getParticipants() {
        return participants;
    }

    public ArrayList<Group> getGroups() {
        return groups;
    }

    public void addParticipant(TournamentParticipant newParticipant){
        participants.add(newParticipant);
        log.debug(String.format("Added '%s' to '%s' tournament.", newParticipant.getName(), name));
    }

    public void addParticipant(Person person){
        TournamentParticipant newParticipant = new TournamentParticipant(person);
        participants.add(newParticipant);
        log.debug(String.format("Added '%s' to '%s' tournament.", newParticipant.getName(), name));
    }

    public void addGroupPool(){
        GroupPool newGroup = new GroupPool();
        groups.add(newGroup);
    }

    public void addGroupLadder(){
        GroupFinals newGroup = new GroupFinals();
        groups.add(newGroup);
    }

    public void sortParticipants(){
        Collections.sort(participants, Comparator
                .comparing(TournamentParticipant::getWins)
                .thenComparing(TournamentParticipant::getScore)
                .thenComparing(Comparator.comparingDouble(TournamentParticipant::getDoubles).reversed())
                .thenComparing(Comparator.comparingDouble(TournamentParticipant::getCards).reversed()));
        log.debug(String.format("%s is sorted.", name));
    }


    public ArrayList<TournamentParticipant> getGroupWinners(int ladderSize){
        if(ladderSize > participants.size()){
            System.out.println("Ladder to big");
        }
        sortParticipants();

        ArrayList<TournamentParticipant> groupWinners = new ArrayList<>();
        for(int i=0; i<ladderSize;i++){
            groupWinners.add(participants.get(i));
        }
        return groupWinners;
    }

    public void createLadder(int size){
        ArrayList<TournamentParticipant> groupWinners = getGroupWinners(size);
        GroupFinals ladder = new GroupFinals(groupWinners);
        groups.add(new GroupFinals(groupWinners));
        log.debug("Created finals group.");
    }

    public void createGroups(int numberOfGroups){

        ArrayList<Group> newGroups = new ArrayList<>();
        for(int i=0;i<numberOfGroups;i++){
            newGroups.add(new GroupPool());
        }
        //for()
    }


}
