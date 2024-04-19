package drajner.hetman.controllers;

import drajner.hetman.TournamentsSingleton;
import drajner.hetman.services.CompetitorStatus;
import drajner.hetman.services.FightStatus;
import drajner.hetman.services.Person;
import drajner.hetman.services.TournamentParticipant;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping("tournaments/{tournamentNumber}/participants")
@Log4j2
public class ParticipantController {

    @GetMapping("/get")
    public ArrayList<TournamentParticipant> getTournamentParticipants(@PathVariable int tournamentNumber){
        return TournamentsSingleton.get(tournamentNumber).getParticipants();
    }

    @PostMapping("/add")
    public void addTournamentParticipant(@RequestBody Person person, @PathVariable int tournamentNumber){
        TournamentsSingleton.get(tournamentNumber).addParticipant(person);
    }

    @DeleteMapping("/delete/{number}")
    public void deleteParticipant(@PathVariable int tournamentNumber, @PathVariable int number){
        TournamentsSingleton.get(tournamentNumber).removeParticipant(number);
    }

    @PostMapping("/replace/{number}")
    public void addTournamentParticipant(@RequestBody TournamentParticipant participant, @PathVariable int tournamentNumber, @PathVariable int number){
        TournamentsSingleton.get(tournamentNumber).replaceParticipant(number, participant);
    }

    @PostMapping("/disqualify/{number}")
    public void disqualifyParticipant(@PathVariable int tournamentNumber, @PathVariable int number){
        TournamentsSingleton.get(tournamentNumber).getParticipant(number).disqualify();
    }

    @PostMapping("/compete/{number}")
    public void competeParticipant(@PathVariable int tournamentNumber, @PathVariable int number){
        TournamentsSingleton.get(tournamentNumber).getParticipant(number).setStatus(CompetitorStatus.COMPETING);
    }

    @PostMapping("/eliminate/{number}")
    public void eliminateParticipant(@PathVariable int tournamentNumber, @PathVariable int number){
        TournamentsSingleton.get(tournamentNumber).getParticipant(number).setStatus(CompetitorStatus.ELIMINATED);
    }

}
