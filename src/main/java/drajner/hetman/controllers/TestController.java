package drajner.hetman.controllers;

import drajner.hetman.services.Person;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.*;

@RestController
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
