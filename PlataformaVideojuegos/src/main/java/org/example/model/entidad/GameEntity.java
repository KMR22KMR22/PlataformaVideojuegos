package org.example.model.entidad;

import org.example.model.dto.game.GameAgeClasification;
import org.example.model.dto.game.GameState;

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

    //Setters


    public void setId(Long id) {
        this.id = id;
    }

    public void setTittle(String tittle) {
        this.tittle = tittle;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDeveloper(String developer) {
        this.developer = developer;
    }

    public void setLaunchDate(LocalDate launchDate) {
        this.launchDate = launchDate;
    }

    public void setBasePrice(float basePrice) {
        this.basePrice = basePrice;
    }

    public void setCurrentDescount(int currentDescount) {
        this.currentDescount = currentDescount;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setGameAgeClasification(GameAgeClasification gameAgeClasification) {
        this.gameAgeClasification = gameAgeClasification;
    }

    public void setAvailabeLanguages(List<String> availabeLanguages) {
        this.availabeLanguages = availabeLanguages;
    }

    public void setState(GameState state) {
        State = state;
    }

    //Constructor Creacion
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
        this.State = GameState.DISPONIBLE;
    }

    //Constructor Actualizcion

    /**
     *
     * @param id
     * @param tittle
     * @param description
     * @param developer
     * @param launchDate
     * @param basePrice
     * @param currentDescount
     * @param category
     * @param gameAgeClasification
     * @param availabeLanguages
     * @param state
     */
    public GameEntity(Long id, String tittle, String description, String developer, LocalDate launchDate, float basePrice, int currentDescount, String category, GameAgeClasification gameAgeClasification, List<String> availabeLanguages, GameState state) {
        this.id = id;
        this.tittle = tittle;
        this.description = description;
        this.developer = developer;
        this.launchDate = launchDate;
        this.basePrice = basePrice;
        this.currentDescount = currentDescount;
        this.category = category;
        this.gameAgeClasification = gameAgeClasification;
        this.availabeLanguages = availabeLanguages;
        this.State = state;
    }

    @Override
    public String toString() {
        return "GameEntity{" + "id=" + id + ", tittle='" + tittle + '\'' + ", description='" + description + '\'' +
                ", developer='" + developer + '\'' + ", launchDate=" + launchDate + ", basePrice=" + basePrice +
                ", currentDescount=" + currentDescount + ", category='" + category + '\'' + ", gameAgeClasification="
                + gameAgeClasification + ", availabeLanguages=" + availabeLanguages + ", State=" + State + '}';
    }
}
