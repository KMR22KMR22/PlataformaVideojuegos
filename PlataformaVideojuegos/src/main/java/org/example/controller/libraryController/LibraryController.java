package org.example.controller.libraryController;

import org.example.controller.Util;
import org.example.exeptions.ValidationException;
import org.example.mapper.Mapper;
import org.example.model.dto.game.GameDTO;
import org.example.model.dto.library.InstalationState;
import org.example.model.dto.library.LibraryDTO;
import org.example.model.dto.user.UserDTO;
import org.example.model.entidad.LibraryEntity;
import org.example.model.entidad.UserEntity;
import org.example.model.form.errors.ErrorDto;
import org.example.model.form.errors.ErrorType;
import org.example.model.form.LibraryForm;
import org.example.model.form.updates.LibraryUpdate;
import org.example.repository.Interface.IGameRepo;
import org.example.repository.Interface.ILibraryRepo;
import org.example.repository.Interface.IUserRepo;
import org.example.transaction.ITransactionManager;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class LibraryController {

    private ILibraryRepo libraryRepo;
    private IUserRepo userRepo;
    private IGameRepo gameRepo;
    public ITransactionManager tm;


    //Constructor


    public LibraryController(ILibraryRepo libraryRepo, IUserRepo userRepo, IGameRepo gameRepo, ITransactionManager tm) {
        this.libraryRepo = libraryRepo;
        this.userRepo = userRepo;
        this.gameRepo = gameRepo;
        this.tm = tm;
    }


    /**
     * Lista todos los juegos que posee un usuario en su biblioteca
     *
     * @param userId Id del usuario que va a comprar el juego
     * @param order  Orden en que se muestran los juegos (opcional)
     * @return Lista de bibliotecas con los juegos que posee el usuario
     *
     */
    public List<LibraryDTO> showPersonalLibrary(Long userId, Optional<OrderParameters> order) throws ValidationException {
        List<ErrorDto> errors = new ArrayList<>();
        //Compruebo que el userId no sea null
        if (userId == null) {
            errors.add(new ErrorDto("UserId", ErrorType.REQUERIDO));
        }
        //Compruebo errores para lanzar exepcion
        Util.throwException(errors);

        List<LibraryDTO> libraries = showLibraryStats(userId);

        if (order.isPresent()) {

            switch (order.get()) {

                case ALPHABETICAL:
                    return libraries.stream()
                            .sorted((g1, g2) -> g1.game().title().compareToIgnoreCase(g2.game().title()))
                            .toList();

                case GAME_TYPE:
                    return libraries.stream()
                            .sorted((g1, g2) -> g1.game().category().compareToIgnoreCase(g2.game().category()))
                            .toList();

                case LAST_SESION:
                    return libraries.stream()
                            .sorted((g1, g2) -> g1.lastPlayed().compareTo(g2.lastPlayed()))
                            .toList();

                case ADQUISITION_DATE:
                    return libraries.stream()
                            .sorted((g1, g2) -> g1.acquisitionDate().compareTo(g2.acquisitionDate()))
                            .toList();

                default:
                    throw new IllegalArgumentException("Operador no dado de alta: " + order.get().name());
            }
        }
        return libraries;
    }


    /**
     * Agregar un juego adquirido a la biblioteca del usuario
     *
     * @param userId Id del usuario que va comprar e jeugo
     * @param gameId id del juego
     * @return LibraryDTO con los datos de la libreria que se creó
     *
     */
    public LibraryDTO addGameToLibrary(Long gameId, Long userId) throws ValidationException {
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
        return tm.inTransaction(() -> {
            List<ErrorDto> transactionErrors = new ArrayList<>();

            //Valido
            transactionErrors.addAll(validate(gameId, userId));

            //Reviso si hay errores para lanzar exepcion
            Util.throwException(transactionErrors);

            //Creo el formulario de la biblioteca
            LibraryForm libraryForm = new LibraryForm(userId, gameId, LocalDate.now());

            //Creo la biblioteca
            LibraryEntity createdLibrary = libraryRepo.create(libraryForm).orElse(null);

            Optional<UserDTO> userDTO = Optional.ofNullable(Mapper.mapFrom(userRepo.getById(userId).orElse(null)));
            Optional<GameDTO> gameDTO = Optional.ofNullable(Mapper.mapFrom(gameRepo.getById(gameId).orElse(null)));

            return Mapper.mapFrom(createdLibrary, userDTO, gameDTO);
        });
    }


    /**
     * Quita un juego de la biblioteca del usuario
     *
     * @param gameId Id del juego a comprar
     * @param userId Id del usuario que va comprar e jeugo
     * @throws ValidationException
     *
     */
    public void deleteLibrary(Long userId, Long gameId) throws ValidationException {
        var errors = new ArrayList<ErrorDto>();

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
        tm.inTransaction(() -> {
            List<ErrorDto> transactionErrors = new ArrayList<>();

            //Busco la biblioteca
            LibraryEntity library = libraryRepo.getByUserGameId(userId, gameId).orElse(null);
            //Compruebo que se haya encontrado la biblioteca
            if (library == null) {
                transactionErrors.add(new ErrorDto("Library (UserId, GameId)", ErrorType.NO_ENCONTRADO));
            }

            //Reviso si hay errores para lanzar exepcion
            Util.throwException(transactionErrors);

            boolean deleted = libraryRepo.delete(library.getId());

            //Compruebo que se haya eliminado la biblioteca
            if (!deleted) {
                transactionErrors.add(new ErrorDto("Library", ErrorType.NO_ELIMINADO));
            }

            //Vuelvo a comprobar si hay errores, ya que si llega a este punto es que no hubo error al encontrar la biblioteca, pero si hubo error al borrarla
            Util.throwException(transactionErrors);

            return true;
            //Aquí devuelvo true solo porque la lambda me obliga, pero no necesito devolver nada, ya que lo estoy controlando todo con las exepciones
        });
    }


    /**
     * Registra y actualiza las horas jugadas de un juego
     *
     * @param gameId Id del juego a comprar
     * @param userId Id del usuario que va comprar e jeugo
     * @param time   Tiempo nuevo jugado
     * @return LibraryDTO con las horas de juego actualizadas
     * @throws ValidationException
     *
     */
    public LibraryDTO updateGameTime(Long userId, Long gameId, Long time) throws ValidationException {
        List<ErrorDto> errors = new ArrayList<>();

        //Compruebo que el userId, gameId y time no sean null
        if (userId == null) {
            errors.add(new ErrorDto("UserId", ErrorType.REQUERIDO));
        }
        if (gameId == null) {
            errors.add(new ErrorDto("GameId", ErrorType.REQUERIDO));
        }
        if (time == null) {
            errors.add(new ErrorDto("TimePlaying", ErrorType.REQUERIDO));
        } else {
            //Compruebo que el tiempo que entra no sea menor que cero
            if (time <= 0) {
                errors.add(new ErrorDto("TimePlaying", ErrorType.VALOR_DEMASIADO_BAJO));
            }
        }

        //Compruebo si hay errores para lanzar exepcion
        Util.throwException(errors);

        //Inicio Transaccion
        return tm.inTransaction(() -> {
            List<ErrorDto> transactionErrors = new ArrayList<>();

            //Encuentro la biblioteca que coincida con el id del juego y del usuario
            LibraryEntity library = libraryRepo.getByUserGameId(userId, gameId).orElse(null);
            //Compruebo que se haya encontrado la biblioteca
            if (library == null) {
                transactionErrors.add(new ErrorDto("Library (UserId, GameId)", ErrorType.NO_ENCONTRADO));
            }

            //Reviso si hay errores para lanzar exepcion
            Util.throwException(transactionErrors);

            //Calculo el nuevo tiempo
            Long updatedTime = library.getTimePlaying() + time;

            //Creo el formulario con el tiempo actualizado
            LibraryUpdate libraryForm = new LibraryUpdate(library.getId(), library.getIdUser(), library.getIdGame(), library.getAcquisitionDate(), updatedTime, library.getLastPlayed(), library.getInstalationState());

            //Actualizo la biblioteca
            LibraryEntity updatedLibrary = libraryRepo.update(libraryForm.id(), libraryForm).orElse(null);

            Optional<UserDTO> userDTO = Optional.ofNullable(Mapper.mapFrom(userRepo.getById(userId).orElse(null)));
            Optional<GameDTO> gameDTO = Optional.ofNullable(Mapper.mapFrom(gameRepo.getById(gameId).orElse(null)));

            return Mapper.mapFrom(updatedLibrary, userDTO, gameDTO);
        });
    }


    /**
     * Ver la última vez que se jugó a un juego específico
     *
     * @param gameId Id del juego a comprar
     * @param userId Id del usuario que va comprar e jeugo
     * @return LibraryDTO
     *
     */
    public LibraryDTO consultLastSession(Long userId, Long gameId) throws ValidationException {
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
        return tm.inTransaction(() -> {
            //busco la biblioteca
            LibraryEntity foundLibrary = libraryRepo.getByUserGameId(userId, gameId).orElse(null);

            Optional<UserDTO> userDTO = Optional.ofNullable(Mapper.mapFrom(userRepo.getById(userId).orElse(null)));
            Optional<GameDTO> gameDTO = Optional.ofNullable(Mapper.mapFrom(gameRepo.getById(gameId).orElse(null)));

            return Mapper.mapFrom(foundLibrary, userDTO, gameDTO);
        });
    }


    /**
     * Buscar juegos en la biblioteca personal según criterios
     *
     * @param userId           Id del juego a comprar
     * @param text             Texto por el que se va a filtrar (Optional)
     * @param instalationState estado de instalacion por el que se va a filtrar (Optional)
     * @return Lista con las bibliotecas filtradas
     *
     */
    public List<LibraryDTO> filterLibrary(Long userId, Optional<String> text, Optional<InstalationState> instalationState)
            throws ValidationException {

        List<ErrorDto> errors = new ArrayList<>();

        //Compruebo que el userId no sea null
        if (userId == null) {
            errors.add(new ErrorDto("UserId", ErrorType.REQUERIDO));
        }
        //Compruebo si hay errores para lanzar exepcion
        Util.throwException(errors);

        return tm.inTransaction(() -> {
            List<ErrorDto> transactionErrors = new ArrayList<>();

            // Compruebo que el usuario exista
            UserEntity user = userRepo.getById(userId).orElse(null);
            if (user == null) {
                transactionErrors.add(new ErrorDto("UserID", ErrorType.NO_ENCONTRADO));
            }

            UserDTO userDTO =
                    Mapper.mapFrom(userRepo.getById(userId).orElse(null));

            List<LibraryDTO> libraries =
                    libraryRepo.getAll().stream()
                            .filter(l -> Objects.equals(l.getIdUser(), userId))
                            .map(l -> Mapper.mapFrom(
                                    l,
                                    Optional.ofNullable(userDTO),
                                    Optional.ofNullable(
                                            Mapper.mapFrom(
                                                    gameRepo.getById(l.getIdGame()).orElse(null)
                                            )
                                    )
                            ))
                            .toList();

            if (libraries.isEmpty()) {
                transactionErrors.add(new ErrorDto("LibraryIDUser", ErrorType.NO_ENCONTRADO));
            }

            Util.throwException(transactionErrors);

            // Filtro por texto
            if (text.isPresent()) {
                String t = text.get().trim().toLowerCase();
                libraries = libraries.stream()
                        .filter(l -> l.game().title() != null &&
                                l.game().title().toLowerCase().contains(t))
                        .toList();
            }

            //Filtro por estado de instalacion
            if (instalationState.isPresent()) {
                InstalationState state = instalationState.get();
                libraries = libraries.stream()
                        .filter(l -> l.instalationState() == state)
                        .toList();
            }

            return libraries;
        });
    }

    /**
     * Muestra métricas generales de la biblioteca del usuario
     *
     * @param userId Id del usuario que va comprar e jeugo
     * @return Lista con todas las bibliotecas que coincidan con el usuario
     *
     */
    public List<LibraryDTO> showLibraryStats(Long userId) throws ValidationException {
        List<ErrorDto> errors = new ArrayList<>();

        //Compruebo que el userId y gameId no sean null
        if (userId == null) {
            errors.add(new ErrorDto("UserId", ErrorType.REQUERIDO));
        }
        //Compruebo si hay errores para lanzar exepcion
        Util.throwException(errors);

        //Inicio Transaccion

        return tm.inTransaction(() -> {
            List<ErrorDto> transactionErrors = new ArrayList<>();

            //Compruebo que el usuario exista
            UserEntity user = userRepo.getById(userId).orElse(null);
            if (user == null) {
                transactionErrors.add(new ErrorDto("UserID", ErrorType.NO_ENCONTRADO));
            }

            //Reviso si hay errores para lanzar exepcion
            Util.throwException(transactionErrors);

            //Encuentro las bibliotecas que coincidan con el jugador y las mapeo a DTOs
            Optional<UserDTO> userDTO =
                    Optional.ofNullable(Mapper.mapFrom(user));

            return libraryRepo.getAll().stream()
                    .filter(l -> Objects.equals(l.getIdUser(), userId))
                    .map(l -> Mapper.mapFrom(
                            l,
                            userDTO,
                            Optional.ofNullable(
                                    Mapper.mapFrom(
                                            gameRepo.getById(l.getIdGame()).orElse(null)
                                    )
                            )
                    ))
                    .toList();
        });
    }


    /**
     * Realiza las validaciones de la Biblioteca que necesitan acceso a datos
     *
     * @param gameId Id del juego a comprar
     * @param userId Id del usuario que va comprar e jeugo
     * @return Lista con errores, en caso de no haber devuelve la lista vacia
     *
     */
    public List<ErrorDto> validate(Long gameId, Long userId) {
        List<ErrorDto> errors = new ArrayList<>();

        //Compruebo que el userId y gameId no sean null
        if (userId == null) {
            errors.add(new ErrorDto("UserId", ErrorType.REQUERIDO));
        }
        if (gameId == null) {
            errors.add(new ErrorDto("GameId", ErrorType.REQUERIDO));
        }

        if (userId != null && gameId != null) {
            //Compruebo que el usuario exista en el repositorio
            if (userRepo.getById(userId).isEmpty()) {
                errors.add(new ErrorDto("IdUser", ErrorType.NO_ENCONTRADO));
            }

            //Compruebo que el juego exista en el repositorio
            if (gameRepo.getById(gameId).isEmpty()) {
                errors.add(new ErrorDto("Idgame", ErrorType.NO_ENCONTRADO));
            }

            if (libraryRepo.getAll().stream()
                    .anyMatch(l ->
                            Objects.equals(l.getIdUser(), userId) &&
                                    Objects.equals(l.getIdGame(), gameId)
                    )) {
                errors.add(new ErrorDto("IdGame, IdUser", ErrorType.DUPLICADO));
            }
        }

        return errors;
    }
}
