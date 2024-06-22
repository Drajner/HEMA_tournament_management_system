package drajner.hetman.requests;

import lombok.Getter;

@Getter
public class ErrorResponse {
    String exceptionName;
    String message;

    public ErrorResponse(String exceptionName, String message){
        this.exceptionName = exceptionName;
        this.message = message;
    }
}
