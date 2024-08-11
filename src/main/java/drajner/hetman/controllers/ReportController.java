package drajner.hetman.controllers;

import drajner.hetman.services.ReportsService;
import drajner.hetman.services.ReportsSingleton;
import drajner.hetman.errors.NotPendingException;
import drajner.hetman.errors.ReportMismatchException;
import drajner.hetman.requests.FightReport;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("reports")
@Log4j2
public class ReportController {

    @Autowired
    ReportsService reportsService;

    @PostMapping("/send")
    public void sendReport(@RequestBody FightReport fightReport){
        reportsService.addReport(fightReport);
    }

    @PostMapping("accept/{number}")
    public void acceptReport(@PathVariable Integer number) throws ReportMismatchException, NotPendingException {
        reportsService.acceptReport(number);
    }

    @DeleteMapping("delete/{number}")
    public void deleteReport(@PathVariable Integer number){
        reportsService.removeReport(number);
    }

    @GetMapping("/get")
    public List<FightReport> getReports(){
        return reportsService.getReports();
    }
}
