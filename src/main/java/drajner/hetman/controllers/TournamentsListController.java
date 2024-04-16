package drajner.hetman.controllers;

import drajner.hetman.TournamentsSingleton;
import drajner.hetman.services.Tournament;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping("/tournaments")
public class TournamentsListController {
    @GetMapping("/get")
    public ArrayList<Tournament> getTournaments() throws Exception{
        return TournamentsSingleton.getInstance().tournaments;
    }

    @PostMapping("/add")
    public void addTournament(@RequestBody String name){
        Tournament newTournament = new Tournament(name);
        TournamentsSingleton.add(newTournament);
    }

    @DeleteMapping("/delete/{number}")
    public void deleteTournament(@PathVariable int number)
    {
        TournamentsSingleton.remove(number);
    }

    /*
    @GetMapping("/download")
    public ResponseEntity<byte[]> downloadTournaments(){
        String tournamentsJson = jsonExporter
    }
    */

}
