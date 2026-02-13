package org.example.Repository.InMemory;

import org.example.Model.Entidad.ReviewEntity;
import org.example.Model.Form.ReviewForm;
import org.example.Repository.Interface.IReviewRepo;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ReviewRepoInMemory implements IReviewRepo {

    private List<ReviewEntity> reviews = new ArrayList<>();
    private Long idCounter = 0L;

    @Override
    public Optional<ReviewEntity> crear(ReviewForm form) {
        var review = new ReviewEntity(idCounter++, form.getIdUser(), form.getIdGame(), form.isRecommended(), form.getReviwText(), form.getHoursPlayed(), form.getPublicationDate(), form.getLastEditionDate(), form.getState());
        reviews.add(review);
        return Optional.of(review);
    }

    @Override
    public Optional<ReviewEntity> obtenerPorId(Long id) {
        return reviews.stream().filter(review -> review.getId().equals(id)).findFirst();
    }

    @Override
    public List<ReviewEntity> obtenerTodos() {return new ArrayList<>(reviews);
    }

    @Override
    public Optional<ReviewEntity> actualizar(Long aLong, ReviewForm dto) {
        return Optional.empty();
    }

    @Override
    public boolean eliminar(Long aLong) {
        return false;
    }
}
