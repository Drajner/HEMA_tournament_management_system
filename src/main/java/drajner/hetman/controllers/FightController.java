package drajner.hetman.controllers;

import drajner.hetman.TournamentsSingleton;
import drajner.hetman.requests.FightReport;
import drajner.hetman.services.Fight;
import drajner.hetman.services.TournamentParticipant;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping("tournaments/{tournamentNumber}/groups/{groupNumber}/fights")
@Log4j2
public class FightController {

    @GetMapping("/get")
    public ArrayList<Fight> getFights(@PathVariable int tournamentNumber, @PathVariable int groupNumber){
        return TournamentsSingleton.get(tournamentNumber).getGroup(groupNumber).getFights();
    }

    @PostMapping("/add")
    public void addFight(@RequestBody FightReport fightReport, @PathVariable int tournamentNumber, @PathVariable int groupNumber){
        TournamentParticipant firstParticipant = TournamentsSingleton.get(tournamentNumber).getGroup(groupNumber).getGroupParticipant(fightReport.getFirstParticipantNumber());
        TournamentParticipant secondParticipant = TournamentsSingleton.get(tournamentNumber).getGroup(groupNumber).getGroupParticipant(fightReport.getSecondParticipantNumber());
        Fight newFight = new Fight(firstParticipant, secondParticipant);
        TournamentsSingleton.get(tournamentNumber).getGroup(groupNumber).addFight(newFight);
    }

    @DeleteMapping("delete/{number}")
    public void deleteFight(@PathVariable int tournamentNumber, @PathVariable int groupNumber, @PathVariable int number){
        TournamentsSingleton.get(tournamentNumber).getGroup(groupNumber).deleteFight(number);
    }

    @PostMapping("replace/{number}")
    public void replaceFight(@RequestBody FightReport fightReport, @PathVariable int tournamentNumber, @PathVariable int groupNumber, @PathVariable int number){
        TournamentParticipant firstParticipant = TournamentsSingleton.get(tournamentNumber).getGroup(groupNumber).getGroupParticipant(fightReport.getFirstParticipantNumber());
        TournamentParticipant secondParticipant = TournamentsSingleton.get(tournamentNumber).getGroup(groupNumber).getGroupParticipant(fightReport.getSecondParticipantNumber());
        TournamentParticipant winner = TournamentsSingleton.get(tournamentNumber).getGroup(groupNumber).getGroupParticipant(fightReport.getWinnerNumber());

        Fight newFight = new Fight(firstParticipant,
                                   secondParticipant,
                                   fightReport.getFirstParticipantPoints(),
                                   fightReport.getSecondParticipantPoints(),
                                   fightReport.getDoubles(),
                                   fightReport.getStatus(),
                                   winner);
        TournamentsSingleton.get(tournamentNumber).getGroup(groupNumber).replaceFight(number, newFight);
    }


}
