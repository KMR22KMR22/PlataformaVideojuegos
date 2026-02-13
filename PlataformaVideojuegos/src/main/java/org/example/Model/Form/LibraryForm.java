package org.example.Model.Form;

import org.example.Model.DTO.Library.InstalationState;

import java.text.Normalizer;
import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class LibraryForm {

    private Long id;
    private Long idUser;
    private Long idGame;
    private LocalDate adquisitionDate;
    private Duration timePlaying;
    private Date lastPlayed;
    private InstalationState instalationState;

    private List<String> errores = new ArrayList<>();


    //Getters

    public Long getId() {
        return id;
    }

    public Long getIdUser() {
        return idUser;
    }

    public Long getIdGame() {
        return idGame;
    }

    public LocalDate getAdquisitionDate() {
        return adquisitionDate;
    }

    public Duration getTimePlaying() {
        return timePlaying;
    }

    public Date getLastPlayed() {
        return lastPlayed;
    }

    public InstalationState getInstalationState() {
        return instalationState;
    }


    //Setters


    public void setInstalationState(InstalationState instalationState) {
        this.instalationState = instalationState;
    }

    public void setLastPlayed(Date lastPlayed) {
        this.lastPlayed = lastPlayed;
    }

    public void setTimePlaying(Duration timePlaying) {
        this.timePlaying = timePlaying;
    }


    //Constructor


    public LibraryForm(Long id, Long idUser, Long idGame, LocalDate adquisitionDate, Duration timePlaying, Date lastPlayed, InstalationState instalationState) {
        this.id = id;
        this.idUser = idUser;
        this.idGame = idGame;
        this.adquisitionDate = adquisitionDate;
        this.timePlaying = timePlaying;
        this.lastPlayed = lastPlayed;
        this.instalationState = instalationState;
    }



    //Validaciones

    public List<String> validateLibraryForm(LibraryForm library) {
        errores.clear();

        //Usuario
        if(library.getIdGame() == null || library.getIdGame() <= 0){
            errores.add("Debe tener un juego")
        }

        //
    }

}
