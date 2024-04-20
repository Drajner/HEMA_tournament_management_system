package drajner.hetman.controllers;

import drajner.hetman.TournamentsSingleton;
import drajner.hetman.errors.NotPendingException;
import drajner.hetman.errors.ReportMismatchException;
import drajner.hetman.requests.FightReport;
import drajner.hetman.services.Fight;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping("report")
@Log4j2
public class ReportController {

    @PostMapping("/send")
    public void sendReport(@RequestBody FightReport fightReport){
        TournamentsSingleton.addReport(fightReport);
    }

    @PostMapping("accept/{number}")
    public void acceptReport(@PathVariable int number) throws ReportMismatchException, NotPendingException {
        TournamentsSingleton.acceptReport(number);
    }

    @DeleteMapping("delete/{number}")
    public void deleteReport(int number){
        TournamentsSingleton.removeReport(number);
    }

    @GetMapping("/get")
    public ArrayList<FightReport> getReports(){
        return TournamentsSingleton.getReports();
    }
}
