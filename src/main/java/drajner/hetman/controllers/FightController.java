package drajner.hetman.controllers;

import drajner.hetman.entities.FightEntity;
import drajner.hetman.repositories.FightRepo;
import drajner.hetman.requests.ErrorResponse;
import drajner.hetman.requests.ReplaceFightRequest;
import drajner.hetman.services.FightService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("fights")
@Log4j2
public class FightController {

    @Autowired
    FightRepo fightRepo;

    @Autowired
    FightService fightService;

    @GetMapping("/get/{groupId}")
    public ResponseEntity<Object> getFights(@PathVariable Long groupId){
        try {
            return ResponseEntity.ok(fightRepo.findByGroupGroupId(groupId));
        }catch(Exception e){
            ErrorResponse response = new ErrorResponse(e.getClass().getSimpleName(), e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }

    @DeleteMapping("delete/{fightId}")
    public ResponseEntity<Object> deleteFight(@PathVariable Long fightId){
        try {
            fightService.deleteFight(fightId);
            return ResponseEntity.ok().build();
        }catch(Exception e){
            ErrorResponse response = new ErrorResponse(e.getClass().getSimpleName(), e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }

    @PostMapping("replace/{fightId}")
    public ResponseEntity<Object> replaceFight(@RequestBody ReplaceFightRequest fight, @PathVariable Long fightId){

        try {
            fightService.replaceFight(fightId, fight);
            return ResponseEntity.ok().build();
        }catch(Exception e){
            ErrorResponse response = new ErrorResponse(e.getClass().getSimpleName(), e.getMessage());
            return ResponseEntity.internalServerError().body(response);        }
    }


}
