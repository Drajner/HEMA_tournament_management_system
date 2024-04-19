package drajner.hetman.controllers;

import drajner.hetman.TournamentsSingleton;
import drajner.hetman.errors.OneFinalsException;
import drajner.hetman.errors.UnfinishedFightException;
import drajner.hetman.errors.WrongAmountException;
import drajner.hetman.requests.FightReport;
import drajner.hetman.services.*;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping("tournaments/{tournamentNumber}/groups")
@Log4j2
public class GroupController {


    @GetMapping("/get")
    public ArrayList<TournamentParticipant> getGroups(@PathVariable int tournamentNumber){
        return TournamentsSingleton.get(tournamentNumber).getParticipants();
    }

    @PostMapping("add/ladder")
    public void addLadderGroup(@PathVariable int tournamentNumber) throws OneFinalsException {
        TournamentsSingleton.get(tournamentNumber).addGroupLadder();
    }

    @PostMapping("add/pool")
    public void addPoolGroup(@RequestBody Person person, @PathVariable int tournamentNumber){
        TournamentsSingleton.get(tournamentNumber).addGroupPool();
    }

    @DeleteMapping("delete/{number}")
    public void deleteGroup(@PathVariable int tournamentNumber, @PathVariable int number){
        TournamentsSingleton.get(tournamentNumber).getGroups().remove(number);
    }

    @PostMapping("autoGenerateFights")
    public void autoGenerateFights(@PathVariable int tournamentNumber, @PathVariable int groupNumber) throws WrongAmountException {
        TournamentsSingleton.get(tournamentNumber).getGroups().get(groupNumber).autoGenerateFights();
    }

    @PostMapping("evaluateGroup")
    public void evaluateGroup(@PathVariable int tournamentNumber, @PathVariable int groupNumber) throws UnfinishedFightException {
        TournamentsSingleton.get(tournamentNumber).getGroups().get(groupNumber).evaluateGroup(1); //rework modifier later
    }



}
