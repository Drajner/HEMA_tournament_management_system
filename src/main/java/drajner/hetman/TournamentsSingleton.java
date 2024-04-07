package drajner.hetman;

import drajner.hetman.services.Tournament;

import java.util.ArrayList;

public class TournamentsSingleton {

    private static volatile TournamentsSingleton instance;

    public ArrayList<Tournament> tournaments;

    private TournamentsSingleton(){
        this.tournaments = new ArrayList<>();
    }

    public static TournamentsSingleton getInstance(){

        TournamentsSingleton result = instance;
        if (result != null){
            return result;
        }
        synchronized (TournamentsSingleton.class){
            if (instance == null){
                instance = new TournamentsSingleton();
            }
            return instance;
        }
    }
}
