package drajner.hetman.requests;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequest {
    private String username;
    private String password;
    private boolean admin;

    public LoginRequest() {
    }

    public LoginRequest(String username, String password, boolean admin) {
        this.username = username;
        this.password = password;
        this.admin = admin;
    }
}
