package drajner.hetman.services;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
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

}
