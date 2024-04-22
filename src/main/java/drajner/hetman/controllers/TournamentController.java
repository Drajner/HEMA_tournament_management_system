package drajner.hetman.controllers;

import drajner.hetman.TournamentsSingleton;
import drajner.hetman.entities.UserEntity;
import drajner.hetman.errors.DuplicateException;
import drajner.hetman.errors.OneFinalsException;
import drajner.hetman.errors.WrongAmountException;
import drajner.hetman.repositories.UserRepo;
import drajner.hetman.services.GroupFinals;
import drajner.hetman.services.Person;
import drajner.hetman.services.Tournament;
import drajner.hetman.services.TournamentParticipant;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping("tournaments")
@Log4j2
public class TournamentController {

    @Autowired
    UserRepo userRepo;

    @GetMapping("/get")
    public ArrayList<Tournament> getTournaments(){
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

    @PostMapping("/generateGroups/{number}")
    public void generateGroups(@RequestBody int numberOfGroups, @PathVariable int number) throws DuplicateException, WrongAmountException{
        TournamentsSingleton.get(number).createGroups(numberOfGroups);

    }

    @PostMapping("/generateLadder/{number}")
    public void generateLadder(@RequestBody int numberOfParticipants, @PathVariable int number) throws WrongAmountException{
        TournamentsSingleton.get(number).createLadder(numberOfParticipants);

    }

    @GetMapping("/getFinals/{number}")
    public GroupFinals getFinals(@PathVariable int number){
        return TournamentsSingleton.get(number).getFinals();
    }
}
