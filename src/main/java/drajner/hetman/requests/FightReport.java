package drajner.hetman.requests;

import drajner.hetman.entities.FightEntity;
import lombok.Getter;

@Getter
public class FightReport {

    private String username;
    private FightEntity fight;

    public FightReport(String username, FightEntity fight){
        this.username = username;
        this.fight = fight;
    }

}
