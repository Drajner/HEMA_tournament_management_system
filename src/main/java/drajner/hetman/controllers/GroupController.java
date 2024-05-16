package drajner.hetman.controllers;

import drajner.hetman.TournamentsSingleton;
import drajner.hetman.entities.FightEntity;
import drajner.hetman.entities.GroupEntity;
import drajner.hetman.entities.TournamentParticipantEntity;
import drajner.hetman.errors.OneFinalsException;
import drajner.hetman.errors.UnfinishedFightException;
import drajner.hetman.errors.WrongAmountException;
import drajner.hetman.repositories.GroupRepo;
import drajner.hetman.requests.FightReport;
import drajner.hetman.services.*;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping("groups")
@Log4j2
public class GroupController {

    @Autowired
    GroupRepo groupRepo;

    @GetMapping("/get/{tournamentId}")
    public ResponseEntity<Object> getGroups(@PathVariable Long tournamentId){
        try {
            return ResponseEntity.ok(groupRepo.findByTournamentId(tournamentId));
        }catch(Exception e){
            return ResponseEntity.internalServerError().body(e);
        }
    }

    @DeleteMapping("delete/{groupId}")
    public ResponseEntity<Object> deleteGroup(@PathVariable Long groupId){
        try {
            GroupService.deleteGroup(groupId);
            return ResponseEntity.ok().build();
        }catch(Exception e){
            return ResponseEntity.internalServerError().body(e);
        }
    }

    @PostMapping("autoGenerateFights/{groupId}")
    public ResponseEntity<Object> autoGenerateFights(@PathVariable Long groupId) throws WrongAmountException {
        try {
            GroupService.autoGenerateFights(groupId);
            return ResponseEntity.ok().build();
        }catch(WrongAmountException e){
            return ResponseEntity.badRequest().body(e);
        }catch(Exception e){
            return ResponseEntity.internalServerError().body(e);
        }
    }

    @PostMapping("evaluateGroup/{groupId}")
    public ResponseEntity<Object> evaluateGroup(@PathVariable Long groupId){
        try {
            GroupService.evaluateGroup(groupId, 1); //rework modifier later
            return ResponseEntity.ok().build();
        }catch(Exception e){
            return ResponseEntity.internalServerError().body(e);
        }
    }

    @PostMapping("/addFight/{groupId}")
    public ResponseEntity<Object> addFight(@RequestBody FightEntity fight, @PathVariable Long groupId){
        try {
            GroupService.addFight(groupId, fight);
            return ResponseEntity.ok().build();
        }catch(Exception e){
            return ResponseEntity.internalServerError().body(e);
        }
    }

    @PostMapping("/addPartcipant/{groupId}")
    public ResponseEntity<Object> addParticipant(@RequestBody Long participantId, @PathVariable Long groupId){
        try {
            GroupService.addParticipant(groupId, participantId);
            return ResponseEntity.ok().build();
        }catch(Exception e){
            return ResponseEntity.internalServerError().body(e);
        }
    }

    @DeleteMapping("/deleteParticipant/{groupId}")
    public ResponseEntity<Object> deleteParticipant(@RequestBody Long participantId, @PathVariable Long groupId)
    {
        try {
            GroupService.deleteParticipant(groupId, participantId);
            return ResponseEntity.ok().build();
        }catch(Exception e){
            return ResponseEntity.internalServerError().body(e);
        }

    }


}
