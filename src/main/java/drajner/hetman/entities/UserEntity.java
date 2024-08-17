package drajner.hetman.entities;

import drajner.hetman.services.UserStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name="users")
@Getter
@Setter
public class UserEntity {

    @Id
    @GeneratedValue
    private Long userId;
    private String username;
    private String password;

    @Enumerated(EnumType.ORDINAL)
    UserStatus userStatus;

    public UserEntity() { }

    public UserEntity(String username, String password, UserStatus userStatus)
    {
        this.username = username;
        this.password = password;
        this.userStatus = userStatus;
    }

    public UserEntity(String username, String password)
    {
        this.username = username;
        this.password = password;
        this.userStatus = UserStatus.STANDARD;
    }
}
