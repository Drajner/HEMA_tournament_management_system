package drajner.hetman.controllers;

import drajner.hetman.entities.TournamentEntity;
import drajner.hetman.errors.OneFinalsException;
import drajner.hetman.errors.UnfinishedFightException;
import drajner.hetman.errors.WrongAmountException;
import drajner.hetman.repositories.GroupRepo;
import drajner.hetman.repositories.TournamentRepo;
import drajner.hetman.requests.ErrorResponse;
import drajner.hetman.services.*;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;

@RestController
@CrossOrigin(origins = {"http://localhost:5173"}, allowCredentials = "true")
@RequestMapping("tournaments")
@Log4j2
public class TournamentController {

    @Autowired
    TournamentRepo tournamentRepo;

    @Autowired
    TournamentService tournamentService;

    @GetMapping("/get")
    public ResponseEntity<Object> getTournaments(){
        try {
            return ResponseEntity.ok(tournamentRepo.findAll());
        }catch(Exception e){
            log.error(e);
            ErrorResponse response = new ErrorResponse(e.getClass().getSimpleName(), e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }

    @GetMapping("/get/{tournamentId}")
    public ResponseEntity<Object> getTournament(@PathVariable Long tournamentId){
        try {
            return ResponseEntity.ok(tournamentRepo.findById(tournamentId));
        }catch(Exception e){
            log.error(e);
            ErrorResponse response = new ErrorResponse(e.getClass().getSimpleName(), e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }

    @PostMapping("/add")
    public ResponseEntity<Object> addTournament(@RequestBody String name){
        try {
            TournamentEntity newTournament = new TournamentEntity(name);
            return ResponseEntity.ok(tournamentRepo.save(newTournament));
        }catch(Exception e){
            log.error(e);
            ErrorResponse response = new ErrorResponse(e.getClass().getSimpleName(), e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }


    @DeleteMapping("/delete/{tournamentId}")
    public ResponseEntity<Object> deleteTournament(@PathVariable Long tournamentId)
    {
        try {
            tournamentRepo.deleteById(tournamentId);
            return ResponseEntity.ok().build();
        }catch(Exception e){
            log.error(e);
            ErrorResponse response = new ErrorResponse(e.getClass().getSimpleName(), e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }

    }

    @PostMapping("/rename/{tournamentId}")
    public ResponseEntity<Object> changeName(@RequestBody String tournamentName, @PathVariable Long tournamentId){
        try {
            TournamentEntity editedTournament = tournamentRepo.findById(tournamentId).get();
            editedTournament.setName(tournamentName);
            return ResponseEntity.ok(tournamentRepo.save(editedTournament));
        }catch(NoSuchElementException e){
            log.error(e);
            ErrorResponse response = new ErrorResponse(e.getClass().getSimpleName(), e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
        catch(Exception e){
            log.error(e);
            ErrorResponse response = new ErrorResponse(e.getClass().getSimpleName(), e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }

    @PostMapping("/generateGroups/{tournamentId}")
    public ResponseEntity<Object> generateGroups(@RequestBody int numberOfGroups, @PathVariable Long tournamentId) throws WrongAmountException{
        try {
            tournamentService.createGroups(tournamentId, numberOfGroups);
            return ResponseEntity.ok().build();
        }catch(WrongAmountException e){
            log.error(e);
            ErrorResponse response = new ErrorResponse(e.getClass().getSimpleName(), e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }catch(Exception e){
            log.error(e);
            ErrorResponse response = new ErrorResponse(e.getClass().getSimpleName(), e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }

    @PostMapping("/generateLadder/{tournamentId}")
    public ResponseEntity<Object> generateLadder(@RequestBody int numberOfParticipants, @PathVariable Long tournamentId){
        try {
            tournamentService.createLadder(tournamentId, numberOfParticipants);
            return ResponseEntity.ok().build();
        }catch(WrongAmountException|OneFinalsException e){
            log.error(e);
            ErrorResponse response = new ErrorResponse(e.getClass().getSimpleName(), e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }catch(Exception e){
            log.error(e);
            ErrorResponse response = new ErrorResponse(e.getClass().getSimpleName(), e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }

    @GetMapping("/getFinals/{tournamentId}")
    public ResponseEntity<Object> getFinals(@PathVariable Long tournamentId){
        try {
            return ResponseEntity.ok(tournamentService.getFinals(tournamentId));
        }catch(Exception e){
            log.error(e);
            ErrorResponse response = new ErrorResponse(e.getClass().getSimpleName(), e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }

    @PostMapping("/addPool/{tournamentId}")
    public ResponseEntity<Object> addPool(@PathVariable Long tournamentId){
        try {
            tournamentService.addGroupPool(tournamentId);
            return ResponseEntity.ok().build();
        }catch(Exception e){
            log.error(e);
            ErrorResponse response = new ErrorResponse(e.getClass().getSimpleName(), e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }

    @PostMapping("evaluateFinals/{tournamentId}")
    public ResponseEntity<Object> evaluateFinals(@PathVariable Long tournamentId) throws UnfinishedFightException {
        try {
            tournamentService.evaluateFinals(tournamentId);
            return ResponseEntity.ok().build();
        }catch(Exception e){
            log.error(e);
            ErrorResponse response = new ErrorResponse(e.getClass().getSimpleName(), e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }

    @DeleteMapping("/purgeFinals/{tournamentId}")
    public ResponseEntity<Object> purgeFinals(@PathVariable Long tournamentId)
    {
        try {
            tournamentService.purgeFinals(tournamentId);
            return ResponseEntity.ok().build();
        }catch(Exception e){
            log.error(e);
            ErrorResponse response = new ErrorResponse(e.getClass().getSimpleName(), e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }
}
