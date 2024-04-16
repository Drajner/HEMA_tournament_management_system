package drajner.hetman.authentication;

import lombok.Getter;

@Getter
public class User {
    String username;
    String passwordHash;
    String email;
    UserPermissons userPermissons;
}
