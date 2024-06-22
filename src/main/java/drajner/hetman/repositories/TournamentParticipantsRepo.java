package drajner.hetman.repositories;

import drajner.hetman.entities.TournamentParticipantEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TournamentParticipantsRepo extends JpaRepository<TournamentParticipantEntity, Long> {
    List<TournamentParticipantEntity> findByTournamentTournamentId(Long tournamentId);

    List<TournamentParticipantEntity> findByGroupParticipationsGroupId(Long groupId);

}
