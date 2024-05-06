package drajner.hetman.repositories;

import drajner.hetman.entities.FightEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FightRepo extends JpaRepository<FightEntity, Long> {
    List<FightEntity> findByGroupId(Long groupId);
}
