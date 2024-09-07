package drajner.hetman.requests;

import drajner.hetman.status.UserStatus;

public class LoginResponse {
    public LoginResponse(String token, UserStatus status)
    {
        this.token = token;
        this.status = status;
    }
    public String token;
    public UserStatus status;
}
