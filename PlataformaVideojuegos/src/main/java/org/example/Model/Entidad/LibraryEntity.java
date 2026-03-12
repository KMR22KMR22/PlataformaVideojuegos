package org.example.Model.Entidad;

import org.example.Model.DTO.Library.InstalationState;

import java.time.Duration;
import java.time.LocalDate;
import java.util.Date;

public class LibraryEntity {

    private Long id;
    private Long idUser;
    private Long idGame;
    private LocalDate adquisitionDate;
    private Long timePlaying;
    private Date lastPlayed;
    private InstalationState instalationState;


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

    public Long getTimePlaying() {
        return timePlaying;
    }

    public Date getLastPlayed() {
        return lastPlayed;
    }

    public InstalationState getInstalationState() {
        return instalationState;
    }




    //Constructor Creacion
    public LibraryEntity(Long id, Long idUser, Long idGame, LocalDate adquisitionDate) {
        this.id = id;
        this.idUser = idUser;
        this.idGame = idGame;
        this.adquisitionDate = adquisitionDate;
        this.timePlaying = 0L;
        this.lastPlayed = null;
        this.instalationState = InstalationState.NO_INSTALADO;
    }


    //Constructor Actualizacion
    public LibraryEntity(Long id, Long idUser, Long idGame, LocalDate adquisitionDate, Long timePlaying, Date lastPlayed, InstalationState instalationState) {
        this.id = id;
        this.idUser = idUser;
        this.idGame = idGame;
        this.adquisitionDate = adquisitionDate;
        this.timePlaying = timePlaying;
        this.lastPlayed = lastPlayed;
        this.instalationState = instalationState;
    }
}
