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

    void addCompetitor(TournamentParticipant competitor){
        this.groupParticipants.add(competitor);
    }

    void addFight(Fight fight){
        this.fights.add(fight);
    }

    abstract void evaluateGroup(float modifier);

    abstract void autoGenerateFights();

}
