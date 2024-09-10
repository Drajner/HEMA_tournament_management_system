package drajner.hetman;

import drajner.hetman.entities.*;
import drajner.hetman.errors.*;
import drajner.hetman.repositories.*;
import drajner.hetman.requests.Person;
import drajner.hetman.services.TournamentService;
import drajner.hetman.status.CompetitorStatus;
import drajner.hetman.services.FinalsTreeNodeService;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@Log4j2
public class TournamentServiceTest {

    @InjectMocks
    private TournamentService tournamentService;

    @Mock
    private TournamentRepo tournamentRepo;

    @Mock
    private TournamentParticipantsRepo tournamentParticipantsRepo;

    @Mock
    private GroupRepo groupRepo;

    @Mock
    private FightRepo fightRepo;

    @Mock
    private FinalsTreeNodeRepo finalsTreeNodeRepo;

    @Mock
    private FinalsTreeNodeService finalsTreeNodeService;

    private TournamentEntity tournament;
    private TournamentParticipantEntity participant1;
    private TournamentParticipantEntity participant2;
    private TournamentParticipantEntity participant3;
    private TournamentParticipantEntity participant4;

    private TournamentParticipantEntity participant5;
    private GroupEntity group;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        tournament = new TournamentEntity();
        tournament.setTournamentId(1L);
        tournament.setName("Test Tournament");

        participant1 = new TournamentParticipantEntity();
        participant1.setParticipantId(1L);
        participant1.setName("test");
        participant1.setSurname("man");
        participant1.setGroupParticipations(new ArrayList<>());

        participant2 = new TournamentParticipantEntity();
        participant2.setParticipantId(2L);
        participant2.setName("man");
        participant2.setSurname("test");
        participant2.setGroupParticipations(new ArrayList<>());

        participant3 = new TournamentParticipantEntity();
        participant3.setParticipantId(3L);
        participant3.setName("mest");
        participant3.setSurname("tan");
        participant3.setGroupParticipations(new ArrayList<>());

        participant4 = new TournamentParticipantEntity();
        participant4.setParticipantId(4L);
        participant4.setName("tan");
        participant4.setSurname("mest");
        participant4.setGroupParticipations(new ArrayList<>());

        participant5 = new TournamentParticipantEntity();
        participant5.setParticipantId(4L);
        participant5.setName("tset");
        participant5.setSurname("nam");
        participant5.setGroupParticipations(new ArrayList<>());

        tournament.setParticipants(Arrays.asList(participant1, participant2, participant3, participant4, participant5));

        group = new GroupEntity();
        group.setGroupId(1L);
        group.setTournament(tournament);

        tournament.setGroups(new ArrayList<>(Arrays.asList(group)));
    }

    @Test
    void testSearchForTournamentFound() {
        when(tournamentRepo.findById(1L)).thenReturn(Optional.of(tournament));
        TournamentEntity foundTournament = tournamentService.searchForTournament(1L);
        assertNotNull(foundTournament);
        assertEquals(1L, foundTournament.getTournamentId());
    }

    @Test
    void testSearchForTournamentNotFound() {
        when(tournamentRepo.findById(1L)).thenReturn(Optional.empty());
        assertThrows(NoSuchElementException.class, () -> tournamentService.searchForTournament(1L));
    }

    @Test
    void testRemoveParticipant() {
        tournamentService.removeParticipant(1L);
        verify(tournamentParticipantsRepo, times(1)).deleteById(1L);
    }

    @Test
    void testReplaceParticipantFound() {
        when(tournamentParticipantsRepo.findById(1L)).thenReturn(Optional.of(participant1));
        participant1.setName("Updated Name");

        tournamentService.replaceParticipant(participant1);
        verify(tournamentParticipantsRepo, times(1)).save(participant1);
        assertEquals("Updated Name", participant1.getName());
    }

    @Test
    void testReplaceParticipantNotFound() {
        when(tournamentParticipantsRepo.findById(1L)).thenReturn(Optional.empty());
        assertThrows(NoSuchElementException.class, () -> tournamentService.replaceParticipant(participant1));
    }

    @Test
    void testAddGroupPool() {
        when(tournamentRepo.findById(1L)).thenReturn(Optional.of(tournament));

        tournamentService.addGroupPool(1L);
        verify(groupRepo, times(1)).save(any(GroupEntity.class));
        verify(tournamentRepo, times(1)).save(tournament);
    }

    @Test
    void testSortParticipants() {
        participant1.setWins(3);
        participant2.setWins(5);
        when(tournamentRepo.findById(1L)).thenReturn(Optional.of(tournament));

        List<TournamentParticipantEntity> sortedParticipants = tournamentService.sortParticipants(tournament);
        assertEquals(participant2, sortedParticipants.get(0));
        assertEquals(participant1, sortedParticipants.get(1));
    }

    @Test
    void testGetGroupWinners() throws WrongAmountException {
        when(tournamentRepo.findById(1L)).thenReturn(Optional.of(tournament));

        participant1.setStatus(CompetitorStatus.COMPETING);
        participant2.setStatus(CompetitorStatus.COMPETING);
        List<TournamentParticipantEntity> groupWinners = tournamentService.getGroupWinners(tournament, 4);

        assertEquals(4, groupWinners.size());
    }

    @Test
    void testGetGroupWinnersThrowsWrongAmountException() {
        when(tournamentRepo.findById(1L)).thenReturn(Optional.of(tournament));
        assertThrows(WrongAmountException.class, () -> tournamentService.getGroupWinners(tournament, 2));
    }

    @Test
    void testEvaluateFinals() throws UnfinishedFightException {
        FinalsTreeNodeEntity finalFight = new FinalsTreeNodeEntity();
        FinalsTreeNodeEntity thirdPlaceFight = new FinalsTreeNodeEntity();
        tournament.setFinalFight(finalFight);
        tournament.setThirdPlaceFight(thirdPlaceFight);
        when(tournamentRepo.findById(1L)).thenReturn(Optional.of(tournament));

        tournamentService.evaluateFinals(1L);
        verify(finalsTreeNodeService, times(1)).fillNode(finalFight);
        verify(finalsTreeNodeService, times(1)).fillNode(thirdPlaceFight);
    }

    @Test
    void testEvaluateFinalsThrowsNoFinals() {
        tournament.setFinalFight(null);
        when(tournamentRepo.findById(1L)).thenReturn(Optional.of(tournament));

        assertThrows(NoSuchElementException.class, () -> tournamentService.evaluateFinals(1L));
    }

    @Test
    void testCreateGroups() throws WrongAmountException {
        when(tournamentRepo.findById(1L)).thenReturn(Optional.of(tournament));
        when(groupRepo.save(any(GroupEntity.class))).thenReturn(group);

        tournamentService.createGroups(1L, 1);
        verify(groupRepo, times(7)).save(any(GroupEntity.class));
        verify(tournamentRepo, times(2)).save(tournament);
    }

    @Test
    void testGetFinals() {
        FinalsTreeNodeEntity finalFight = new FinalsTreeNodeEntity();
        FinalsTreeNodeEntity thirdPlaceFight = new FinalsTreeNodeEntity();
        tournament.setFinalFight(finalFight);
        tournament.setThirdPlaceFight(thirdPlaceFight);
        when(tournamentRepo.findById(1L)).thenReturn(Optional.of(tournament));

        List<FinalsTreeNodeEntity> finals = tournamentService.getFinals(1L);
        assertEquals(2, finals.size());
        assertEquals(finalFight, finals.get(0));
        assertEquals(thirdPlaceFight, finals.get(1));
    }

    @Test
    void testPurgeFinals() {
        FinalsTreeNodeEntity finalFight = new FinalsTreeNodeEntity();
        FinalsTreeNodeEntity thirdPlaceFight = new FinalsTreeNodeEntity();
        tournament.setFinalFight(finalFight);
        tournament.setThirdPlaceFight(thirdPlaceFight);
        when(tournamentRepo.findById(1L)).thenReturn(Optional.of(tournament));

        tournamentService.purgeFinals(1L);
        verify(finalsTreeNodeService, times(1)).purgeFights(finalFight);
        verify(finalsTreeNodeService, times(1)).purgeFights(thirdPlaceFight);
    }
}
