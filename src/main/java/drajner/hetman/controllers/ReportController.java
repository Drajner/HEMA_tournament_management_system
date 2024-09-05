package drajner.hetman.controllers;

import drajner.hetman.errors.OneFinalsException;
import drajner.hetman.errors.WrongAmountException;
import drajner.hetman.requests.ErrorResponse;
import drajner.hetman.services.ReportsService;
import drajner.hetman.errors.NotPendingException;
import drajner.hetman.errors.ReportMismatchException;
import drajner.hetman.requests.FightReport;
import drajner.hetman.utils.TokenUtils;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = {"http://localhost:5173"}, allowCredentials = "true")
@RequestMapping("reports")
@Log4j2
public class ReportController {

    @Autowired
    ReportsService reportsService;

    @PostMapping("/send")
    public ResponseEntity<Object> sendReport(@RequestHeader(value = "Authorization", required = false) String authHeader, @RequestBody FightReport fightReport){
        try {
            if(authHeader != null && authHeader.startsWith("Bearer ")){
                String username;
                authHeader = authHeader.substring(7);
                username = TokenUtils.getUsername(authHeader);
                fightReport.setUsername(username);
                reportsService.addReport(fightReport);
                return ResponseEntity.ok().build();
            }
            else{
                fightReport.setUsername("UNKNOWN");
                reportsService.addReport(fightReport);
                return ResponseEntity.ok().build();
            }
        }catch(Exception e){
            log.error(e);
            ErrorResponse response = new ErrorResponse(e.getClass().getSimpleName(), e.getMessage());
            return ResponseEntity.internalServerError().body(response);        }
    }

    @PostMapping("accept/{number}")
    public ResponseEntity<Object> acceptReport(@PathVariable Integer number){
        try {
            reportsService.acceptReport(number);
            return ResponseEntity.ok().build();
        }catch(ReportMismatchException | NotPendingException e){
            log.error(e);
            ErrorResponse response = new ErrorResponse(e.getClass().getSimpleName(), e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }catch(Exception e){
            log.error(e);
            ErrorResponse response = new ErrorResponse(e.getClass().getSimpleName(), e.getMessage());
            return ResponseEntity.internalServerError().body(response);        }
    }

    @DeleteMapping("delete/{number}")
    public ResponseEntity<Object> deleteReport(@PathVariable Integer number){
        try {
            reportsService.removeReport(number);
            return ResponseEntity.ok().build();
        }catch(Exception e){
            log.error(e);
            ErrorResponse response = new ErrorResponse(e.getClass().getSimpleName(), e.getMessage());
            return ResponseEntity.internalServerError().body(response);        }
    }

    @GetMapping("/get")
    public ResponseEntity<Object> getReports(){
        try {
            return ResponseEntity.ok(reportsService.getReports());
        }catch(Exception e){
            log.error(e);
            ErrorResponse response = new ErrorResponse(e.getClass().getSimpleName(), e.getMessage());
            return ResponseEntity.internalServerError().body(response);        }
    }

    @GetMapping("/get/{reportNumber}")
    public ResponseEntity<Object> getReports(@PathVariable int reportNumber){
        try {
            return ResponseEntity.ok(reportsService.getReport(reportNumber));
        }catch(Exception e){
            log.error(e);
            ErrorResponse response = new ErrorResponse(e.getClass().getSimpleName(), e.getMessage());
            return ResponseEntity.internalServerError().body(response);        }
    }
}
