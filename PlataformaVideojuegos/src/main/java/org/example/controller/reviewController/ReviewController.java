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
import org.example.transaction.ITransactionManager;

import java.time.LocalDate;
import java.util.*;

public class ReviewController {

    private IReviewRepo reviewRepo;
    private IUserRepo userRepo;
    private IGameRepo gameRepo;
    private ILibraryRepo libraryRepo;
    public ITransactionManager tm;


    //Constructor


    public ReviewController(IReviewRepo reviewRepo, IUserRepo userRepo, IGameRepo gameRepo, ILibraryRepo libraryRepo, ITransactionManager tm) {
        this.reviewRepo = reviewRepo;
        this.userRepo = userRepo;
        this.gameRepo = gameRepo;
        this.libraryRepo = libraryRepo;
        this.tm = tm;
    }


    /**
     * Crear una nueva reseña para un juego que el usuario posee
     *
     * @param userId     id del usuario que hace la reseña
     * @param gameId     id del juego al qeu se le hace la reseña
     * @param recommended Se recomienda o no
     * @param reviewText texto de la reseña
     * @return ReviewDTO con los datos de la reseña
     * @throws ValidationException
     *
     */
    public ReviewDTO writeReview(Long userId, Long gameId, boolean recommended, String reviewText) throws ValidationException {
        List<ErrorDto> errors = new ArrayList<>();

        //Compruebo que el userId y gameId no sean null
        if (userId == null) {
            errors.add(new ErrorDto("UserId", ErrorType.REQUERIDO));
        }
        if (gameId == null) {
            errors.add(new ErrorDto("GameId", ErrorType.REQUERIDO));
        }

        //Compruebo si hay errores para lanzar exepcion
        Util.throwException(errors);

        //Inicio Transaccion

        return Mapper.mapFrom(tm.inTransaction(() -> {
            ReviewEntity newReview = null;

            //Compruebo que el usuario exista
            UserEntity user = userRepo.getById(userId).orElse(null);
            if (user == null) {
                errors.add(new ErrorDto("UserId", ErrorType.NO_ENCONTRADO));
            }
            //Compruebo que el juego exista
            GameEntity game = gameRepo.getById(gameId).orElse(null);
            if (game == null) {
                errors.add(new ErrorDto("GameId", ErrorType.NO_ENCONTRADO));
            }
            //Compruebo que el texto que me hayan pasado tenga algo
            if (Util.checkCadenaBlankOrEmpty(reviewText)) {
                errors.add(new ErrorDto("ReviewText", ErrorType.REQUERIDO));
            }
            //Si existe el usuario y el juego compruebo si existe una biblioteca que los relacione
            LibraryEntity libraryFound = null;
            if (user != null && game != null) {
                libraryFound = libraryRepo.getByUserGameId(userId, gameId).orElse(null);
                if (libraryFound == null) {
                    errors.add(new ErrorDto("LibraryIdGame, LibraryIdUser", ErrorType.NO_ENCONTRADO));
                }
            }

            //Compruebo si hay errores para mandar exepcion
            Util.throwException(errors);

            //Compruebo si el usuario ya habia hecho una reseña a ese juego anteriormente
            ReviewEntity review = reviewRepo.getByUserGameId(userId, gameId).orElse(null);

            //Si no ha hecho reseña creo una y si existe la actualizo
            if (review == null) {
                //Creo el formulario de la reseña
                ReviewForm form = new ReviewForm(userId, gameId, recommended, reviewText, libraryFound.getTimePlaying());

                //Validaciones del formulario
                errors.addAll(form.validate());

                //Si hay errores de validacion del formulario lanzo exepcion
                Util.throwException(errors);

                //Creo la reseña
                newReview = reviewRepo.create(form).orElse(null);
            }else {
                //Creo el formulario actualizado de la reseña
                ReviewUpdate form = new ReviewUpdate(review.getId(), userId, gameId, recommended, reviewText, libraryFound.getTimePlaying(), review.getPublicationDate(), LocalDate.now(), review.getState());

                //Actualizo la reseña
                newReview  = reviewRepo.update(review.getId(), form).orElse(null);
            }
            return newReview;
        }));
    }

    /**
     * Cambiar el estado de una reseña a eliminada
     *
     * @param reviewId id de la reseña
     * @param userId   id del usuario que hizo la reseña
     *
     */
    public ReviewDTO deleteReview(Long reviewId, Long userId) throws ValidationException {
        List<ErrorDto> errors = new ArrayList<>();

        //Compruebo que el userId y reviewId no sean null
        if (userId == null) {
            errors.add(new ErrorDto("UserId", ErrorType.REQUERIDO));
        }
        if (reviewId == null) {
            errors.add(new ErrorDto("ReviewId", ErrorType.REQUERIDO));
        }

        //Compruebo si hay errores para lanzar exepcion
        Util.throwException(errors);

        //Inicio transaccion
        ReviewEntity deletedReview = tm.inTransaction(() -> {
            ReviewEntity updatedReview = null;

            //Compruebo que el usuario exista en el repositorio
            UserEntity user = userRepo.getById(userId).orElse(null);
            if (user == null) {
                errors.add(new ErrorDto("UserId", ErrorType.NO_ENCONTRADO));
            }
            //Compruebo que la reseña existe
            ReviewEntity review = reviewRepo.getById(reviewId).orElse(null);
            if (review == null) {
                errors.add(new ErrorDto("ReviewId", ErrorType.NO_ENCONTRADO));
            } else {
                //Compruebo que la reseña corresponda al usuario
                if (!Objects.equals(userId, review.getIdUser())) {
                    errors.add(new ErrorDto("UserId, ReviewId", ErrorType.NO_ENCONTRADO));
                }

                //Compruebo que la reseña no este previamente eliminada
                if (Objects.equals(ReviewState.ELIMINADA, review.getState())) {
                    errors.add(new ErrorDto("ReviewState", ErrorType.DUPLICADO));
                }

                //Compruebo si hay errores para lanzar exepcion
                Util.throwException(errors);

                //Creo el formulario con el estado de la reseña en eliminada
                ReviewUpdate form = new ReviewUpdate(review.getId(), review.getIdUser(), review.getIdGame(), review.isRecommended(), review.getReviwText(), review.getHoursPlayed(), review.getPublicationDate(), review.getLastEditionDate(), ReviewState.ELIMINADA);
                //Actualizo la reseña
                updatedReview = reviewRepo.update(reviewId, form).orElse(null);
            }
            return updatedReview;
        });
        return Mapper.mapFrom(deletedReview);
    }


    /**
     * Realiza las validaciones del UserForm que necesitan acceso a datos
     *
     * @param gameId     Id del juego
     * @param recommended Se recomienda o no
     * @param order      Parametro para realizar la busqueda
     * @return Lista de reseñas con estadísticas generales
     *
     */
    public List<ReviewDTO> showReviews(Long gameId, Optional<Boolean> recommended, Optional<Order> order) throws ValidationException {
        List<ErrorDto> errors = new ArrayList<>();

        //Compruebo que el gameId no sea null
        if (gameId == null) {
            errors.add(new ErrorDto("GameId", ErrorType.REQUERIDO));
        }
        //Compruebo si hay errores para lanzar exception
        Util.throwException(errors);

        //Inicio transaccion
        return tm.inTransaction(() -> {
            List<ReviewEntity> reviews = new ArrayList<>();

            //Compruebo que el juego exista
            GameEntity game = gameRepo.getById(gameId).orElse(null);
            if (game == null) {
                errors.add(new ErrorDto("GameId", ErrorType.NO_ENCONTRADO));
                //Compruebo si hay errores para lanzar exception
                Util.throwException(errors);
            }

            //Busco todas las reseñas que pertenezcan al juego y filtro las que esten en estado publicada
            reviews = reviewRepo.getByidGame(gameId).stream()
                    .filter(r -> r.getState() == ReviewState.PUBLICADA)
                    .toList();

            //Si se encontraron reseñas las filtro
            if (!reviews.isEmpty()) {
                //Si se paso por parametro recomended las filtro por ahí
                if (recommended.isPresent()) {
                    reviews = reviews.stream()
                            .filter(r -> r.isRecommended() == recommended.get())
                            .toList();
                }

                // Si se paso por parametro order las filtro por ahí
                if (order.isPresent()) {
                    switch (order.get()) {
                        case DATE:
                            reviews = reviews.stream()
                                    .sorted(Comparator.comparing(ReviewEntity::getPublicationDate))
                                    .toList();
                            break;

                        case HOURS:
                            reviews = reviews.stream()
                                    .sorted(Comparator.comparing(ReviewEntity::getHoursPlayed))
                                    .toList();
                            break;
                    }
                }
            }
            return reviews.stream().map(r -> Mapper.mapFrom(r)).toList();
        });
    }


    /**
     * Cambiar la visibilidad de una reseña a oculta
     *
     * @param reviewId Id de la reseña
     * @param userId   Id del usuario que hizo la reseña
     * @return Confirmación de ocultación
     *
     */
    public ReviewDTO hideReview(Long reviewId, Long userId) throws ValidationException {
        List<ErrorDto> errors = new ArrayList<>();

        //Compruebo que el userId y reviewId no sean null
        if (userId == null) {
            errors.add(new ErrorDto("UserId", ErrorType.REQUERIDO));
        }
        if (reviewId == null) {
            errors.add(new ErrorDto("ReviewId", ErrorType.REQUERIDO));
        }

        //Compruebo si hay errores para lanzar exepcion
        Util.throwException(errors);

        //Inicio transaccion
        return tm.inTransaction(()-> {
            //Compruebo que el usuario exista
            UserEntity user = userRepo.getById(userId).orElse(null);
            if (user == null) {
                errors.add(new ErrorDto("UserId", ErrorType.NO_ENCONTRADO));
            }
            //Compruebo que la reseña exista
            ReviewEntity review = reviewRepo.getById(reviewId).orElse(null);
            if (review == null) {
                errors.add(new ErrorDto("ReviewId", ErrorType.NO_ENCONTRADO));
            } else {
                //Compruebo que la reseña pertenezca al usuario
                if (!Objects.equals(userId, review.getIdUser())) {
                    errors.add(new ErrorDto("UserId, ReviewId", ErrorType.NO_ENCONTRADO));
                }
                //Compruebo que la reseña este en estado publicada
                if (review.getState() !=  ReviewState.PUBLICADA) {
                    errors.add(new ErrorDto("ReviewState", ErrorType.ESTADO_INCORRECTO));
                }
            }
            //Compruebo si hay errores para lanzar exepcion
            Util.throwException(errors);

            //Creo el formulario con el estado de la reseña en oculta
            ReviewUpdate form = new ReviewUpdate(review.getId(), review.getIdUser(), review.getIdGame(), review.isRecommended(), review.getReviwText(), review.getHoursPlayed(), review.getPublicationDate(), review.getLastEditionDate(), ReviewState.OCULTA);
            //Actualizo la reseña
            var updatedReview = reviewRepo.update(reviewId, form).orElse(null);

            return Mapper.mapFrom(updatedReview);
        });
    }


    /**
     * Listar todas las reseñas escritas por un usuario específico
     *
     * @param userId Id del usuario que hizo las reseñas
     * @return Lista con errores, en caso de no haber devuelve la lista vacia
     *
     */
    public List<ReviewDTO> showReviewsFromUser(Long userId, Optional<Order> order) throws ValidationException {
        List<ErrorDto> errors = new ArrayList<>();

        //Compruebo que el userId no sea null
        if (userId == null) {
            errors.add(new ErrorDto("UserId", ErrorType.REQUERIDO));
        }
        //Compruebo si hay errores para lanzar exepcion
        Util.throwException(errors);

        //Inicio transaccion
        return tm.inTransaction(()-> {
            //Compruebo que el usuario exista, si no lanzo exepcion
            UserEntity user = userRepo.getById(userId).orElse(null);
            if (user == null) {
                throw new ValidationException(List.of(
                        new ErrorDto("UserId", ErrorType.NO_ENCONTRADO)
                ));
            }

            //Busco las reseñas del usuario con el userId
            List<ReviewEntity> reviews = reviewRepo.getByUserId(userId);

            // Si se paso por parametro order las filtro por ahí
            if (order.isPresent()) {
                switch (order.get()) {
                    case DATE:
                        reviews = reviews.stream()
                                .sorted(Comparator.comparing(ReviewEntity::getPublicationDate))
                                .toList();
                        break;

                    case HOURS:
                        reviews = reviews.stream()
                                .sorted(Comparator.comparing(ReviewEntity::getHoursPlayed))
                                .toList();
                        break;
                }
            }

            return reviews.stream().map(r -> Mapper.mapFrom(r)).toList();
        });
    }
}
