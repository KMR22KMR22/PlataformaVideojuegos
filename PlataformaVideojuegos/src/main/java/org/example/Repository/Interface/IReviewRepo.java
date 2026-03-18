package org.example.Repository.Interface;

import org.example.Model.Entidad.ReviewEntity;
import org.example.Model.Form.ReviewForm;
import org.example.Model.Form.Updates.ReviewUpdate;

import java.util.List;

public interface IReviewRepo extends ICrud<ReviewEntity, ReviewForm, ReviewUpdate, Long>{
    List<ReviewEntity> getByidGame(Long id);
}
