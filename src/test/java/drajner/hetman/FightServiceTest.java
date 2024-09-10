package drajner.hetman;

import drajner.hetman.entities.FightEntity;
import drajner.hetman.entities.TournamentParticipantEntity;
import drajner.hetman.errors.UnfinishedFightException;
import drajner.hetman.repositories.FightRepo;
import drajner.hetman.repositories.TournamentParticipantsRepo;
import drajner.hetman.requests.ReplaceFightRequest;
import drajner.hetman.services.FightService;
import drajner.hetman.services.ParticipantService;
import drajner.hetman.status.FightStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;
import java.util.NoSuchElementException;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class FightServiceTest {

    @InjectMocks
    private FightService fightService;

    @Mock
    private FightRepo fightRepo;

    @Mock
    private TournamentParticipantsRepo tournamentParticipantsRepo;

    @Mock
    private ParticipantService participantService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testSearchForFightSuccess() {

        Long fightId = 1L;
        FightEntity fight = new FightEntity();
        when(fightRepo.findById(fightId)).thenReturn(Optional.of(fight));

        FightEntity result = fightService.searchForFight(fightId);

        assertNotNull(result);
        assertEquals(fight, result);
        verify(fightRepo, times(1)).findById(fightId);
    }

    @Test
    public void testSearchForFightNotFound() {
        Long fightId = 1L;
        when(fightRepo.findById(fightId)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> fightService.searchForFight(fightId));
        verify(fightRepo, times(1)).findById(fightId);
    }

    @Test
    public void testEvaluateFightFinishedSuccess() throws UnfinishedFightException {
        Long fightId = 1L;
        FightEntity fight = new FightEntity();
        fight.setStatus(FightStatus.FINISHED);
        TournamentParticipantEntity participant1 = new TournamentParticipantEntity();
        TournamentParticipantEntity participant2 = new TournamentParticipantEntity();
        fight.setFirstParticipant(participant1);
        fight.setSecondParticipant(participant2);
        when(fightRepo.findById(fightId)).thenReturn(Optional.of(fight));

        fightService.evaluateFight(fightId, 1.0f);

        assertEquals(FightStatus.EVALUATED, fight.getStatus());
        verify(fightRepo, times(1)).save(fight);
    }

    @Test
    public void testEvaluateFightPendingThrowsException() {
        Long fightId = 1L;
        FightEntity fight = new FightEntity();
        fight.setStatus(FightStatus.PENDING);
        when(fightRepo.findById(fightId)).thenReturn(Optional.of(fight));

        assertThrows(UnfinishedFightException.class, () -> fightService.evaluateFight(fightId, 1.0f));
        verify(fightRepo, never()).save(fight);
    }

    @Test
    public void testEvaluateParticipantSuccess() {
        TournamentParticipantEntity participant = new TournamentParticipantEntity();
        participant.setScore(0.0f);

        fightService.evaluateParticipant(participant, 10, 5, 1, 2, 1.0f);

        assertEquals(4.0f, participant.getScore());
        verify(tournamentParticipantsRepo, times(1)).save(participant);
    }

    @Test
    public void testReplaceFightSuccess() {
        Long fightId = 1L;
        FightEntity existingFight = new FightEntity();
        ReplaceFightRequest replaceRequest = new ReplaceFightRequest();
        replaceRequest.setFirstParticipantId(2L);
        replaceRequest.setSecondParticipantId(3L);
        replaceRequest.setWinner(2L);
        replaceRequest.setFirstParticipantPoints(10);
        replaceRequest.setSecondParticipantPoints(5);
        replaceRequest.setFirstParticipantCards(1);
        replaceRequest.setSecondParticipantCards(2);
        replaceRequest.setDoubles(0);
        replaceRequest.setStatus(FightStatus.FINISHED);

        TournamentParticipantEntity firstParticipant = new TournamentParticipantEntity();
        TournamentParticipantEntity secondParticipant = new TournamentParticipantEntity();
        TournamentParticipantEntity winner = firstParticipant;

        when(fightRepo.findById(fightId)).thenReturn(Optional.of(existingFight));
        when(participantService.searchForParticipant(2L)).thenReturn(firstParticipant);
        when(participantService.searchForParticipant(3L)).thenReturn(secondParticipant);
        when(participantService.searchForParticipant(2L)).thenReturn(winner);

        fightService.replaceFight(fightId, replaceRequest);

        assertEquals(firstParticipant, existingFight.getFirstParticipant());
        assertEquals(secondParticipant, existingFight.getSecondParticipant());
        assertEquals(10, existingFight.getFirstParticipantPoints());
        assertEquals(5, existingFight.getSecondParticipantPoints());
        assertEquals(1, existingFight.getFirstParticipantCards());
        assertEquals(2, existingFight.getSecondParticipantCards());
        assertEquals(FightStatus.FINISHED, existingFight.getStatus());
        verify(fightRepo, times(1)).save(existingFight);
    }


    @Test
    public void testDeleteFight() {
        Long fightId = 1L;
        fightService.deleteFight(fightId);
        verify(fightRepo, times(1)).deleteById(fightId);
    }


}
