package drajner.hetman.controllers;

import drajner.hetman.ReportsSingleton;
import drajner.hetman.errors.NotPendingException;
import drajner.hetman.errors.ReportMismatchException;
import drajner.hetman.requests.FightReport;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping("report")
@Log4j2
public class ReportController {

    @PostMapping("/send")
    public void sendReport(@RequestBody FightReport fightReport){
        ReportsSingleton.addReport(fightReport);
    }

    @PostMapping("accept/{number}")
    public void acceptReport(@PathVariable int number) throws ReportMismatchException, NotPendingException {
        ReportsSingleton.acceptReport(number);
    }

    @DeleteMapping("delete/{number}")
    public void deleteReport(int number){
        ReportsSingleton.removeReport(number);
    }

    @GetMapping("/get")
    public ArrayList<FightReport> getReports(){
        return ReportsSingleton.getReports();
    }
}
