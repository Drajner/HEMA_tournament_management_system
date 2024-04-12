package drajner.hetman;

import drajner.hetman.requests.FightReport;
import drajner.hetman.services.Tournament;

import java.util.ArrayList;

public class TournamentsSingleton {

    private static volatile TournamentsSingleton instance;

    public ArrayList<Tournament> tournaments;
    public ArrayList<FightReport> reports;

    private TournamentsSingleton(){
        this.tournaments = new ArrayList<>();
    }

    public static TournamentsSingleton getInstance(){

        TournamentsSingleton result = instance;
        if (result != null){
            return result;
        }
        synchronized (TournamentsSingleton.class){
            if (instance == null){
                instance = new TournamentsSingleton();
            }
            return instance;
        }
    }

    public static void add(Tournament tournament){
        getInstance().tournaments.add(tournament);
    }

    public static void remove(int tournamentPos){
        getInstance().tournaments.remove(tournamentPos);
    }

    public static void replace(int tournamentPos, Tournament tournament){
        getInstance().tournaments.set(tournamentPos, tournament);
    }

    public static Tournament get(int tournamentPos){
        return getInstance().tournaments.get(tournamentPos);
    }

    public static ArrayList<FightReport> getReports(){
        return getInstance().reports;
    }

    public static void addReport(FightReport fightReport){
        getInstance().reports.add(fightReport);
    }

    public static void removeReport(int number){
        getInstance().reports.remove(number);
    }

    public static void acceptReport(int number){
        FightReport acceptedReport = getInstance().reports.get(number);
        getInstance().tournaments.get(acceptedReport.getTournamentNumber()).getGroups().get(acceptedReport.getGroupNumber())
                .getFights().set(acceptedReport.getFightNumber(), acceptedReport.getFightProposition());
    }
}
