package drajner.hetman.services;

import drajner.hetman.TournamentsSingleton;
import drajner.hetman.errors.DuplicateException;
import drajner.hetman.errors.UnfinishedFightException;
import drajner.hetman.errors.WrongAmountException;
import lombok.extern.log4j.Log4j2;

import java.util.ArrayList;

@Log4j2
public abstract class Group {
    protected ArrayList<TournamentParticipant> groupParticipants;
    protected ArrayList<Fight> fights;

    public Group(){
        groupParticipants = new ArrayList<>();
        fights = new ArrayList<>();
    }

    public Group(ArrayList<TournamentParticipant> groupParticipants){
        this.groupParticipants = groupParticipants;
        this.fights = new ArrayList<>();
    }

    public void addFight(Fight fight){
        this.fights.add(fight);
        log.info(String.format("Added fight between '%s' and '%s' to group.", fight.getFirstParticipant().getName(), fight.getSecondParticipant().getName()));
    }

    public void addParticipant(TournamentParticipant participant) throws DuplicateException{
        for(TournamentParticipant tp: groupParticipants){
            if(tp == participant) throw new DuplicateException("One participant cannot compete twice in one group");
        }
        this.groupParticipants.add(participant);
        log.info(String.format("Added '%s' to group.", participant.getName()));
    }

    public ArrayList<TournamentParticipant> getGroupParticipants(){
        return groupParticipants;
    }

    public ArrayList<Fight> getFights(){
        return fights;
    }

    public TournamentParticipant getGroupParticipant(int number){
        return groupParticipants.get(number);
    }

    public Fight getFight(int number) {return fights.get(number);}

    public void deleteParticipant(int participantNumber){
        log.info(String.format("Removing '%s' from group.", groupParticipants.get(participantNumber).getName()));
        groupParticipants.remove(participantNumber);

    }

    public void deleteFight(int fightNumber){
        log.info(String.format("Removing fight between '%s' and '%s' from group.", getFight(fightNumber).getFirstParticipant().getName(), getFight(fightNumber).getSecondParticipant().getName()));
        fights.remove(fightNumber);
    }

    public void replaceFight(int fightNumber, Fight fight){
        log.info(String.format("Replacing fight between '%s' and '%s' within group.", getFight(fightNumber).getFirstParticipant().getName(), getFight(fightNumber).getSecondParticipant().getName()));
        fights.set(fightNumber, fight);
    }

    public abstract void evaluateGroup(float modifier) throws UnfinishedFightException;

    public abstract void autoGenerateFights() throws WrongAmountException;

}
