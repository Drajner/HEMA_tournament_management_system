package drajner.hetman.controllers;

import drajner.hetman.TournamentsSingleton;
import drajner.hetman.errors.DuplicateException;
import drajner.hetman.services.Person;
import drajner.hetman.services.TournamentParticipant;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping("tournaments/{tournamentNumber}/groups/{groupNumber}/participants")
@Log4j2
public class GroupParticipantController {
    @GetMapping("/get")
    public ArrayList<TournamentParticipant> getTournamentParticipants(@PathVariable int tournamentNumber, @PathVariable int groupNumber){
        return TournamentsSingleton.get(tournamentNumber).getGroup(groupNumber).getGroupParticipants();
    }

    @PostMapping("/add")
    public void addTournamentParticipant(@RequestBody int participantNumber, @PathVariable int tournamentNumber, @PathVariable int groupNumber) throws DuplicateException {
        TournamentParticipant tpToBeAdded = TournamentsSingleton.get(tournamentNumber).getParticipant(participantNumber);
        TournamentsSingleton.get(tournamentNumber).getGroup(groupNumber).addParticipant(tpToBeAdded);
    }

    @DeleteMapping("/delete/{number}")
    public void deleteParticipant(@PathVariable int tournamentNumber, @PathVariable int groupNumber, @PathVariable int number){
        TournamentsSingleton.get(tournamentNumber).getGroup(groupNumber).deleteParticipant(number);
    }
}
