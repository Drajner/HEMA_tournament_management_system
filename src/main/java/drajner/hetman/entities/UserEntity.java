package drajner.hetman.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name="users")
@Getter
@Setter
public class UserEntity {
    public UserEntity() { }

    public UserEntity(int userId, String username, String password, boolean isAdmin)
    {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.isAdmin = isAdmin;
    }

    public UserEntity(String username, String password)
    {
        this.username = username;
        this.password = password;
        this.isAdmin = false;
    }

    @Id
    @GeneratedValue
    private int userId;
    private String username;
    private String password;

    boolean isAdmin;
}