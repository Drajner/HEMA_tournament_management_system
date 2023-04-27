package drajner.hetman.services;

import java.util.ArrayList;

public class Tournament {

    private String name;
    private ArrayList<Competitor> competitors;
    private ArrayList<Group> groups;

    public Tournament(String name){
        this.name = name;
        this.competitors = new ArrayList<Competitor>();
        this.groups = new ArrayList<Group>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Competitor> getCompetitors() {
        return competitors;
    }

    public ArrayList<Group> getGroups() {
        return groups;
    }

    public void addCompetitor(Competitor newCompetitor){
        competitors.add(newCompetitor);
    }





}
