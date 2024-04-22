package drajner.hetman.repositories;

import drajner.hetman.entities.TournamentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TournamentRepo extends JpaRepository<TournamentEntity, Long> {
}
