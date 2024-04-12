package drajner.hetman.requests;

import drajner.hetman.services.Fight;
public class FightReport {

    String username;
    int tournamentNumber;
    int groupNumber;
    int fightNumber;
    Fight fightProposition;

    public FightReport(String username, int tournamentNumber, int groupNumber, int fightNumber, Fight fightProposition){
        this.username = username;
        this.tournamentNumber = tournamentNumber;
        this.groupNumber = groupNumber;
        this.fightNumber = fightNumber;
        this.fightProposition = fightProposition;
    }

    public int getTournamentNumber(){return tournamentNumber;}
    public int getGroupNumber(){return groupNumber;}
    public int getFightNumber(){return fightNumber;}

    public Fight getFightProposition() {
        return fightProposition;
    }
}
