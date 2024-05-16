package drajner.hetman.controllers;

import drajner.hetman.entities.FightEntity;
import drajner.hetman.TournamentsSingleton;
import drajner.hetman.repositories.FightRepo;
import drajner.hetman.requests.FightReport;
import drajner.hetman.services.Fight;
import drajner.hetman.services.FightService;
import drajner.hetman.services.GroupService;
import drajner.hetman.services.TournamentParticipant;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping("fights")
@Log4j2
public class FightController {

    @Autowired
    FightRepo fightRepo;

    @GetMapping("/get/{groupId}")
    public ResponseEntity<Object> getFights(@PathVariable Long groupId){
        try {
            return ResponseEntity.ok(fightRepo.findByGroupId(groupId));
        }catch(Exception e){
            return ResponseEntity.internalServerError().body(e);
        }
    }

    @DeleteMapping("delete/{fightId}")
    public ResponseEntity<Object> deleteFight(@PathVariable Long fightId){
        try {
            FightService.deleteFight(fightId);
            return ResponseEntity.ok().build();
        }catch(Exception e){
            return ResponseEntity.internalServerError().body(e);
        }
    }

    @PostMapping("replace/{fightId}")
    public ResponseEntity<Object> replaceFight(@RequestBody FightEntity fight, @PathVariable Long fightId){

        try {
            FightService.replaceFight(fightId, fight);
            return ResponseEntity.ok().build();
        }catch(Exception e){
            return ResponseEntity.internalServerError().body(e);
        }
    }


}
