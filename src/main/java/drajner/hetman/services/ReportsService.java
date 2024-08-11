package drajner.hetman.services;

import drajner.hetman.entities.FightEntity;
import drajner.hetman.entities.TournamentParticipantEntity;
import drajner.hetman.errors.NotPendingException;
import drajner.hetman.errors.ReportMismatchException;
import drajner.hetman.requests.FightReport;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Log4j2
public class ReportsService {

    @Autowired
    FightService fightService;

    @Autowired
    ParticipantService participantService;

    @Autowired
    ReportsList reportsList;

    public List<FightReport> getReports(){
        return reportsList.getAll();
    }

    public void addReport(FightReport fightReport){
        reportsList.add(fightReport);
    }

    public void removeReport(int number){
        reportsList.remove(number);
    }

    public void acceptReport(int number) throws ReportMismatchException, NotPendingException {
        FightReport acceptedReport = reportsList.get(number);
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
        reportsList.remove(number);
    }
}
