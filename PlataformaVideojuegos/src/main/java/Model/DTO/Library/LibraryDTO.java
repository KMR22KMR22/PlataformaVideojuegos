package Model.DTO.Library;

import Model.DTO.User.UserDTO;

import java.time.Duration;
import java.time.LocalDate;
import java.util.Date;

public class LibraryDTO {

    private int id;
    private int idUser;
    private int idGame;
    private LocalDate adquisitionDate;
    private Duration timePlaying;
    private Date lastPlayed;
    private InstalationState instalationState;


    //Getters

    public int getId() {
        return id;
    }

    public int getIdUser() {
        return idUser;
    }

    public int getIdGame() {
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

    public Enum getInstalationState() {
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


    public LibraryDTO(int id, int idUser, int idGame, LocalDate adquisitionDate, Duration timePlaying, Date lastPlayed, InstalationState instalationState) {
        this.id = id;
        this.idUser = idUser;
        this.idGame = idGame;
        this.adquisitionDate = adquisitionDate;
        this.timePlaying = timePlaying;
        this.lastPlayed = lastPlayed;
        this.instalationState = instalationState;
    }
}
