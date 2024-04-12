package drajner.hetman.requests;


public class TournamentAddRequest {
    String name;

    TournamentAddRequest() {}
    TournamentAddRequest(String name) {
        this.name = name;
    }

    public String getName(){
        return name;
    }

}
