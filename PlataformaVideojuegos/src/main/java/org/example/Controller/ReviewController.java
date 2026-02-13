package org.example.Controller;

import org.example.Model.Form.ReviewForm;
import org.example.Repository.InMemory.ReviewRepoInMemory;

import java.util.ArrayList;
import java.util.List;

public class ReviewController {

    private ReviewRepoInMemory repository = new ReviewRepoInMemory();
    private List<String> errores = new ArrayList<>();

    public List<String> validateReview(ReviewForm review) {
        errores.clear();

        //Usuario
        validateUser(review);

        //Juego
        validateGame(review);

        //Recomendado
        validateRecomended(review);

        //Tesxto de la reseña
        validateReviewText(review);

        //Horas jugadas al momento de la reseña
        validateHoursPlayed(review);

        //Fecha de publicacion
        validatePublicactionDate(review);

        //Fecha de utima edicion
        validateLastEditionDate(review);

        //Estado
        validateState(review);

    }
}
