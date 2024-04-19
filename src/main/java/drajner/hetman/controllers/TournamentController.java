package drajner.hetman.controllers;

import drajner.hetman.TournamentsSingleton;
import drajner.hetman.errors.OneFinalsException;
import drajner.hetman.services.Person;
import drajner.hetman.services.Tournament;
import drajner.hetman.services.TournamentParticipant;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping("tournaments")
@Log4j2
public class TournamentController {

    @GetMapping("/get")
    public ArrayList<Tournament> getTournaments() throws Exception{
        return TournamentsSingleton.getInstance().tournaments;
    }

    @PostMapping("/add")
    public void addTournament(@RequestBody String name){
        Tournament newTournament = new Tournament(name);
        TournamentsSingleton.add(newTournament);
    }

    @DeleteMapping("/delete/{number}")
    public void deleteTournament(@PathVariable int number)
    {
        TournamentsSingleton.remove(number);
    }

    @PostMapping("/rename/{number}")
    public void changeName(@RequestBody String string, @PathVariable int number){
        TournamentsSingleton.get(number).setName(string);

    }

}
