package drajner.hetman.requests;

public class LoginResponse {
    public LoginResponse(boolean status, String sessionId)
    {
        this.status = status;
        this.sessionId = sessionId;
    }

    public boolean status;
    public String sessionId;
}
