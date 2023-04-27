package drajner.hetman.services;

public class Competitor {

    private String name;
    private String surname;
    private String teamName;
    CompetitorStatus status;
    int score;

    public Competitor(String name, String surname){
        this.name = name;
        this.surname = surname;
        this.teamName = "None";
        this.status = CompetitorStatus.COMPETING;
        this.score = 0;
    }

    public Competitor(String name, String surname, String teamName, CompetitorStatus status, int score){
        this.name = name;
        this.surname = surname;
        this.teamName = teamName;
        this.status = status;
        this.score = score;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getTeamName() {
        return teamName;
    }

    public CompetitorStatus getStatus() {
        return status;
    }

    public int getScore() {
        return score;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public void setStatus(CompetitorStatus status) {
        this.status = status;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
