package drajner.hetman.repositories;

import drajner.hetman.entities.GroupEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GroupRepo extends JpaRepository<GroupEntity, Long> {
}
