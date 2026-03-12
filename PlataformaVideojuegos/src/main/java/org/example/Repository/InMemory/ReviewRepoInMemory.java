package org.example.Repository.InMemory;

import org.example.Model.Entidad.GameEntity;
import org.example.Model.Entidad.ReviewEntity;
import org.example.Model.Form.ReviewForm;
import org.example.Model.Form.Updates.ReviewUpdate;
import org.example.Repository.Interface.IReviewRepo;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ReviewRepoInMemory implements IReviewRepo {

    private static final List<ReviewEntity> REVIEWS = new ArrayList<>();
    private static Long NEXT_ID = 0L;

    @Override
    public Optional<ReviewEntity> create(ReviewForm form) {
        var review = new ReviewEntity(NEXT_ID++, form.getIdUser(), form.getIdGame(), form.isRecommended(), form.getReviwText(), form.getHoursPlayed(), form.getPublicationDate(), form.getLastEditionDate(), form.getState());
        REVIEWS.add(review);
        return Optional.of(review);
    }

    @Override
    public Optional<ReviewEntity> getById(Long id) {
        return REVIEWS.stream().filter(review -> review.getId().equals(id)).findFirst();
    }

    @Override
    public List<ReviewEntity> getAll() {return new ArrayList<>(REVIEWS);
    }

    @Override
    public Optional<ReviewEntity> update(Long id, ReviewUpdate form) {
        getById(id).orElseThrow(()-> new IllegalArgumentException("Reseña no encontrada"));

        var reviewUpdated = new GameEntity(form.id(), form.tittle(), form.description(), form.developer(), form.launchDate(), form.basePrice(), form.currentDescount(), form.category(), form.gameAgeClasification(), form.availabeLanguages(), form.State());
        REVIEWS.removeIf(p -> p.getId().equals(id));
        REVIEWS.add(reviewUpdated);
        return Optional.of(reviewUpdated);
    }

    @Override
    public boolean delete(Long id) {
        return REVIEWS.removeIf(p -> p.getId().equals(id));
    }
}
