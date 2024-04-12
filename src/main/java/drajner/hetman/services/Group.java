package drajner.hetman.services;

import java.util.ArrayList;

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

    public void addParticipant(TournamentParticipant participant){
        this.groupParticipants.add(participant);
    }

    public ArrayList<TournamentParticipant> getGroupParticipants(){
        return groupParticipants;
    }

    public void deleteParticipant(int participantNumber){
        groupParticipants.remove(participantNumber);
    }

    public void replaceParticipant(int fightNumber, TournamentParticipant participant){
        groupParticipants.set(fightNumber, participant);
    }

    public void addFight(Fight fight){
        this.fights.add(fight);
    }

    public ArrayList<Fight> getFights(){
        return fights;
    }

    public void deleteFight(int fightNumber){
        fights.remove(fightNumber);
    }

    public void replaceFight(int fightNumber, Fight fight){
        fights.set(fightNumber, fight);
    }

    public abstract void evaluateGroup(float modifier);

    public abstract void autoGenerateFights();

}
