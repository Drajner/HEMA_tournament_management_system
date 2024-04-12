package drajner.hetman.controllers;

import drajner.hetman.TournamentsSingleton;
import drajner.hetman.requests.TournamentAddRequest;
import drajner.hetman.services.Person;
import drajner.hetman.services.Tournament;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

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

    /*
    @RequestMapping("/tournaments")
    public ArrayList<Tournament> getTournaments() throws Exception{
        return TournamentsSingleton.getInstance().tournaments;
    }

    @RequestMapping("/tournaments/{number}")
    public Tournament getTournament(@PathVariable int number){
        return TournamentsSingleton.get(number);
    }

    @RequestMapping("/tournaments/add/{name}")
    public void addTournamentGet(@PathVariable String name){
        Tournament newTournament = new Tournament(name);
        TournamentsSingleton.add(newTournament);
    }

    @PostMapping("tournaments/add")
    public void addTournament(@RequestBody String name){
        Tournament newTournament = new Tournament(name);
        TournamentsSingleton.add(newTournament);
    }

    */
}
