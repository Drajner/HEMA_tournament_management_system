package drajner.hetman.requests;

import org.springframework.http.HttpStatus;

public class GenericResponse {

    HttpStatus status;
    Exception exception;
    Object object;


    public GenericResponse(HttpStatus status){
        this.status = status;
    }

    public GenericResponse(Object object){
        this.status = HttpStatus.OK;
        this.object = object;
    }

    public GenericResponse(HttpStatus status, Exception exception){
        this.status = status;
        this.exception = exception;
    }
    public GenericResponse(HttpStatus status, Exception exception, Object object){
        this.status = status;
        this.exception = exception;
        this.object = object;
    };



}
