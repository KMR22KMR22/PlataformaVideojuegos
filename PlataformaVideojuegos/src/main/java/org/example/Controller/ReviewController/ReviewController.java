package org.example.Controller.ReviewController;

import org.example.Exeptions.ValidationException;
import org.example.Mapper.Mapper;
import org.example.Model.DTO.Review.ReviewDTO;
import org.example.Model.DTO.Review.ReviewState;
import org.example.Model.Entidad.GameEntity;
import org.example.Model.Entidad.LibraryEntity;
import org.example.Model.Entidad.ReviewEntity;
import org.example.Model.Form.Errors.ErrorDto;
import org.example.Model.Form.Errors.ErrorType;
import org.example.Model.Form.ReviewForm;
import org.example.Model.Form.Updates.ReviewUpdate;
import org.example.Repository.Interface.IGameRepo;
import org.example.Repository.Interface.ILibraryRepo;
import org.example.Repository.Interface.IReviewRepo;
import org.example.Repository.Interface.IUserRepo;

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

        LibraryEntity library = libraryRepo.getByUserGameId(idUser, idGame).orElseThrow(()-> new IllegalArgumentException("Biblioteca no encontrada"));

        ReviewForm form = new ReviewForm(idUser, idGame, recomended, reviewText, library.getTimePlaying());

        //Validaciones del formulario
        errors.addAll(form.validate());
        errors.addAll(validate(form));

        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }

        var reviewOpt = reviewRepo.create(form);
        var reviewUpdated = reviewOpt.orElseThrow(()-> new IllegalArgumentException("No se puedo crear la biblioteca"));

        return Mapper.mapFrom(reviewUpdated);
    }

    /** Cambiar el estado de una reseña a eliminada
     * @param idReview id de la reseña
     * @param idUser id del usuario que hizo la reseña
     * @return Confirmación de eliminación
     * */
    public boolean delteReview (Long idReview, Long idUser) {
        boolean deleted = false;

        //Compruebo que la reseña existe
        ReviewEntity review = reviewRepo.getById(idReview).orElseThrow(()-> new IllegalArgumentException("Reseña no encontrada"));

        //Compruebo que la reseña corresponda al usuario
        if(idUser != review.getIdUser()) {
            throw new IllegalArgumentException("El id de la reseña no coincide con el id del usuario");
        }

        //Compruebo que el usuario exista en el repositorio
        userRepo.getById(idUser).orElseThrow(()-> new IllegalArgumentException("Usuario no encontrado"));

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
     * @return Lista con errores, en caso de no haber devuelve la lista vacia
     * */
    public List<ReviewDTO> showReviews(Long idGame, Optional<Boolean> recomended, Optional<Order> order) {
        GameEntity game = gameRepo.getById(idGame).orElseThrow(()-> new IllegalArgumentException("Juego no encontrado"));

        ReviewEntity
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
