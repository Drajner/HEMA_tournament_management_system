package drajner.hetman.repositories;

import drajner.hetman.entities.TournamentParticipantEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TournamentParticipantsRepo extends JpaRepository<TournamentParticipantEntity, Long> {
}
