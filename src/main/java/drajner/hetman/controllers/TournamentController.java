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
@RequestMapping("tournaments/{tournamentNumber}")
@Log4j2
public class TournamentController {

    @GetMapping("/get")
    public Tournament getTournament(@PathVariable int tournamentNumber){
        return TournamentsSingleton.get(tournamentNumber);
    }

    @GetMapping("/participants")
    public ArrayList<TournamentParticipant> getTournamentParticipants(@PathVariable int tournamentNumber){
        return TournamentsSingleton.get(tournamentNumber).getParticipants();
    }

    @PostMapping("/participants/add")
    public void addTournamentParticipant(@RequestBody Person person, @PathVariable int tournamentNumber){
        TournamentsSingleton.get(tournamentNumber).addParticipant(person);
    }

    @DeleteMapping("participants/delete/{number}")
    public void deleteParticipant(@PathVariable int tournamentNumber, @PathVariable int number){
        TournamentsSingleton.get(tournamentNumber).removeParticipant(number);
    }

    @PostMapping("/participants/replace/{number}")
    public void addTournamentParticipant(@RequestBody TournamentParticipant participant, @PathVariable int tournamentNumber, @PathVariable int number){
        TournamentsSingleton.get(tournamentNumber).replaceParticipant(number, participant);
    }

    @GetMapping("/groups")
    public ArrayList<TournamentParticipant> getGroups(@PathVariable int tournamentNumber){
        return TournamentsSingleton.get(tournamentNumber).getParticipants();
    }

    @PostMapping("/groups/add/ladder")
    public void addLadderGroup(@PathVariable int tournamentNumber) throws OneFinalsException {
        TournamentsSingleton.get(tournamentNumber).addGroupLadder();
    }

    @PostMapping("/groups/add/pool")
    public void addPoolGroup(@RequestBody Person person, @PathVariable int tournamentNumber){
        TournamentsSingleton.get(tournamentNumber).addGroupPool();
    }

    @DeleteMapping("groups/delete/{number}")
    public void deleteGroup(@PathVariable int tournamentNumber, @PathVariable int number){
        TournamentsSingleton.get(tournamentNumber).getGroups().remove(number);
    }

    @PostMapping("/rename")
    public void changeName(@RequestBody String string, @PathVariable int tournamentNumber){
        TournamentsSingleton.get(tournamentNumber).setName(string);

    }

}
