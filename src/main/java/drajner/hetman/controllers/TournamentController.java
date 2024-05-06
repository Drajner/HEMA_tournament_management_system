package drajner.hetman.controllers;

import drajner.hetman.TournamentsSingleton;
import drajner.hetman.entities.TournamentEntity;
import drajner.hetman.entities.UserEntity;
import drajner.hetman.errors.DuplicateException;
import drajner.hetman.errors.OneFinalsException;
import drajner.hetman.errors.WrongAmountException;
import drajner.hetman.repositories.TournamentRepo;
import drajner.hetman.repositories.UserRepo;
import drajner.hetman.requests.GenericResponse;
import drajner.hetman.services.GroupFinals;
import drajner.hetman.services.Person;
import drajner.hetman.services.Tournament;
import drajner.hetman.services.TournamentParticipant;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@RestController
@RequestMapping("tournaments")
@Log4j2
public class TournamentController {

    @Autowired
    TournamentRepo tournamentRepo;

    @GetMapping("/get")
    public ResponseEntity<Object> getTournaments(){
        try {
            return ResponseEntity.ok(tournamentRepo.findAll());
        }catch(Exception e){
            return ResponseEntity.internalServerError().body(e);
        }
    }

    @PostMapping("/add")
    public ResponseEntity<Object> addTournament(@RequestBody String name){
        try {
            TournamentEntity newTournament = new TournamentEntity(name);
            return ResponseEntity.ok(tournamentRepo.save(newTournament));
        }catch(Exception e){
            return ResponseEntity.internalServerError().body(e);
        }
    }


    @DeleteMapping("/delete/{tournamentId}")
    public ResponseEntity<Object> deleteTournament(@PathVariable Long tournamentId)
    {
        try {
            tournamentRepo.deleteById(tournamentId);
            return ResponseEntity.ok().build();
        }catch(Exception e){
            return ResponseEntity.internalServerError().body(e);
        }

    }

    @PostMapping("/rename/{number}")
    public ResponseEntity<Object> changeName(@RequestBody String tournamentName, @PathVariable Long tournamentId){
        try {
            TournamentEntity editedTournament = tournamentRepo.findById(tournamentId).get();
            editedTournament.setName(tournamentName);
            return ResponseEntity.ok(tournamentRepo.save(editedTournament));
        }catch(NoSuchElementException e){
            return ResponseEntity.badRequest().body(e);
        }
        catch(Exception e){
            return ResponseEntity.internalServerError().body(e);
        }
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
