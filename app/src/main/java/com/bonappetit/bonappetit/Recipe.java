package com.bonappetit.bonappetit;

import java.util.ArrayList;

public class Recipe {

    private String image;
    private String title;
    private String calories;
    private String id;
    private String hide;
    private ArrayList<String> ingredients;
    private ArrayList<String> instructions;

    public Recipe(String image, String title, String calories, String id, String hide) {
        this.image = image;
        this.title = title;
        this.calories = calories;
        this.id = id;
        this.hide = hide;
    }

    public Recipe(String image, String title, String calories, String id, String hide, ArrayList<String> ingredients, ArrayList<String> instructions) {
        this.image = image;
        this.title = title;
        this.calories = calories;
        this.id = id;
        this.hide = hide;
        this.ingredients = ingredients;
        this.instructions = instructions;
    }

    public Recipe() {
        ingredients = new ArrayList<>();
        instructions = new ArrayList<>();


    }

    public String getImage() {
        return image;
    }

    public String getTitle() {
        return title;
    }

    public String getCalories() {
        return calories;
    }

    public String getId() {
        return id;
    }

    public String getHide() {
        return hide;
    }

    public void setHide(String hide) {
        this.hide = hide;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setCalories(String calories) {
        this.calories = calories;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ArrayList<String> getIngredients() {
        return ingredients;
    }

    public void setIngredients(ArrayList<String> ingredients) {
        this.ingredients = ingredients;
    }

    public ArrayList<String> getInstructions() {
        return instructions;
    }

    public void setInstructions(ArrayList<String> instructions) {
        this.instructions = instructions;
    }




}
