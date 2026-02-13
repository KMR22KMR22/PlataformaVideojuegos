package org.example.Model.Entidad;

import org.example.Model.DTO.Game.AgeClasification;
import org.example.Model.DTO.Game.GameCategory;
import org.example.Model.DTO.Game.GameState;

import java.time.LocalDate;
import java.util.List;

public class GameEntity {

    private Long id;
    private String tittle;
    private String description;
    private String developer;
    private LocalDate launchDate;
    private float basePrice;
    private int currentDescount;
    private GameCategory category;
    private AgeClasification ageClasification;
    private List<String> availabeLanguages;
    private GameState State;


    //Getters


    public Long getId() {
        return id;
    }

    public String getTittle() {
        return tittle;
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

    public Enum getCategory() {
        return category;
    }

    public Enum getAgeClasification() {
        return ageClasification;
    }

    public String[] getAvailabeLanguages() {
        return availabeLanguages;
    }

    public Enum getState() {
        return State;
    }


    //Setters


    public void setTittle(String tittle) {
        this.tittle = tittle;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setAvailabeLanguages(String[] availabeLanguages) {
        this.availabeLanguages = availabeLanguages;
    }

    public void setState(GameState state) {
        State = state;
    }


    //Constructor


    public GameEntity(Long id, String tittle, String description, String developer, LocalDate launchDate, float basePrice, int currentDescount, GameCategory category, AgeClasification ageClasification, List<String> availabeLanguages, GameState state) {
        this.id = id;
        this.tittle = tittle;
        this.description = description;
        this.developer = developer;
        this.launchDate = launchDate;
        this.basePrice = basePrice;
        this.currentDescount = currentDescount;
        this.category = category;
        this.ageClasification = ageClasification;
        this.availabeLanguages = availabeLanguages;
        State = state;
    }
}
