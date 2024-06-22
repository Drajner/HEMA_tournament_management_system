package drajner.hetman.repositories;

import drajner.hetman.entities.GroupEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GroupRepo extends JpaRepository<GroupEntity, Long> {
    List<GroupEntity> findByTournamentTournamentId(Long tournamentId);
}
