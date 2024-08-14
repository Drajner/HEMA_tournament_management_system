package drajner.hetman.controllers;

import drajner.hetman.entities.FightEntity;
import drajner.hetman.errors.WrongAmountException;
import drajner.hetman.repositories.GroupRepo;
import drajner.hetman.requests.AddFightRequest;
import drajner.hetman.requests.ErrorResponse;
import drajner.hetman.services.*;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("groups")
@Log4j2
public class GroupController {

    @Autowired
    GroupRepo groupRepo;

    @Autowired
    GroupService groupService;

    @GetMapping("/get/{tournamentId}")
    public ResponseEntity<Object> getGroups(@PathVariable Long tournamentId){
        try {
            return ResponseEntity.ok(groupRepo.findByTournamentTournamentId(tournamentId));
        }catch(Exception e){
            log.error(e);
            ErrorResponse response = new ErrorResponse(e.getClass().getSimpleName(), e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }

    @DeleteMapping("delete/{groupId}")
    public ResponseEntity<Object> deleteGroup(@PathVariable Long groupId){
        try {
            groupService.deleteGroup(groupId);
            return ResponseEntity.ok().build();
        }catch(Exception e){
            log.error(e);
            ErrorResponse response = new ErrorResponse(e.getClass().getSimpleName(), e.getMessage());
            return ResponseEntity.internalServerError().body(response);        }
    }

    @PostMapping("setModifier/{groupId}")
    public ResponseEntity<Object> setModifier(@RequestBody float newModifier, @PathVariable Long tournamentId){
        try {
            groupService.setModifier(tournamentId, newModifier);
            return ResponseEntity.ok().build();
        }catch(Exception e){
            log.error(e);
            ErrorResponse response = new ErrorResponse(e.getClass().getSimpleName(), e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }

    @PostMapping("autoGenerateFights/{groupId}")
    public ResponseEntity<Object> autoGenerateFights(@PathVariable Long groupId) throws WrongAmountException {
        try {
            groupService.autoGenerateFights(groupId);
            return ResponseEntity.ok().build();
        }catch(WrongAmountException e){
            log.error(e);
            ErrorResponse response = new ErrorResponse(e.getClass().getSimpleName(), e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }catch(Exception e){
            log.error(e);
            ErrorResponse response = new ErrorResponse(e.getClass().getSimpleName(), e.getMessage());
            return ResponseEntity.internalServerError().body(response);        }
    }

    @PostMapping("evaluateGroup/{groupId}")
    public ResponseEntity<Object> evaluateGroup(@PathVariable Long groupId){
        try {
            groupService.evaluateGroup(groupId);
            return ResponseEntity.ok().build();
        }catch(Exception e){
            log.error(e);
            ErrorResponse response = new ErrorResponse(e.getClass().getSimpleName(), e.getMessage());
            return ResponseEntity.internalServerError().body(response);        }
    }

    @PostMapping("/addFight/{groupId}")
    public ResponseEntity<Object> addFight(@RequestBody AddFightRequest addFightRequest, @PathVariable Long groupId){
        try {
            groupService.addFight(groupId, addFightRequest);
            return ResponseEntity.ok().build();
        }catch(Exception e){
            log.error(e);
            ErrorResponse response = new ErrorResponse(e.getClass().getSimpleName(), e.getMessage());
            return ResponseEntity.internalServerError().body(response);        }
    }

    @PostMapping("/addParticipant/{groupId}")
    public ResponseEntity<Object> addParticipant(@RequestBody Long participantId, @PathVariable Long groupId){
        try {
            groupService.addParticipant(groupId, participantId);
            return ResponseEntity.ok().build();
        }catch(Exception e){
            log.error(e);
            ErrorResponse response = new ErrorResponse(e.getClass().getSimpleName(), e.getMessage());
            return ResponseEntity.internalServerError().body(response);        }
    }

    @DeleteMapping("/deleteParticipant/{groupId}")
    public ResponseEntity<Object> deleteParticipant(@RequestBody Long participantId, @PathVariable Long groupId)
    {
        try {
            groupService.deleteParticipant(groupId, participantId);
            return ResponseEntity.ok().build();
        }catch(Exception e){
            log.error(e);
            ErrorResponse response = new ErrorResponse(e.getClass().getSimpleName(), e.getMessage());
            return ResponseEntity.internalServerError().body(response);        }

    }

}
