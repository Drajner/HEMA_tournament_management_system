package drajner.hetman.services;

public class Person {

    private String name;
    private String surname;
    private String teamName;


    public Person(){}
    public Person(String name, String surname){
        this.name = name;
        this.surname = surname;
        this.teamName = "None";
    }

    public Person(String name, String surname, String teamName){
        this.name = name;
        this.surname = surname;
        this.teamName = teamName;
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

    public void setName(String name) {
        this.name = name;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

}
