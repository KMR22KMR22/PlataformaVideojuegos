package org.example.Model.Entidad;

import org.example.Model.DTO.Game.GameAgeClasification;
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
    private String category;
    private GameAgeClasification gameAgeClasification;
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


    //Constructor


    public GameEntity(Long id, String tittle, String description, String developer, LocalDate launchDate, float basePrice, String category, GameAgeClasification gameAgeClasification, List<String> availabeLanguages) {
        this.id = id;
        this.tittle = tittle;
        this.description = description;
        this.developer = developer;
        this.launchDate = launchDate;
        this.basePrice = basePrice;
        this.currentDescount = 0;
        this.category = category;
        this.gameAgeClasification = gameAgeClasification;
        this.availabeLanguages = availabeLanguages;
        State = GameState.DISPONIBLE;
    }
}
