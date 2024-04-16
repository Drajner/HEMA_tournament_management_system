package drajner.hetman.controllers;

import drajner.hetman.TournamentsSingleton;
import drajner.hetman.errors.UnfinishedFightException;
import drajner.hetman.errors.WrongAmountException;
import drajner.hetman.requests.FightReport;
import drajner.hetman.services.*;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping("tournaments/{tournamentNumber}/groups/{groupNumber}")
@Log4j2
public class GroupController {

    @GetMapping("/get")
    public Group getGroup(@PathVariable int tournamentNumber, @PathVariable int groupNumber){
        return TournamentsSingleton.get(tournamentNumber).getGroup(groupNumber);
    }

    @GetMapping("/participants")
    public ArrayList<TournamentParticipant> getTournamentParticipants(@PathVariable int tournamentNumber, @PathVariable int groupNumber){
        return TournamentsSingleton.get(tournamentNumber).getGroup(groupNumber).getGroupParticipants();
    }

    @PostMapping("/participants/add")
    public void addTournamentParticipant(@RequestBody TournamentParticipant tournamentParticipant, @PathVariable int tournamentNumber, @PathVariable int groupNumber){
        TournamentsSingleton.get(tournamentNumber).getGroup(groupNumber).addParticipant(tournamentParticipant);
    }

    @DeleteMapping("participants/delete/{number}")
    public void deleteParticipant(@PathVariable int tournamentNumber, @PathVariable int groupNumber, @PathVariable int number){
        TournamentsSingleton.get(tournamentNumber).getGroup(groupNumber).deleteParticipant(number);
    }

    @DeleteMapping("participants/replace/{number}")
    public void replaceParticipant(@RequestBody TournamentParticipant tournamentParticipant, @PathVariable int tournamentNumber, @PathVariable int groupNumber, @PathVariable int number){
        TournamentsSingleton.get(tournamentNumber).getGroup(groupNumber).replaceParticipant(number, tournamentParticipant);
    }

    @GetMapping("/fights")
    public ArrayList<Fight> getFights(@PathVariable int tournamentNumber, @PathVariable int groupNumber){
        return TournamentsSingleton.get(tournamentNumber).getGroup(groupNumber).getFights();
    }

    @PostMapping("/fights/add")
    public void addFight(@RequestBody Fight fight, @PathVariable int tournamentNumber, @PathVariable int groupNumber){
        TournamentsSingleton.get(tournamentNumber).getGroup(groupNumber).addFight(fight);
    }

    @DeleteMapping("fights/delete/{number}")
    public void deleteFight(@PathVariable int tournamentNumber, @PathVariable int groupNumber, @PathVariable int number){
        TournamentsSingleton.get(tournamentNumber).getGroup(groupNumber).deleteFight(number);
    }

    @PostMapping("fight/replace/{number}")
    public void replaceFight(@RequestBody Fight fight, @PathVariable int tournamentNumber, @PathVariable int groupNumber, @PathVariable int number){
        TournamentsSingleton.get(tournamentNumber).getGroup(groupNumber).replaceFight(number, fight);
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
