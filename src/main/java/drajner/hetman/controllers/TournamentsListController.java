package drajner.hetman.controllers;

import drajner.hetman.TournamentsSingleton;
import drajner.hetman.services.Tournament;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
public class TournamentsListController {
    @RequestMapping("/tournaments")
    public ArrayList<Tournament> getTournaments() throws Exception{
        return TournamentsSingleton.getInstance().tournaments;
    }

    @PostMapping("tournaments/add")
    public void addTournament(@RequestBody String name){
        Tournament newTournament = new Tournament(name);
        TournamentsSingleton.add(newTournament);
    }

    @DeleteMapping("tournaments/delete/{number}")
    public void deleteTournament(@PathVariable int number)
    {
        TournamentsSingleton.remove(number);
    }
}
