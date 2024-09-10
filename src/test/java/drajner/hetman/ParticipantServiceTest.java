package drajner.hetman;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import drajner.hetman.repositories.TournamentParticipantsRepo;
import drajner.hetman.services.ParticipantService;
import drajner.hetman.entities.TournamentParticipantEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.NoSuchElementException;
import java.util.Optional;

@SpringBootTest
public class ParticipantServiceTest {

    @Mock
    private TournamentParticipantsRepo tournamentParticipantsRepo;

    @InjectMocks
    private ParticipantService participantService;

    @Mock
    private TournamentParticipantEntity testParticipant;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testSearchForParticipantSuccess() {
        when(tournamentParticipantsRepo.findById(1L)).thenReturn(Optional.of(testParticipant));

        TournamentParticipantEntity participant = participantService.searchForParticipant(1L);

        verify(tournamentParticipantsRepo, times(1)).findById(1L);
        assert(participant != null);
    }

    @Test
    public void testSearchForParticipantFailure() {
        when(tournamentParticipantsRepo.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> participantService.searchForParticipant(1L));
    }

    @Test
    public void testDisqualifyParticipant() {
        when(tournamentParticipantsRepo.findById(1L)).thenReturn(Optional.of(testParticipant));

        participantService.disqualify(1L);

        verify(testParticipant, times(1)).disqualify();
        verify(tournamentParticipantsRepo, times(1)).save(testParticipant);
    }

    @Test
    public void testCompeteParticipant() {
        when(tournamentParticipantsRepo.findById(1L)).thenReturn(Optional.of(testParticipant));

        participantService.compete(1L);

        verify(testParticipant, times(1)).compete();
        verify(tournamentParticipantsRepo, times(1)).save(testParticipant);
    }

    @Test
    public void testEliminateParticipant() {
        when(tournamentParticipantsRepo.findById(1L)).thenReturn(Optional.of(testParticipant));

        participantService.eliminate(1L);

        verify(testParticipant, times(1)).eliminate();
        verify(tournamentParticipantsRepo, times(1)).save(testParticipant);
    }
}
