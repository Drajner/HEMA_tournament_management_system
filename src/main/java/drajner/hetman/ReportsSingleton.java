package drajner.hetman;

import drajner.hetman.entities.FightEntity;
import drajner.hetman.errors.ReportMismatchException;
import drajner.hetman.errors.NotPendingException;
import drajner.hetman.requests.FightReport;
import drajner.hetman.services.*;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@Log4j2
public class ReportsSingleton {

    @Autowired
    static FightService fightService;

    private static volatile ReportsSingleton instance;
    public ArrayList<FightReport> reports;

    private ReportsSingleton(){
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
        FightEntity savedFight = fightService.searchForFight(acceptedReport.getFight().getId());
        if(savedFight.getStatus() != FightStatus.PENDING) throw new NotPendingException("This fight is already finished.");
        if( !(savedFight.getFirstParticipant() == acceptedReport.getFight().getFirstParticipant() &&
                savedFight.getSecondParticipant() == acceptedReport.getFight().getSecondParticipant()) ){
            throw new ReportMismatchException("Accepted report doesn't match with its selected fight.");
        }
        acceptedReport.getFight().setStatus(FightStatus.FINISHED);
        fightService.saveFight(acceptedReport.getFight());
    }
}
