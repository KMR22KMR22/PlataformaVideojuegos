package org.example.repository.Interface;

import org.example.model.entidad.ReviewEntity;
import org.example.model.form.ReviewForm;
import org.example.model.form.updates.ReviewUpdate;

import java.util.List;

public interface IReviewRepo extends ICrud<ReviewEntity, ReviewForm, ReviewUpdate, Long>{
    List<ReviewEntity> getByidGame(Long id);
}
