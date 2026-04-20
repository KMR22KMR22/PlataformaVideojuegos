package org.example.model.entidad;

import jakarta.persistence.*;
import org.example.model.dto.library.InstalationState;

import java.time.LocalDate;
import java.util.Date;

@Table(name = "bibliotecas")
@Entity
public class LibraryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "id_usuario")
    private Long idUser;
    @Column(name = "id_juego")
    private Long idGame;
    @Column(name = "fecha_adquisicion")
    private LocalDate acquisitionDate;
    @Column(name = "tiempo_juego")
    private Long timePlaying;
    @Column(name = "ultimo_juego")
    private Date lastPlayed;
    @Column(name = "estado_instalacion")
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
