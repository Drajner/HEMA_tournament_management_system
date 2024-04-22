package drajner.hetman.repositories;

import drajner.hetman.entities.FightEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FightRepo extends JpaRepository<FightEntity, Long> {
}
