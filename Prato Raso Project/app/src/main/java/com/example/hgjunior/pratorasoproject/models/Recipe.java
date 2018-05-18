package com.example.hgjunior.pratorasoproject.models;

import java.util.List;

public class Recipe {

    private String id;
    private String name;
    private String image;
    private String like;
    private int preparation;
    private int portion;
    private int ingredients_qtd;
    private String difficulty;
    private List<String> ingredientsList;
    private List<String> preparationList;

    public Recipe() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getLike() {
        return like;
    }

    public void setLike(String like) {
        this.like = like;
    }

    public int getPreparation() {
        return preparation;
    }

    public void setPreparation(int preparation) {
        this.preparation = preparation;
    }

    public int getPortion() {
        return portion;
    }

    public void setPortion(int portion) {
        this.portion = portion;
    }

    public int getIngredients_qtd() {
        return ingredients_qtd;
    }

    public void setIngredients_qtd(int ingredients_qtd) {
        this.ingredients_qtd = ingredients_qtd;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public List<String> getIngredientsList() {
        return ingredientsList;
    }

    public void setIngredientsList(List<String> ingredientsList) {
        this.ingredientsList = ingredientsList;
    }

    public List<String> getPreparationList() {
        return preparationList;
    }

    public void setPreparationList(List<String> preparationList) {
        this.preparationList = preparationList;
    }
}
