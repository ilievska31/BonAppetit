package com.bonappetit.bonappetit;

import java.util.ArrayList;

public class User {

    private String firstName;
    private String lastName;
    private String diet;
    private ArrayList<String> intolerances;


    public User(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public User(String diet, ArrayList<String> intolerances) {
        this.diet = diet;
        this.intolerances = intolerances;
    }

    public User(String firstName, String lastName, String diet, ArrayList<String> intolerances) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.diet = diet;
        this.intolerances = intolerances;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getDiet() {
        return diet;
    }

    public void setDiet(String diet) {
        this.diet = diet;
    }

    public ArrayList<String> getIntolerances() {
        return intolerances;
    }

    public void setIntolerances(ArrayList<String> intolerances) {
        this.intolerances = intolerances;
    }
}
