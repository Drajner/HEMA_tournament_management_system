package drajner.hetman;

import drajner.hetman.entities.FightEntity;
import drajner.hetman.entities.FinalsTreeNodeEntity;
import drajner.hetman.entities.TournamentParticipantEntity;
import drajner.hetman.errors.UnfinishedFightException;
import drajner.hetman.repositories.FightRepo;
import drajner.hetman.repositories.FinalsTreeNodeRepo;
import drajner.hetman.requests.Person;
import drajner.hetman.services.FinalsTreeNodeService;
import drajner.hetman.services.FightService;
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

public class FinalsTreeNodeServiceTest {

    @InjectMocks
    private FinalsTreeNodeService finalsTreeNodeService;

    @Mock
    private FinalsTreeNodeRepo finalsTreeNodeRepo;

    @Mock
    private FightRepo fightRepo;

    @Mock
    private FightService fightService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testFillNodeWithPendingFight() throws UnfinishedFightException {
        FinalsTreeNodeEntity node = new FinalsTreeNodeEntity();
        FightEntity fight = new FightEntity();
        fight.setStatus(FightStatus.PENDING);
        node.setFight(fight);

        TournamentParticipantEntity firstWinner = new TournamentParticipantEntity(new Person("test", "man"));
        firstWinner.setParticipantId(1L);
        TournamentParticipantEntity secondWinner = new TournamentParticipantEntity(new Person("man", "test"));
        secondWinner.setParticipantId(2L);

        FinalsTreeNodeEntity firstChild = new FinalsTreeNodeEntity();
        FightEntity firstChildFight = new FightEntity();
        firstChildFight.setStatus(FightStatus.FINISHED);
        firstChildFight.setWinner(firstWinner);
        firstChild.setFight(firstChildFight);
        node.setFirstChildNode(firstChild);

        FinalsTreeNodeEntity secondChild = new FinalsTreeNodeEntity();
        FightEntity secondChildFight = new FightEntity();
        secondChildFight.setStatus(FightStatus.FINISHED);
        secondChildFight.setWinner(secondWinner);
        secondChild.setFight(secondChildFight);
        node.setSecondChildNode(secondChild);

        finalsTreeNodeService.fillNode(node);

        verify(fightService, times(4)).evaluateFight(any(FightEntity.class), eq(1f));
        verify(finalsTreeNodeRepo, times(1)).save(node);
        assertEquals(fight.getFirstParticipant().getFullName(), "test man");
        assertEquals(fight.getSecondParticipant().getFullName(), "man test");

    }


    @Test
    public void testFillNodeWithFinishedFight() throws UnfinishedFightException {
        FinalsTreeNodeEntity node = new FinalsTreeNodeEntity();
        FightEntity fight = new FightEntity();
        fight.setStatus(FightStatus.FINISHED);
        node.setFight(fight);

        finalsTreeNodeService.fillNode(node);

        assertEquals(FightStatus.FINISHED, fight.getStatus());
        verify(fightService, times(1)).evaluateFight(any(FightEntity.class), eq(1f));
        verify(finalsTreeNodeRepo, times(1)).save(node);
    }

    @Test
    public void testSetUpTreeWithTwoParticipants() {
        FinalsTreeNodeEntity node = new FinalsTreeNodeEntity();
        List<TournamentParticipantEntity> participants = List.of(new TournamentParticipantEntity(), new TournamentParticipantEntity());

        finalsTreeNodeService.setUpTree(node, participants);

        verify(fightRepo, times(1)).save(any(FightEntity.class));
        verify(finalsTreeNodeRepo, times(1)).save(node);
    }

    @Test
    public void testSetUpTreeWithThreeParticipants() {
        FinalsTreeNodeEntity node = new FinalsTreeNodeEntity();
        List<TournamentParticipantEntity> participants = List.of(
                new TournamentParticipantEntity(),
                new TournamentParticipantEntity(),
                new TournamentParticipantEntity()
        );

        finalsTreeNodeService.setUpTree(node, participants);

        verify(fightRepo, times(3)).save(any(FightEntity.class));
        verify(finalsTreeNodeRepo, times(4)).save(any(FinalsTreeNodeEntity.class));
    }

    @Test
    public void testSetUpTreeWithMoreThanThreeParticipants() {
        FinalsTreeNodeEntity node = new FinalsTreeNodeEntity();
        List<TournamentParticipantEntity> participants = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            participants.add(new TournamentParticipantEntity());
        }

        finalsTreeNodeService.setUpTree(node, participants);

        verify(fightRepo, times(7)).save(any(FightEntity.class));
        verify(finalsTreeNodeRepo, times(19)).save(any(FinalsTreeNodeEntity.class));
    }


    @Test
    public void testPurgeTree() {
        FinalsTreeNodeEntity node = new FinalsTreeNodeEntity();
        node.setNodeId(1L);
        when(finalsTreeNodeRepo.findById(node.getNodeId())).thenReturn(java.util.Optional.of(node));

        finalsTreeNodeService.purgeTree(node);

        verify(finalsTreeNodeRepo, times(1)).deleteById(node.getNodeId());
    }

    @Test
    public void testPurgeFights() {
        FinalsTreeNodeEntity node = new FinalsTreeNodeEntity();
        FightEntity fight = new FightEntity();
        fight.setId(1L);
        node.setFight(fight);
        FinalsTreeNodeEntity firstChild = new FinalsTreeNodeEntity();
        FinalsTreeNodeEntity secondChild = new FinalsTreeNodeEntity();
        node.setFirstChildNode(firstChild);
        node.setSecondChildNode(secondChild);

        firstChild.setFight(new FightEntity());
        secondChild.setFight(new FightEntity());

        when(fightRepo.findById(anyLong())).thenReturn(java.util.Optional.of(new FightEntity()));

        finalsTreeNodeService.purgeFights(node);

        verify(fightRepo, times(1)).deleteById(anyLong());
        verify(finalsTreeNodeRepo, times(1)).save(node);
    }

}
