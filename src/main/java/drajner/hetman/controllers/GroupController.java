package drajner.hetman.controllers;

import drajner.hetman.TournamentsSingleton;
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
        return TournamentsSingleton.get(tournamentNumber).getGroups().get(groupNumber);
    }

    @GetMapping("/participants")
    public ArrayList<TournamentParticipant> getTournamentParticipants(@PathVariable int tournamentNumber, @PathVariable int groupNumber){
        return TournamentsSingleton.get(tournamentNumber).getGroups().get(groupNumber).getGroupParticipants();
    }

    @PostMapping("/participants/add")
    public void addTournamentParticipant(@RequestBody TournamentParticipant tournamentParticipant, @PathVariable int tournamentNumber, @PathVariable int groupNumber){
        TournamentsSingleton.get(tournamentNumber).getGroups().get(groupNumber).addParticipant(tournamentParticipant);
    }

    @DeleteMapping("participants/delete/{number}")
    public void deleteParticipant(@PathVariable int tournamentNumber, @PathVariable int groupNumber, @PathVariable int number){
        TournamentsSingleton.get(tournamentNumber).getGroups().get(groupNumber).deleteParticipant(number);
    }

    @DeleteMapping("participants/replace/{number}")
    public void replaceParticipant(@RequestBody TournamentParticipant tournamentParticipant, @PathVariable int tournamentNumber, @PathVariable int groupNumber, @PathVariable int number){
        TournamentsSingleton.get(tournamentNumber).getGroups().get(groupNumber).replaceParticipant(number, tournamentParticipant);
    }

    @GetMapping("/fights")
    public ArrayList<Fight> getFights(@PathVariable int tournamentNumber, @PathVariable int groupNumber){
        return TournamentsSingleton.get(tournamentNumber).getGroups().get(groupNumber).getFights();
    }

    @PostMapping("/fights/add")
    public void addFight(@RequestBody Fight fight, @PathVariable int tournamentNumber, @PathVariable int groupNumber){
        TournamentsSingleton.get(tournamentNumber).getGroups().get(groupNumber).addFight(fight);
    }

    @DeleteMapping("fights/delete/{number}")
    public void deleteFight(@PathVariable int tournamentNumber, @PathVariable int groupNumber, @PathVariable int number){
        TournamentsSingleton.get(tournamentNumber).getGroups().get(groupNumber).deleteFight(number);
    }

    @PostMapping("fight/replace/{number}")
    public void replaceFight(@RequestBody Fight fight, @PathVariable int tournamentNumber, @PathVariable int groupNumber, @PathVariable int number){
        TournamentsSingleton.get(tournamentNumber).getGroups().get(groupNumber).replaceFight(number, fight);
    }

    @PostMapping("fight/sendReport/{number}")
    public void sendReport(@RequestBody Fight fight, @PathVariable int tournamentNumber, @PathVariable int groupNumber, @PathVariable int number){
        FightReport newReport = new FightReport("BottomText", tournamentNumber, groupNumber, number, fight);
        TournamentsSingleton.addReport(newReport);
    }

    @PostMapping("autoGenerateFights")
    public void autoGenerateFights(@PathVariable int tournamentNumber, @PathVariable int groupNumber){
        TournamentsSingleton.get(tournamentNumber).getGroups().get(groupNumber).autoGenerateFights();
    }

    @PostMapping("evaluateGroup")
    public void evaluateGroup(@PathVariable int tournamentNumber, @PathVariable int groupNumber){
        TournamentsSingleton.get(tournamentNumber).getGroups().get(groupNumber).evaluateGroup(1); //rework modifier later
    }



}
