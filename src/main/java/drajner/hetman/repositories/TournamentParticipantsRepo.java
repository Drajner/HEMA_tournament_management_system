package drajner.hetman.repositories;

import drajner.hetman.entities.GroupEntity;
import drajner.hetman.entities.TournamentParticipantEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TournamentParticipantsRepo extends JpaRepository<TournamentParticipantEntity, Long> {
    List<TournamentParticipantEntity> findByTournamentId(Long tournamentId);

    List<TournamentParticipantEntity> findByGroupId(Long groupId);

}
