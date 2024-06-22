package drajner.hetman.repositories;

import drajner.hetman.entities.FightEntity;
import drajner.hetman.entities.GroupEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FightRepo extends JpaRepository<FightEntity, Long> {
    List<FightEntity> findByGroupGroupId(Long groupId);
}
