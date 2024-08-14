package drajner.hetman.controllers;

import drajner.hetman.entities.TournamentParticipantEntity;
import drajner.hetman.repositories.TournamentParticipantsRepo;
import drajner.hetman.requests.ErrorResponse;
import drajner.hetman.requests.Person;
import drajner.hetman.services.*;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("participants")
@Log4j2
public class ParticipantController {

    @Autowired
    TournamentParticipantsRepo tournamentParticipantsRepo;

    @Autowired
    TournamentService tournamentService;

    @Autowired
    ParticipantService participantService;

    @GetMapping("/get/tournament/{tournamentId}")
    public ResponseEntity<Object> getTournamentParticipants(@PathVariable Long tournamentId){
        try{
            return ResponseEntity.ok(tournamentParticipantsRepo.findByTournamentTournamentId(tournamentId));
        }catch(Exception e){
            log.error(e);
            ErrorResponse response = new ErrorResponse(e.getClass().getSimpleName(), e.getMessage());
            return ResponseEntity.internalServerError().body(response);        }
    }

    @GetMapping("/get/group/{groupId}")
    public ResponseEntity<Object> getGroupParticipants(@PathVariable Long groupId){
        try{
            return ResponseEntity.ok(tournamentParticipantsRepo.findByGroupParticipationsGroupId(groupId));
        }catch(Exception e){
            log.error(e);
            ErrorResponse response = new ErrorResponse(e.getClass().getSimpleName(), e.getMessage());
            return ResponseEntity.internalServerError().body(response);        }
    }

    @PostMapping("/add/tournament/{tournamentId}")
    public ResponseEntity<Object> addTournamentParticipant(@RequestBody Person person, @PathVariable Long tournamentId){
        try{
            tournamentService.addParticipant(tournamentId, person);
            return ResponseEntity.ok().build();
        }catch(Exception e){
            log.error(e);
            ErrorResponse response = new ErrorResponse(e.getClass().getSimpleName(), e.getMessage());
            return ResponseEntity.internalServerError().body(response);        }
    }

    @DeleteMapping("/delete/{participantId}")
    public ResponseEntity<Object> deleteParticipant(@PathVariable Long participantId){
        try{
            tournamentService.removeParticipant(participantId);
            return ResponseEntity.ok().build();
        }catch(Exception e){
            log.error(e);
            ErrorResponse response = new ErrorResponse(e.getClass().getSimpleName(), e.getMessage());
            return ResponseEntity.internalServerError().body(response);        }
    }

    @PostMapping("/replace")
    public ResponseEntity<Object> replaceParticipant(@RequestBody TournamentParticipantEntity participant){
        try{
            tournamentService.replaceParticipant(participant);
            return ResponseEntity.ok().build();
        }catch(Exception e){
            log.error(e);
            ErrorResponse response = new ErrorResponse(e.getClass().getSimpleName(), e.getMessage());
            return ResponseEntity.internalServerError().body(response);        }
    }

    @PostMapping("/disqualify/{participantId}")
    public ResponseEntity<Object> disqualifyParticipant(@PathVariable Long participantId){
        try{
            participantService.disqualify(participantId);
            return ResponseEntity.ok().build();
        }catch(Exception e){
            log.error(e);
            ErrorResponse response = new ErrorResponse(e.getClass().getSimpleName(), e.getMessage());
            return ResponseEntity.internalServerError().body(response);        }
    }

    @PostMapping("/compete/{participantId}")
    public ResponseEntity<Object> competeParticipant(@PathVariable Long participantId){
        try{
            participantService.compete(participantId);
            return ResponseEntity.ok().build();
        }catch(Exception e){
            log.error(e);
            ErrorResponse response = new ErrorResponse(e.getClass().getSimpleName(), e.getMessage());
            return ResponseEntity.internalServerError().body(response);        }
    }

    @PostMapping("/eliminate/{participantId}")
    public ResponseEntity<Object> eliminateParticipant(@PathVariable Long participantId){
        try{
            participantService.eliminate(participantId);
            return ResponseEntity.ok().build();
        }catch(Exception e){
            log.error(e);
            ErrorResponse response = new ErrorResponse(e.getClass().getSimpleName(), e.getMessage());
            return ResponseEntity.internalServerError().body(response);        }
    }

}
