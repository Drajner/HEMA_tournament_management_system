package drajner.hetman;

import drajner.hetman.entities.FightEntity;
import drajner.hetman.entities.TournamentParticipantEntity;
import drajner.hetman.errors.NotPendingException;
import drajner.hetman.errors.ReportMismatchException;
import drajner.hetman.requests.FightReport;
import drajner.hetman.services.FightService;
import drajner.hetman.services.ParticipantService;
import drajner.hetman.services.ReportsList;
import drajner.hetman.services.ReportsService;
import drajner.hetman.status.FightStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ReportsServiceTest {

    @InjectMocks
    private ReportsService reportsService;

    @Mock
    private FightService fightService;

    @Mock
    private ParticipantService participantService;

    @Mock
    private ReportsList reportsList;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetReports() {
        List<FightReport> reports = new ArrayList<>();
        when(reportsList.getAll()).thenReturn(reports);

        List<FightReport> result = reportsService.getReports();

        assertEquals(reports, result);
        verify(reportsList, times(1)).getAll();
    }

    @Test
    public void testGetReport() {
        int index = 0;
        FightReport report = new FightReport();
        when(reportsList.get(index)).thenReturn(report);

        FightReport result = reportsService.getReport(index);

        assertEquals(report, result);
        verify(reportsList, times(1)).get(index);
    }

    @Test
    public void testAddReport() {
        FightReport report = new FightReport();

        reportsService.addReport(report);

        verify(reportsList, times(1)).add(report);
    }

    @Test
    public void testRemoveReport() {
        int number = 0;

        reportsService.removeReport(number);

        verify(reportsList, times(1)).remove(number);
    }

    @Test
    public void testAcceptReportSuccess() throws ReportMismatchException, NotPendingException {
        int reportNumber = 0;
        FightReport report = new FightReport();
        report.setFightId(1L);
        report.setFirstParticipantId(2L);
        report.setSecondParticipantId(3L);
        report.setWinner(2L);
        report.setFirstParticipantPoints(10);
        report.setSecondParticipantPoints(5);
        report.setFirstParticipantCards(1);
        report.setSecondParticipantCards(2);
        report.setDoubles(0);
        report.setUsername("user");

        FightEntity fight = new FightEntity();
        fight.setStatus(FightStatus.PENDING);
        fight.setId(1L);
        TournamentParticipantEntity firstParticipant = new TournamentParticipantEntity();
        firstParticipant.setParticipantId(2L);
        TournamentParticipantEntity secondParticipant = new TournamentParticipantEntity();
        secondParticipant.setParticipantId(3L);
        fight.setFirstParticipant(firstParticipant);
        fight.setSecondParticipant(secondParticipant);
        fight.setWinner(firstParticipant);

        when(reportsList.get(reportNumber)).thenReturn(report);
        when(fightService.searchForFight(report.getFightId())).thenReturn(fight);
        when(participantService.searchForParticipant(report.getFirstParticipantId())).thenReturn(firstParticipant);
        when(participantService.searchForParticipant(report.getSecondParticipantId())).thenReturn(secondParticipant);
        when(participantService.searchForParticipant(report.getWinner())).thenReturn(firstParticipant);


        reportsService.acceptReport(reportNumber);

        assertEquals(FightStatus.FINISHED, fight.getStatus());
        assertEquals(report.getFirstParticipantPoints(), fight.getFirstParticipantPoints());
        assertEquals(report.getSecondParticipantPoints(), fight.getSecondParticipantPoints());
        assertEquals(report.getFirstParticipantCards(), fight.getFirstParticipantCards());
        assertEquals(report.getSecondParticipantCards(), fight.getSecondParticipantCards());
        assertEquals(report.getDoubles(), fight.getDoubles());
        assertEquals(firstParticipant, fight.getWinner());
        verify(fightService, times(1)).saveFight(fight);
        verify(reportsList, times(1)).remove(reportNumber);
    }

    @Test
    public void testAcceptReportNotPending() throws ReportMismatchException {
        int reportNumber = 0;
        FightReport report = new FightReport();
        report.setFightId(1L);
        report.setFirstParticipantId(2L);
        report.setSecondParticipantId(3L);
        report.setWinner(2L);

        FightEntity fight = new FightEntity();
        fight.setStatus(FightStatus.FINISHED);

        when(reportsList.get(reportNumber)).thenReturn(report);
        when(fightService.searchForFight(report.getFightId())).thenReturn(fight);

        assertThrows(NotPendingException.class, () -> reportsService.acceptReport(reportNumber));
    }

    @Test
    public void testAcceptReportMismatch() throws ReportMismatchException {
        int reportNumber = 0;
        FightReport report = new FightReport();
        report.setFightId(1L);
        report.setFirstParticipantId(2L);
        report.setSecondParticipantId(3L);
        report.setWinner(2L);

        FightEntity fight = new FightEntity();
        fight.setStatus(FightStatus.PENDING);
        TournamentParticipantEntity wrongParticipant = new TournamentParticipantEntity();

        when(reportsList.get(reportNumber)).thenReturn(report);
        when(fightService.searchForFight(report.getFightId())).thenReturn(fight);
        when(participantService.searchForParticipant(report.getFirstParticipantId())).thenReturn(wrongParticipant);
        when(participantService.searchForParticipant(report.getSecondParticipantId())).thenReturn(wrongParticipant);

        assertThrows(ReportMismatchException.class, () -> reportsService.acceptReport(reportNumber));
    }


}
