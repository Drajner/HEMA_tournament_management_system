package drajner.hetman.services;

import drajner.hetman.requests.FightReport;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
public class ReportsList {
    private List<FightReport> reportsList = Collections.synchronizedList(new ArrayList<>());

    public void add(FightReport fightReport){
        synchronized (reportsList){
            reportsList.add(fightReport);
        }
    }

    public FightReport get(int index){
        synchronized (reportsList){
            return reportsList.get(index);
        }
    }

    public List<FightReport> getAll(){
        synchronized (reportsList){
            return new ArrayList<>(reportsList);
        }
    }

    public void remove(int index){
        synchronized (reportsList){
            reportsList.remove(index);
        }
    }
}
