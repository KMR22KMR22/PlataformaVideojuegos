package Model.DTO.Review;

import java.time.Duration;
import java.util.Date;

public class ReviewDTO {

    private Long id;
    private int idUser;
    private int idGame;
    private boolean recommended;
    private String reviwText;
    private Duration hoursPlaid;
    private Date publicationDate;
    private Date lastEditionDate;
    private  ReviewState state;


    //Getters


    public Long getId() {
        return id;
    }

    public int getIdUser() {
        return idUser;
    }

    public int getIdGame() {
        return idGame;
    }

    public boolean isRecommended() {
        return recommended;
    }

    public String getReviwText() {
        return reviwText;
    }

    public Duration getHoursPlaid() {
        return hoursPlaid;
    }

    public Date getPublicationDate() {
        return publicationDate;
    }

    public Date getLastEditionDate() {
        return lastEditionDate;
    }

    public ReviewState getState() {
        return state;
    }


    //Setters


    public void setRecommended(boolean recommended) {
        this.recommended = recommended;
    }

    public void setReviwText(String reviwText) {
        this.reviwText = reviwText;
    }

    public void setHoursPlaid(Duration hoursPlaid) {
        this.hoursPlaid = hoursPlaid;
    }

    public void setLastEditionDate(Date lastEditionDate) {
        this.lastEditionDate = lastEditionDate;
    }

    public void setState(ReviewState state) {
        this.state = state;
    }


    //Constructor


    public ReviewDTO(Long id, int idUser, int idGame, boolean recommended, String reviwText, Duration hoursPlaid, Date publicationDate, Date lastEditionDate, ReviewState state) {
        this.id = id;
        this.idUser = idUser;
        this.idGame = idGame;
        this.recommended = recommended;
        this.reviwText = reviwText;
        this.hoursPlaid = hoursPlaid;
        this.publicationDate = publicationDate;
        this.lastEditionDate = lastEditionDate;
        this.state = state;
    }
}
