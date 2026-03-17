package org.example.Controller;

import org.example.Model.DTO.Review.ReviewDTO;
import org.example.Model.Form.ReviewForm;
import org.example.Repository.Interface.IReviewRepo;

public class ReviewController {

    private IReviewRepo repository;

    public ReviewDTO writeReview(Long idUser, Long idGame, boolean recomended, String reviewText) {

        ReviewForm form = new ReviewForm(idUser, idGame, recomended, reviewText, )
    }

}
