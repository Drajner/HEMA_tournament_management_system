package drajner.hetman.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name="sessions")
@Getter
@Setter
public class SessionEntity {
    public SessionEntity() { }
    public SessionEntity(String sessionId, int userId){
        this.sessionId = sessionId;
        this.userId = userId;
    }
    @Id
    private String sessionId;
    private int userId;
}