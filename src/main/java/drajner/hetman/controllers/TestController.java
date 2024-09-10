package drajner.hetman.controllers;

import drajner.hetman.requests.Person;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = {"http://localhost:5173"}, allowCredentials = "true")
@Log4j2
public class TestController {

    @RequestMapping("/testError")
    public void testError() throws Exception{
        throw new Exception("aaa");
    }

}
