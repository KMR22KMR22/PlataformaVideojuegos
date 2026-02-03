package Model.Game;

import Model.Model;

import java.time.LocalDate;

public class Game extends Model {

    private int id;
    private String tittle;
    private String description;
    private String desarrollador;
    private LocalDate launchDate;
    private float basePrice;
    private float currentDescount;
    private GameCategory category;
    private AgeClasification ageClasification;
    private String[] availabeLanguages;
    private Enum State;


    //Getters


    public int getId() {
        return id;
    }

    public String getTittle() {
        return tittle;
    }

    public String getDescription() {
        return description;
    }

    public String getDesarrollador() {
        return desarrollador;
    }

    public LocalDate getLaunchDate() {
        return launchDate;
    }

    public float getBasePrice() {
        return basePrice;
    }

    public float getCurrentDescount() {
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

    public void setState(Enum state) {
        State = state;
    }


    //Constructor


    public Game(int id, String tittle, String description, String desarrollador, LocalDate launchDate, float basePrice, float currentDescount, GameCategory category, AgeClasification ageClasification, String[] availabeLanguages, Enum state) {
        this.id = id;
        this.tittle = tittle;
        this.description = description;
        this.desarrollador = desarrollador;
        this.launchDate = launchDate;
        this.basePrice = basePrice;
        this.currentDescount = currentDescount;
        this.category = category;
        this.ageClasification = ageClasification;
        this.availabeLanguages = availabeLanguages;
        State = state;
    }
}
