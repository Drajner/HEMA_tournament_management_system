package drajner.hetman.repositories;

import drajner.hetman.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface UserRepo extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByUsername(String username);
}