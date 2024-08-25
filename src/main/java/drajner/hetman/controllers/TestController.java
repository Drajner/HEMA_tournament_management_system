package drajner.hetman.controllers;

import drajner.hetman.requests.Person;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = {"http://localhost:5173"}, allowCredentials = "true")
@Log4j2
public class TestController {

    @RequestMapping("/dupa")
    public Person dupa(){
        Person p = new Person("Aaa", "Bbb", "PNDU");
        log.info("dupa");
        //HetmanLogger.warn("dupa");
        return p;
    }

    @RequestMapping("/testError")
    public void testError() throws Exception{
        throw new Exception("dupa");
    }

}
