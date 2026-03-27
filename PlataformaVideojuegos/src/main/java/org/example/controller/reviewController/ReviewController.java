package org.example.controller.reviewController;

import org.example.controller.Util;
import org.example.exeptions.ValidationException;
import org.example.mapper.Mapper;
import org.example.model.dto.review.ReviewDTO;
import org.example.model.dto.review.ReviewState;
import org.example.model.entidad.GameEntity;
import org.example.model.entidad.LibraryEntity;
import org.example.model.entidad.ReviewEntity;
import org.example.model.entidad.UserEntity;
import org.example.model.form.errors.ErrorDto;
import org.example.model.form.errors.ErrorType;
import org.example.model.form.ReviewForm;
import org.example.model.form.updates.ReviewUpdate;
import org.example.repository.Interface.IGameRepo;
import org.example.repository.Interface.ILibraryRepo;
import org.example.repository.Interface.IReviewRepo;
import org.example.repository.Interface.IUserRepo;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ReviewController {

    private IReviewRepo reviewRepo;
    private IUserRepo userRepo;
    private IGameRepo gameRepo;
    private ILibraryRepo libraryRepo;


    //Constructor


    public ReviewController(IReviewRepo reviewRepo, IUserRepo userRepo, IGameRepo gameRepo, ILibraryRepo libraryRepo) {
        this.reviewRepo = reviewRepo;
        this.userRepo = userRepo;
        this.gameRepo = gameRepo;
        this.libraryRepo = libraryRepo;
    }


    /** Crear una nueva reseña para un juego que el usuario posee
     * @param idUser id del usuario que hace la reseña
     * @param idGame id del juego al qeu se le hace la reseña
     * @param recomended Se recomienda o no
     * @param reviewText texto de la reseña
     * @return ReviewDTO con los datos de la reseña
     * @throws ValidationException
     * */
    public ReviewDTO writeReview(Long idUser, Long idGame, boolean recomended, String reviewText) throws ValidationException {
        List<ErrorDto> errors =  new ArrayList<>();

        UserEntity user = userRepo.getById(idUser).orElse(null);
        if (user == null) {
            errors.add(new ErrorDto("UserId", ErrorType.NO_ENCONTRADO));
        }

        GameEntity game = gameRepo.getById(idGame).orElse(null);
        if (game == null) {
            errors.add(new ErrorDto("GameId", ErrorType.NO_ENCONTRADO));
        }

        LibraryEntity library = libraryRepo.getByUserGameId(idUser, idGame).orElse(null);
        if (library == null){
            errors.add(new ErrorDto("LibraryIdGame, LibraryIdUser", ErrorType.NO_ENCONTRADO));
        }

        Util.thowException(errors);
        errors.clear();

        ReviewForm form = new ReviewForm(idUser, idGame, recomended, reviewText, library.getTimePlaying());

        //Validaciones del formulario
        errors.addAll(form.validate());
        errors.addAll(validate(form));

        Util.thowException(errors);

        var reviewOpt = reviewRepo.create(form);
        var reviewUpdated = reviewOpt.orElseThrow(()-> new IllegalArgumentException("No se puedo crear la biblioteca"));

        return Mapper.mapFrom(reviewUpdated);
    }

    /** Cambiar el estado de una reseña a eliminada
     * @param idReview id de la reseña
     * @param idUser id del usuario que hizo la reseña
     * @return Confirmación de eliminación
     * */
    public boolean delteReview (Long idReview, Long idUser) throws ValidationException {
        List<ErrorDto> errors =  new ArrayList<>();
        boolean deleted = false;

        //Compruebo que la reseña existe
        ReviewEntity review = reviewRepo.getById(idReview).orElse(null);
        if (review == null) {
            errors.add(new ErrorDto("ReviewId", ErrorType.NO_ENCONTRADO));
        }

        //Compruebo que la reseña corresponda al usuario
        if(idUser != review.getIdUser()) {
            errors.add(new ErrorDto("UserId, ReviewId", ErrorType.NO_ENCONTRADO));
        }

        //Compruebo que el usuario exista en el repositorio
        UserEntity user = userRepo.getById(idUser).orElse(null);
        if (user == null){
            errors.add(new ErrorDto("UserId", ErrorType.NO_ENCONTRADO));
        }

        Util.thowException(errors);

        ReviewUpdate form = new ReviewUpdate(review.getId(), review.getIdUser(), review.getIdGame(), review.isRecommended(), review.getReviwText(), review.getHoursPlayed(), review.getPublicationDate(), review.getLastEditionDate(), ReviewState.ELIMINADA);
        var reviewOpt = reviewRepo.update(idReview, form);

        if(reviewOpt.isPresent()) {
            deleted = true;
        }
        return deleted;
    }



    /**Realiza las validaciones del UserForm que necesitan acceso a datos
     * @param idGame Id del juego
     * @param recomended Se recomienda o no
     * @param order Parametro para realizar la busqueda
     * @return  Lista de reseñas con estadísticas generales
     * */
    public List<ReviewDTO> showReviews(Long idGame, Optional<Boolean> recomended, Optional<Order> order) throws ValidationException {
        List<ErrorDto> errors =  new ArrayList<>();

        GameEntity game = gameRepo.getById(idGame).orElse(null);
        if (game == null) {
            errors.add(new ErrorDto("GameId", ErrorType.NO_ENCONTRADO));
        }

        List<ReviewEntity> reviewEntitys = reviewRepo.getByidGame(idGame);
        if (reviewEntitys.isEmpty()) {
            errors.add(new ErrorDto("ReviewId", ErrorType.NO_ENCONTRADO));
        }

        Util.thowException(errors);

        if(recomended.isPresent()) {
            reviewEntitys = reviewEntitys.stream()
                    .filter(r -> r.isRecommended())
                    .toList();
        }

        return reviewEntitys.stream().map(r -> Mapper.mapFrom(r)).toList();
    }


    /**Cambiar la visibilidad de una reseña a oculta
     * @param idReview Id de la reseña
     * @param idUser Id del usuario que hizo la reseña
     * @return  Confirmación de ocultación
     * */
    public boolean hideReview(Long idReview, Long idUser) throws ValidationException {
        List<ErrorDto> errors =  new ArrayList<>();
        boolean hidden = false;

        UserEntity user = userRepo.getById(idUser).orElse(null);
        if (user == null) {
            errors.add(new ErrorDto("UserId", ErrorType.NO_ENCONTRADO));
        }
        ReviewEntity review = reviewRepo.getById(idReview).orElse(null);
        if (review == null) {
            errors.add(new ErrorDto("ReviewId", ErrorType.NO_ENCONTRADO));
        }

        if(idUser != review.getIdUser()) {
            errors.add(new ErrorDto("UserId, ReviewId", ErrorType.NO_ENCONTRADO));
        }

        Util.thowException(errors);

        ReviewUpdate form = new ReviewUpdate(review.getId(), review.getIdUser(), review.getIdGame(), review.isRecommended(), review.getReviwText(), review.getHoursPlayed(), review.getPublicationDate(), review.getLastEditionDate(), ReviewState.OCULTA);
        var reviewOpt = reviewRepo.update(idReview, form);

        if(reviewOpt.isPresent()) {
            hidden = true;
        }
        return hidden;
    }


    /**Listar todas las reseñas escritas por un usuario específico
     * @param idUser Id del usuario que hizo las reseñas
     * @return Lista con errores, en caso de no haber devuelve la lista vacia
     * */
    public List<ReviewDTO> showReviewsFromUser(Long idUser, Optional<Order> order) throws ValidationException {
        List<ErrorDto> errors =  new ArrayList<>();

        List<ReviewEntity> reviews = reviewRepo.getAll().stream()
                .filter(r -> r.getIdUser() == idUser)
                .toList();

        if(reviews.isEmpty()) {
            errors.add(new ErrorDto("ReviewId, UserId", ErrorType.NO_ENCONTRADO));
        }

        Util.thowException(errors);

        return  reviews.stream().map(r -> Mapper.mapFrom(r)).toList();
    }



    /**Realiza las validaciones del UserForm que necesitan acceso a datos
     * @param review Formulario con los datos introducidos por el usuario
     * @return Lista con errores, en caso de no haber devuelve la lista vacia
     * */
    public List<ErrorDto> validate(ReviewForm review){
        List<ErrorDto> errors = new ArrayList<>();

        //usuario existe
        if(userRepo.getById(review.idUser()).isEmpty()){
            errors.add(new ErrorDto("User", ErrorType.NO_ENCONTRADO));
        }
        //Juego existe
        if(gameRepo.getById(review.idUser()).isEmpty()){
            errors.add(new ErrorDto("Game", ErrorType.NO_ENCONTRADO));
        }
        //Usuario tiene juego
        if(libraryRepo.getByUserGameId(review.idUser(), review.idGame()).isEmpty()){
            errors.add(new ErrorDto("Library", ErrorType.NO_ENCONTRADO));
        }
        return errors;
    }

}
