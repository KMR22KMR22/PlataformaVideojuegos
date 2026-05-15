package org.example.model.entidad;

import jakarta.persistence.*;
import org.example.model.dto.review.ReviewState;

import java.time.LocalDate;

@Table(name = "resena")
@Entity
public class ReviewEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "id_usuario")
    private Long idUser;
    @Column(name = "id_juego")
    private Long idGame;
    @Column(name = "recomendado")
    private boolean recommended;
    @Column(name = "texto_reseña")
    private String reviwText;
    @Column(name = "horas_jugadas")
    private Long hoursPlayed;
    @Column(name = "fecha_publicacion")
    private LocalDate publicationDate;
    @Column(name = "fecha_ultima_edicion")
    private LocalDate lastEditionDate;
    @Column(name = "estado")
    private ReviewState state;

    public ReviewEntity() {

    }


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
