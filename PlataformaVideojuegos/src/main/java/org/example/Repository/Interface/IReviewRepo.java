package org.example.Repository.Interface;

import org.example.Model.Entidad.ReviewEntity;
import org.example.Model.Form.ReviewForm;
import org.example.Model.Form.Updates.ReviewUpdateForm;

public interface IReviewRepo extends ICrud<ReviewEntity, ReviewForm, ReviewUpdateForm, Long>{
}
