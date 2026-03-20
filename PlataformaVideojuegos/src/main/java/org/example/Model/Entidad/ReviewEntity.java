package org.example.Model.Entidad;

import org.example.Model.DTO.Review.ReviewState;

import java.time.LocalDate;

public class ReviewEntity {

    private Long id;
    private Long idUser;
    private Long idGame;
    private boolean recommended;
    private String reviwText;
    private Long hoursPlayed;
    private LocalDate publicationDate;
    private LocalDate lastEditionDate;
    private ReviewState state;


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

    public boolean isRecommended() {
        return recommended;
    }

    public String getReviwText() {
        return reviwText;
    }

    public Long getHoursPlayed() {
        return hoursPlayed;
    }

    public LocalDate getPublicationDate() {
        return publicationDate;
    }

    public LocalDate getLastEditionDate() {
        return lastEditionDate;
    }

    public ReviewState getState() {
        return state;
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

    public void setRecommended(boolean recommended) {
        this.recommended = recommended;
    }

    public void setReviwText(String reviwText) {
        this.reviwText = reviwText;
    }

    public void setHoursPlayed(Long hoursPlayed) {
        this.hoursPlayed = hoursPlayed;
    }

    public void setPublicationDate(LocalDate publicationDate) {
        this.publicationDate = publicationDate;
    }

    public void setLastEditionDate(LocalDate lastEditionDate) {
        this.lastEditionDate = lastEditionDate;
    }

    public void setState(ReviewState state) {
        this.state = state;
    }


    //Constructor Actualizacion

    public ReviewEntity(Long id, Long idUser, Long idGame, boolean recommended, String reviwText, Long hoursPlayed, LocalDate publicationDate, LocalDate lastEditionDate, ReviewState state) {
        this.id = id;
        this.idUser = idUser;
        this.idGame = idGame;
        this.recommended = recommended;
        this.reviwText = reviwText;
        this.hoursPlayed = hoursPlayed;
        this.publicationDate = publicationDate;
        this.lastEditionDate = lastEditionDate;
        this.state = state;
    }


    //Constructor Creacion

    public ReviewEntity(Long id, Long idUser, Long idGame, boolean recommended, String reviwText, Long hoursPlayed) {
        this.id = id;
        this.idUser = idUser;
        this.idGame = idGame;
        this.recommended = recommended;
        this.reviwText = reviwText;
        this.hoursPlayed = hoursPlayed;
        this.publicationDate = LocalDate.now();
        this.lastEditionDate = LocalDate.now();
        this.state = ReviewState.PUBLICADA;
    }

    @Override
    public String toString() {
        return "ReviewEntity{" +
                "id=" + id +
                ", idUser=" + idUser +
                ", idGame=" + idGame +
                ", recommended=" + recommended +
                ", reviwText='" + reviwText + '\'' +
                ", hoursPlayed=" + hoursPlayed +
                ", publicationDate=" + publicationDate +
                ", lastEditionDate=" + lastEditionDate +
                ", state=" + state +
                '}';
    }
}
