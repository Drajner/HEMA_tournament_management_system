package drajner.hetman.services;

import java.util.ArrayList;

public abstract class Group {
    private ArrayList<Competitor> competitors;
    private ArrayList<Fight> fights;

    void addCompetitor(Competitor competitor){
        this.competitors.add(competitor);
    }

    void addFight(Fight fight){
        this.fights.add(fight);
    }

    abstract ArrayList<Competitor> evaluateGroup();



}
