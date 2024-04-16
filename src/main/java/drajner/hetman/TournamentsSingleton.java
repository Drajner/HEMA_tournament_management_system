package drajner.hetman;

import drajner.hetman.errors.ReportMismatchException;
import drajner.hetman.requests.FightReport;
import drajner.hetman.services.Fight;
import drajner.hetman.services.FightStatus;
import drajner.hetman.services.Group;
import drajner.hetman.services.Tournament;
import lombok.extern.log4j.Log4j2;

import java.util.ArrayList;
@Log4j2
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

    public static void acceptReport(int number) throws ReportMismatchException{
        FightReport acceptedReport = getInstance().reports.get(number);
        Group reportedGroup =  getInstance().tournaments
                .get(acceptedReport.getTournamentNumber())
                .getGroup(acceptedReport.getGroupNumber());
        Fight reportedFight = reportedGroup.getFight(acceptedReport.getFightNumber());
        if( !(reportedFight.getFirstParticipant() == reportedGroup.getGroupParticipant(acceptedReport.getFightNumber()) &&
            reportedFight.getSecondParticipant() == reportedGroup.getGroupParticipant(acceptedReport.getSecondParticipantNumber())) ){
            throw new ReportMismatchException("Accepted report doesn't match with its selected fight.");
        }
        reportedFight.setFirstParticipantPoints(acceptedReport.getFirstParticipantPoints());
        reportedFight.setSecondParticipantPoints(acceptedReport.getSecondParticipantPoints());
        reportedFight.setFirstParticipantCards(acceptedReport.getFirstParticipantCards());
        reportedFight.setSecondParticipantCards(acceptedReport.getSecondParticipantCards());
        reportedFight.setDoubles(acceptedReport.getDoubles());
        reportedFight.setWinner(reportedGroup.getGroupParticipant(acceptedReport.getWinnerNumber()));
        reportedFight.setStatus(FightStatus.FINISHED);

    }

    public static void importTournaments(String path){

    }

    public static void exportTournaments(){
        
    }
}
