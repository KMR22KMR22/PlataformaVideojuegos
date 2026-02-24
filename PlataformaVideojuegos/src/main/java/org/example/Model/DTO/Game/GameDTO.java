package org.example.Model.DTO.Game;

import java.time.LocalDate;
import java.util.List;

public class GameDTO {

    private Long id;
    private String title;
    private String description;
    private String developer;
    private LocalDate launchDate;
    private float basePrice;
    private int currentDescount;
    private String category;
    private GameAgeClasification gameAgeClasification;
    private List<String> availabeLanguages;
    private GameState State;


    //Getters


    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getDeveloper() {
        return developer;
    }

    public LocalDate getLaunchDate() {
        return launchDate;
    }

    public float getBasePrice() {
        return basePrice;
    }

    public int getCurrentDescount() {
        return currentDescount;
    }

    public String getCategory() {
        return category;
    }

    public GameAgeClasification getAgeClasification() {
        return gameAgeClasification;
    }

    public List<String> getAvailabeLanguages() {
        return availabeLanguages;
    }

    public GameState getState() {
        return State;
    }


    //Setters


    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setAvailabeLanguages(List<String> availabeLanguages) {
        this.availabeLanguages = availabeLanguages;
    }

    public void setState(GameState state) {
        State = state;
    }


    //Constructor


    public GameDTO(Long id, String title, String description, String developer, LocalDate launchDate, float basePrice, int currentDescount, String category, GameAgeClasification gameAgeClasification, List<String> availabeLanguages, GameState state) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.developer = developer;
        this.launchDate = launchDate;
        this.basePrice = basePrice;
        this.currentDescount = currentDescount;
        this.category = category;
        this.gameAgeClasification = gameAgeClasification;
        this.availabeLanguages = availabeLanguages;
        State = state;
    }
}
