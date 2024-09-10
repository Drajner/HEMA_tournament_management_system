package drajner.hetman;
import drajner.hetman.entities.*;
import drajner.hetman.errors.*;
import drajner.hetman.repositories.*;
import drajner.hetman.requests.AddFightRequest;
import drajner.hetman.services.FightService;
import drajner.hetman.services.GroupService;
import drajner.hetman.services.ParticipantService;
import drajner.hetman.status.FightStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class GroupServiceTest {

    @InjectMocks
    private GroupService groupService;

    @Mock
    private GroupRepo groupRepo;

    @Mock
    private FightRepo fightRepo;

    @Mock
    private TournamentParticipantsRepo tournamentParticipantsRepo;

    @Mock
    private ParticipantService participantService;

    @Mock
    private FightService fightService;

    private GroupEntity groupEntity;
    private TournamentParticipantEntity participant1;
    private TournamentParticipantEntity participant2;
    private AddFightRequest addFightRequest;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        groupEntity = new GroupEntity();
        groupEntity.setGroupId(1L);
        participant1 = new TournamentParticipantEntity();
        participant1.setParticipantId(1L);
        participant1.setName("Participant 1");
        participant2 = new TournamentParticipantEntity();
        participant2.setParticipantId(2L);
        participant2.setName("Participant 2");
        groupEntity.setGroupParticipants(new ArrayList<>(Arrays.asList(participant1, participant2)));
        participant1.setGroupParticipations(new ArrayList<>(Arrays.asList(groupEntity)));
        participant2.setGroupParticipations(new ArrayList<>(Arrays.asList(groupEntity)));

        addFightRequest = new AddFightRequest();
        addFightRequest.setFirstParticipantId(1L);
        addFightRequest.setSecondParticipantId(2L);
    }

    @Test
    void testSearchForGroupExists() {
        when(groupRepo.findById(1L)).thenReturn(Optional.of(groupEntity));
        GroupEntity foundGroup = groupService.searchForGroup(1L);
        assertEquals(1L, foundGroup.getGroupId());
    }

    @Test
    void testSearchForGroupNotExists() {
        when(groupRepo.findById(1L)).thenReturn(Optional.empty());
        assertThrows(NoSuchElementException.class, () -> groupService.searchForGroup(1L));
    }

    @Test
    void testAutoGenerateFightsEvenParticipants() throws WrongAmountException {
        when(groupRepo.findById(1L)).thenReturn(Optional.of(groupEntity));

        groupService.autoGenerateFights(1L);
        verify(fightRepo, times(1)).save(any(FightEntity.class));
    }

    @Test
    void testAutoGenerateFightsOddParticipants() throws WrongAmountException {
        groupEntity.getGroupParticipants().add(null);
        when(groupRepo.findById(1L)).thenReturn(Optional.of(groupEntity));

        groupService.autoGenerateFights(1L);
        verify(fightRepo, times(1)).save(any(FightEntity.class));
    }

    @Test
    void testEvaluateGroup() throws UnfinishedFightException {
        FightEntity fight = new FightEntity(participant1, participant2);
        fight.setStatus(FightStatus.FINISHED);
        groupEntity.setGroupFights(Collections.singletonList(fight));
        when(groupRepo.findById(1L)).thenReturn(Optional.of(groupEntity));

        groupService.evaluateGroup(1L);
        verify(fightService, times(1)).evaluateFight(fight, groupEntity.getModifier());
    }

    @Test
    void testAddFightParticipantsInGroup() throws ImpossibleFightException {
        when(groupRepo.findById(1L)).thenReturn(Optional.of(groupEntity));
        when(participantService.searchForParticipant(1L)).thenReturn(participant1);
        when(participantService.searchForParticipant(2L)).thenReturn(participant2);

        groupService.addFight(1L, addFightRequest);
        verify(fightRepo, times(1)).save(any(FightEntity.class));
    }

    @Test
    void testAddFightParticipantsNotInGroup() {
        TournamentParticipantEntity participant3 = new TournamentParticipantEntity();
        participant3.setParticipantId(3L);
        when(groupRepo.findById(1L)).thenReturn(Optional.of(groupEntity));
        when(participantService.searchForParticipant(1L)).thenReturn(participant1);
        when(participantService.searchForParticipant(2L)).thenReturn(participant3);

        assertThrows(ImpossibleFightException.class, () -> groupService.addFight(1L, addFightRequest));
    }

    @Test
    void testAddParticipantSuccessfully() throws DuplicateException {
        when(groupRepo.findById(1L)).thenReturn(Optional.of(groupEntity));
        when(participantService.searchForParticipant(1L)).thenReturn(participant1);

        groupService.addParticipant(1L, 1L);
        verify(groupRepo, times(1)).save(groupEntity);
        verify(tournamentParticipantsRepo, times(1)).save(participant1);
    }

    @Test
    void testDeleteParticipantSuccessfully() {
        when(groupRepo.findById(1L)).thenReturn(Optional.of(groupEntity));
        when(participantService.searchForParticipant(1L)).thenReturn(participant1);

        groupService.deleteParticipant(1L, 1L);
        verify(groupRepo, times(1)).save(groupEntity);
        verify(tournamentParticipantsRepo, times(1)).save(participant1);
    }

    @Test
    void testDeleteParticipantNotInGroup() {
        TournamentParticipantEntity participant3 = new TournamentParticipantEntity();
        participant3.setParticipantId(3L);
        when(groupRepo.findById(1L)).thenReturn(Optional.of(groupEntity));
        when(participantService.searchForParticipant(3L)).thenReturn(participant3);

        assertThrows(NoSuchElementException.class, () -> groupService.deleteParticipant(1L, 3L));
    }

    @Test
    void testDeleteGroup() {
        FightEntity fight = new FightEntity(participant1, participant2);
        groupEntity.setGroupFights(Collections.singletonList(fight));
        when(groupRepo.findById(1L)).thenReturn(Optional.of(groupEntity));

        groupService.deleteGroup(1L);
        verify(fightRepo, times(1)).deleteById(fight.getId());
        verify(groupRepo, times(1)).deleteById(1L);
    }

    @Test
    void testSetModifierSuccessfully() {
        when(groupRepo.findById(1L)).thenReturn(Optional.of(groupEntity));

        groupService.setModifier(1L, 2.0f);
        assertEquals(2.0f, groupEntity.getModifier());
        verify(groupRepo, times(1)).save(groupEntity);
    }
}
