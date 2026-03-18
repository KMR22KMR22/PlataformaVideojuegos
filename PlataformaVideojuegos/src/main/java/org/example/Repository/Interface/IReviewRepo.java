package org.example.Repository.Interface;

import org.example.Model.Entidad.ReviewEntity;
import org.example.Model.Form.ReviewForm;
import org.example.Model.Form.Updates.ReviewUpdate;

import java.util.Optional;

public interface IReviewRepo extends ICrud<ReviewEntity, ReviewForm, ReviewUpdate, Long>{
    Optional<ReviewEntity> getByidGame(Long id);
}
