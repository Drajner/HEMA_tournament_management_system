package drajner.hetman.controllers;

import drajner.hetman.TournamentsSingleton;
import drajner.hetman.entities.TournamentParticipantEntity;
import drajner.hetman.repositories.TournamentParticipantsRepo;
import drajner.hetman.services.*;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping("participants/tournament/{tournamentId}")
@Log4j2
public class ParticipantController {

    @Autowired
    TournamentParticipantsRepo tournamentParticipantsRepo;

    @GetMapping("/get")
    public ResponseEntity<Object> getTournamentParticipants(@PathVariable Long tournamentId){
        try{
            return ResponseEntity.ok(tournamentParticipantsRepo.findByTournamentId(tournamentId));
        }catch(Exception e){
            return ResponseEntity.internalServerError().body(e);
        }
    }

    @PostMapping("/add")
    public ResponseEntity<Object> addTournamentParticipant(@RequestBody Person person, @PathVariable Long tournamentId){
        try{
            TournamentService.addParticipant(tournamentId, person);
            return ResponseEntity.ok().build();
        }catch(Exception e){
            return ResponseEntity.internalServerError().body(e);
        }
    }

    @DeleteMapping("/delete/{participantId}")
    public ResponseEntity<Object> deleteParticipant(@PathVariable Long tournamentId, @PathVariable Long participantId){
        try{
            TournamentService.removeParticipant(participantId);
            return ResponseEntity.ok().build();
        }catch(Exception e){
            return ResponseEntity.internalServerError().body(e);
        }
    }

    @PostMapping("/replace")
    public ResponseEntity<Object> replaceParticipant(@RequestBody TournamentParticipantEntity participant,
                                                     @PathVariable Long tournamentId){
        try{
            TournamentService.replaceParticipant(participant);
            return ResponseEntity.ok().build();
        }catch(Exception e){
            return ResponseEntity.internalServerError().body(e);
        }
    }

    @PostMapping("/disqualify/{participantId}")
    public ResponseEntity<Object> disqualifyParticipant(@PathVariable Long tournamentNumber,
                                                        @PathVariable Long participantId){
        try{
            ParticipantService.disqualify(participantId);
            return ResponseEntity.ok().build();
        }catch(Exception e){
            return ResponseEntity.internalServerError().body(e);
        }
    }

    @PostMapping("/compete/{participantId}")
    public ResponseEntity<Object> competeParticipant(@PathVariable Long tournamentNumber,
                                                     @PathVariable Long participantId){
        try{
            ParticipantService.compete(participantId);
            return ResponseEntity.ok().build();
        }catch(Exception e){
            return ResponseEntity.internalServerError().body(e);
        }
    }

    @PostMapping("/eliminate/{participantId}")
    public ResponseEntity<Object> eliminateParticipant(@PathVariable Long tournamentNumber,
                                                       @PathVariable Long participantId){
        try{
            ParticipantService.eliminate(participantId);
            return ResponseEntity.ok().build();
        }catch(Exception e){
            return ResponseEntity.internalServerError().body(e);
        }
    }

}
