package drajner.hetman.services;

import drajner.hetman.entities.FightEntity;
import drajner.hetman.entities.TournamentParticipantEntity;
import drajner.hetman.errors.ReportMismatchException;
import drajner.hetman.errors.NotPendingException;
import drajner.hetman.requests.FightReport;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Repository
@Service
@Log4j2
public class ReportsSingleton {

    @Autowired
    static FightService fightService;

    @Autowired
    static ParticipantService participantService;

    private static volatile ReportsSingleton instance;
    public ArrayList<FightReport> reports;

    public ReportsSingleton(){
        this.reports = new ArrayList<>();
    }

    public static ReportsSingleton getInstance(){

        ReportsSingleton result = instance;
        if (result != null){
            return result;
        }
        synchronized (ReportsSingleton.class){
            if (instance == null){
                instance = new ReportsSingleton();
            }
            return instance;
        }
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

    public static void acceptReport(int number) throws ReportMismatchException, NotPendingException{
        FightReport acceptedReport = getInstance().reports.get(number);
        FightEntity savedFight = fightService.searchForFight(acceptedReport.getFightId());
        if(savedFight.getStatus() != FightStatus.PENDING) throw new NotPendingException("This fight is already finished.");
        TournamentParticipantEntity firstParticipant = participantService.searchForParticipant(acceptedReport.getFirstParticipantId());
        TournamentParticipantEntity secondParticipant = participantService.searchForParticipant(acceptedReport.getSecondParticipantId());
        if( !(savedFight.getFirstParticipant() == firstParticipant && savedFight.getSecondParticipant() == secondParticipant) ){
            throw new ReportMismatchException("Accepted report doesn't match with its selected fight.");
        }
        savedFight.setFirstParticipantPoints(acceptedReport.getFirstParticipantPoints());
        savedFight.setSecondParticipantPoints(acceptedReport.getSecondParticipantPoints());
        savedFight.setFirstParticipantCards(acceptedReport.getFirstParticipantCards());
        savedFight.setSecondParticipantCards(acceptedReport.getSecondParticipantCards());
        savedFight.setDoubles(acceptedReport.getDoubles());
        TournamentParticipantEntity winner = participantService.searchForParticipant(acceptedReport.getWinner());
        savedFight.setWinner(winner);
        savedFight.setStatus(FightStatus.FINISHED);
        fightService.saveFight(savedFight);
    }
}
