package org.example.Model.DTO.Library;

import org.example.Model.DTO.Game.GameDTO;
import org.example.Model.DTO.User.UserDTO;

import java.time.Duration;
import java.time.LocalDate;
import java.util.Date;

public class LibraryDTO {

    private Long id;
    private Long idUser;
    private UserDTO User;
    private Long idGame;
    private GameDTO Game;
    private LocalDate adquisitionDate;
    private Duration timePlaying;
    private Date lastPlayed;
    private InstalationState instalationState;


    //Getters

    public Long getId() {
        return id;
    }

    public UserDTO getUser() {
        return User;
    }

    public GameDTO getGame() {
        return Game;
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

    public Long getIdUser() {return idUser;}

    public Long getIdGame() {return idGame;}



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


    public LibraryDTO(Long id, Long idUser, UserDTO user, Long idGame, GameDTO game, LocalDate adquisitionDate, Duration timePlaying, Date lastPlayed, InstalationState instalationState) {
        this.id = id;
        this.idUser = idUser;
        User = user;
        this.idGame = idGame;
        Game = game;
        this.adquisitionDate = adquisitionDate;
        this.timePlaying = timePlaying;
        this.lastPlayed = lastPlayed;
        this.instalationState = instalationState;
    }
}
