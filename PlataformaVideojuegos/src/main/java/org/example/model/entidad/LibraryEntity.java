package org.example.model.entidad;

import org.example.model.dto.library.InstalationState;

import java.time.LocalDate;
import java.util.Date;

public class LibraryEntity {

    private Long id;
    private Long idUser;
    private Long idGame;
    private LocalDate acquisitionDate;
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

    public LocalDate getAcquisitionDate() {
        return acquisitionDate;
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


    //Setters


    public void setId(Long id) {
        this.id = id;
    }

    public void setIdUser(Long idUser) {
        this.idUser = idUser;
    }

    public void setIdGame(Long idGame) {
        this.idGame = idGame;
    }

    public void setAcquisitionDate(LocalDate acquisitionDate) {
        this.acquisitionDate = acquisitionDate;
    }

    public void setTimePlaying(Long timePlaying) {
        this.timePlaying = timePlaying;
    }

    public void setLastPlayed(Date lastPlayed) {
        this.lastPlayed = lastPlayed;
    }

    public void setInstalationState(InstalationState instalationState) {
        this.instalationState = instalationState;
    }

    //Constructor Creacion
    public LibraryEntity(Long id, Long idUser, Long idGame, LocalDate acquisitionDate) {
        this.id = id;
        this.idUser = idUser;
        this.idGame = idGame;
        this.acquisitionDate = acquisitionDate;
        this.timePlaying = 0L;
        this.lastPlayed = null;
        this.instalationState = InstalationState.NO_INSTALADO;
    }


    //Constructor Actualizacion
    public LibraryEntity(Long id, Long idUser, Long idGame, LocalDate acquisitionDate, Long timePlaying, Date lastPlayed, InstalationState instalationState) {
        this.id = id;
        this.idUser = idUser;
        this.idGame = idGame;
        this.acquisitionDate = acquisitionDate;
        this.timePlaying = timePlaying;
        this.lastPlayed = lastPlayed;
        this.instalationState = instalationState;
    }

    @Override
    public String toString() {
        return "LibraryEntity{" +
                "id=" + id +
                ", idUser=" + idUser +
                ", idGame=" + idGame +
                ", acquisitionDate=" + acquisitionDate +
                ", timePlaying=" + timePlaying +
                ", lastPlayed=" + lastPlayed +
                ", instalationState=" + instalationState +
                '}';
    }
}
