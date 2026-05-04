package org.example.repository.Interface;

import org.example.model.entidad.ReviewEntity;
import org.example.model.form.ReviewForm;
import org.example.model.form.updates.ReviewUpdate;

import java.util.List;
import java.util.Optional;

public interface IReviewRepo extends ICrud<ReviewEntity, ReviewForm, ReviewUpdate, Long>{
    Optional<ReviewEntity> getByUserGameId(Long idUser, Long idGame);

    List<ReviewEntity> getByidGame(Long id);

    List<ReviewEntity> getByUserId(Long id);
}
