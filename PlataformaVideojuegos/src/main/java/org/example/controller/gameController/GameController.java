package org.example.controller.gameController;

import org.example.controller.Util;
import org.example.exeptions.ValidationException;
import org.example.mapper.Mapper;
import org.example.model.dto.game.GameAgeClasification;
import org.example.model.dto.game.GameDTO;
import org.example.model.dto.game.GameState;
import org.example.model.entidad.GameEntity;
import org.example.model.form.errors.ErrorDto;
import org.example.model.form.errors.ErrorType;
import org.example.model.form.GameForm;
import org.example.model.form.updates.GameUpdate;

import org.example.repository.Interface.IGameRepo;
import org.example.transaction.ITransactionManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class GameController {

    public static final int MIN_DISCOUNT = 0;
    public static final int MAX_DISCOUNT = 100;
    private IGameRepo gameRepo;
    public ITransactionManager tm;


    //Constructor

    public GameController(IGameRepo gameRepo, ITransactionManager tm) {
        this.gameRepo = gameRepo;
        this.tm = tm;
    }

    /**
     * Registra un nuevo videojuego en el catálogo de Steam
     *
     * @param gameForm Formulario con los datos introducidos por el usuario
     * @return GameDTO del juego añadido
     * @throws ValidationException
     *
     */
    public GameDTO addNewGame(GameForm gameForm) throws ValidationException {
        //Compruebo que el formulario no sea null
        if (gameForm == null) {
            throw new ValidationException(List.of(new ErrorDto("Form", ErrorType.REQUERIDO)));
        }

        //Inicio transaccion
        var createdGame = tm.inTransaction(() -> {
            List<ErrorDto> errors = new ArrayList<>();

            //LLamo al validate del formulario y guardo la lista de errores
            errors.addAll(gameForm.validate());

            //LLamo al validate del controlador y guardo la lista de errores
            errors.addAll(validate(gameForm));

            //Compreubo errores
            Util.throwException(errors);

            return gameRepo.create(gameForm);
        }).orElse(null);

        return Mapper.mapFrom(createdGame);
    }


    /**
     * Filtra y busca juegos en el catálogo según múltiples criterios
     *
     * @param texto            Texto libre para filtrar por título o descripción (opcional)
     * @param category         Categoría del juego (opcional)
     * @param minPrice         Precio minimo posible (opcional)
     * @param maxPrice         Precio maximo posible (opcional)
     * @param ageClasification Clasificación por edad del juego (opcional)
     * @param gameState        Estado del juego (opcional)
     * @return Lista de juegos encontrados, en caso de no encontrar ninguno se devuelve la lista vacia
     *
     */
    public List<GameDTO> findGames(Optional<String> texto, Optional<String> category, Optional<Integer> minPrice, Optional<Integer> maxPrice, Optional<GameAgeClasification> ageClasification, Optional<GameState> gameState) throws ValidationException {
        List<ErrorDto> errors = new ArrayList<>();

        //Compruebo que al menos haya un parametro de entrada que tenga valor, si todos son null devuelvo una exepcion
        if (texto.isEmpty() && category.isEmpty() && minPrice.isEmpty()
                && maxPrice.isEmpty() && ageClasification.isEmpty() && gameState.isEmpty()) {
            errors.add(new ErrorDto("text,category,minPricy,maxPrice,ageClasification,gameState", ErrorType.REQUERIDO));
        } else {
            //Comprueba que el precio maximo no sea menor al precio minimo
            if (minPrice.isPresent() && maxPrice.isPresent()) {
                if (minPrice.get() > maxPrice.get()) {
                    errors.add(new ErrorDto("MinPriece, MaxPriece", ErrorType.FORMATO_INVALIDO));
                }
            }

        }

        //Compruebo si hay errores
        Util.throwException(errors);

        //Inicio la transaccion

        return tm.inTransaction(() -> {

            //Filtro la lista de juegos del repositorio, la mapeo a DTO y devuelvo una lista con los juegos que coincidan con todos los parametros de busqueda a la vez
            return gameRepo.getAll().stream()
                    .filter(g -> texto.isEmpty() ||
                            (!texto.get().trim().isBlank() &&
                                    (g.getTittle() != null &&
                                            g.getTittle().toLowerCase()
                                                    .contains(texto.get().trim().toLowerCase())
                                            ||
                                            g.getDescription() != null &&
                                                    g.getDescription().toLowerCase()
                                                            .contains(texto.get().trim().toLowerCase())
                                    )))

                    .filter(g -> category.isEmpty() ||
                            (!category.get().trim().isBlank() &&
                                    g.getCategory() != null &&
                                    g.getCategory().toLowerCase()
                                            .contains(category.get().trim().toLowerCase())))

                    .filter(g -> minPrice.isEmpty() ||
                            g.getBasePrice() >= minPrice.get())

                    .filter(g -> maxPrice.isEmpty() ||
                            g.getBasePrice() <= maxPrice.get())

                    .filter(g -> ageClasification.isEmpty() ||
                            g.getAgeClasification().equals(ageClasification.get()))

                    .filter(g -> gameState.isEmpty() ||
                            g.getState().equals(gameState.get()))

                    .map(Mapper::mapFrom)
                    .toList();
        });
    }


    /**
     * Lista todos los juegos disponibles en la plataforma
     *
     * @param orderParameter Parametro por el que el usuario quiere que se ordenen los juegos al mostrarse (optional)
     * @return Lista con todos los juegos ordenados por título, precio o fecha de lanzamiento. En caso de no aclararse se muestran todos los juegos disponibles en el orden que ya esten guardados
     *
     */
    public List<GameDTO> consultWholeCatalogue(Optional<OrderParameters> orderParameter) throws ValidationException {

        //Inicio Transaccion

        //Guardo los juegos almacenados en el repositorio los cuales esten como disponible en una lista
        List<GameDTO> games = tm.inTransaction(() -> {
            return gameRepo.getAll().stream()
                    .filter(g -> g.getState() != GameState.NO_DISPONIBLE)
                    .map(g -> Mapper.mapFrom(g))
                    .toList();
        });

        if (orderParameter.isPresent()) {
            switch (orderParameter.get()) {
                case ALPHABETICAL:
                    return games.stream()
                            .sorted((g1, g2) -> g1.title().compareToIgnoreCase(g2.title()))
                            .toList();
                case PRICE:
                    return games.stream()
                            .sorted((g1, g2) -> Float.compare(g1.basePrice(), g2.basePrice()))
                            .toList();
                case DATE:
                    return games.stream()
                            .sorted((g1, g2) -> g1.launchDate().compareTo(g2.launchDate()))
                            .toList();
                default:
                    throw new IllegalArgumentException("Operador no dado de alta: " + orderParameter.get().name());

            }
        }
        return games;
    }


    /**
     * Muestra toda la información completa de un juego específico
     *
     * @param id id del juego a buscar
     * @return GameStatsDTO con la información del juego
     *
     */
    public GameDTO consultGameDetails(Long id) throws ValidationException {
        List<ErrorDto> errors = new ArrayList<>();

        //Compruebo que id no sea null
        if (id == null) {
            errors.add(new ErrorDto("GameId", ErrorType.REQUERIDO));
        }
        Util.throwException(errors);

        //Inicio Transaccion
        //Encuentro el juego
        GameEntity game = tm.inTransaction(() -> gameRepo.getById(id).orElse(null));

        //Si no encuentro el juego agrego el error a la lista
        if (game == null) {
            errors.add(new ErrorDto("IdGame", ErrorType.NO_ENCONTRADO));
        }

        //Si hay herrores lanzo la exepcion
        Util.throwException(errors);

        return Mapper.mapFrom(game);
    }


    /**
     * Establece un porcentaje de descuento temporal a un juego
     *
     * @param id      id del juego a buscar
     * @param percent porciento a descontar del precio del juego
     * @return GameDTO con el descuento aplicado
     *
     */
    public GameDTO applyDiscount(Long id, Integer percent) throws ValidationException {
        List<ErrorDto> errors = new ArrayList<>();

        //Compruebo que el id no sea null
        if (id == null) {
            errors.add(new ErrorDto("GameId", ErrorType.REQUERIDO));
        }
        //Copruebo que el porciento que se quiere aplicar no sea null y esté en un rango correcto
        if (percent == null) {
            errors.add(new ErrorDto("Discount", ErrorType.REQUERIDO));
        } else {
            if (percent < MIN_DISCOUNT) {
                errors.add(new ErrorDto("Discount", ErrorType.VALOR_DEMASIADO_BAJO));
            }
            if (percent > MAX_DISCOUNT) {
                errors.add(new ErrorDto("Discount", ErrorType.VALOR_DEMASIADO_ALTO));
            }
        }
        //Si hay herrores lanzo la exepcion
        Util.throwException(errors);

        //Inicio Transaccion
        //Busco el juego en el repositorio
        GameEntity updatedGame = tm.inTransaction(() -> {
            List<ErrorDto> transactionErrors = new ArrayList<>();

            //Busco el juego
            GameEntity entity = gameRepo.getById(id).orElse(null);
            //Compruebo que haya encontrado el juego
            if (entity == null) {
                transactionErrors.add(new ErrorDto("GameId", ErrorType.NO_ENCONTRADO));
            }

            //Si hay herrores lanzo la exepcion
            Util.throwException(transactionErrors);

            //Creo el formulario con los datos del juego actualizados
            GameUpdate form = new GameUpdate(entity.getId(), entity.getTittle(), entity.getDescription(), entity.getDeveloper(), entity.getLaunchDate(), entity.getBasePrice(), percent, entity.getCategory(), entity.getAgeClasification(), entity.getAvailabeLanguages(), entity.getState());

            return gameRepo.update(id, form).orElse(null);
        });

        return Mapper.mapFrom(updatedGame);
    }


    /**
     * Modifica el estado de disponibilidad de un juego
     *
     * @param id       id del juego a buscar
     * @param newState nuevo estado al que se va a cambiar el juego (opcional)
     * @return Confirmación del cambio de estado o mensaje de error
     * @throws ValidationException
     *
     */
    public GameDTO changeGameState(Long id, GameState newState) throws ValidationException {
        List<ErrorDto> errors = new ArrayList<>();

        //Compruebo que id no sea null
        if (id == null) {
            errors.add(new ErrorDto("GameId", ErrorType.REQUERIDO));
        }
        //Compruebo que el newState no sea null
        if (newState == null) {
            errors.add(new ErrorDto("GameState", ErrorType.REQUERIDO));
        }
        //Compruebo que el nuevo estado esté entre los admisibles
        if (Arrays.stream(GameState.values())
                .noneMatch(gameState -> gameState
                        .equals(newState))) {
            errors.add(new ErrorDto("GameState", ErrorType.NO_ENCONTRADO));
        }

        //Si hay errores lanzo exepcion
        Util.throwException(errors);

        //Inicio transaccion
        GameEntity updatedGame = tm.inTransaction(() -> {
            List<ErrorDto> transactionErrors = new ArrayList<>();

            //Busco el juego en el repositorio
            GameEntity entity = gameRepo.getById(id).orElse(null);
            //Compruebo que se haya encontrado el juego
            if (entity == null) {
                transactionErrors.add(new ErrorDto("IdGame", ErrorType.NO_ENCONTRADO));
            }
            //Si hay herrores lanzo la exepcion
            Util.throwException(transactionErrors);

            //Creo el formulario con los datos del juego actualizados
            GameUpdate form = new GameUpdate(entity.getId(), entity.getTittle(), entity.getDescription(), entity.getDeveloper(), entity.getLaunchDate(), entity.getBasePrice(), entity.getCurrentDescount(), entity.getCategory(), entity.getAgeClasification(), entity.getAvailabeLanguages(), newState);

            return gameRepo.update(id, form).orElse(null);
        });

        return Mapper.mapFrom(updatedGame);
    }


    /**
     * Realiza las validaciones del GameForm que necesitan acceso a datos
     *
     * @param game Formulario con los datos introducidos por el usuario
     * @return Lista con errores en caso de haber
     *
     */
    public List<ErrorDto> validate(GameForm game) {
        List<ErrorDto> errores = new ArrayList<>();

        //Compruebo que game no sea null
        if (game == null) {
            errores.add(new ErrorDto("GameForm", ErrorType.REQUERIDO));
        } else {
            //Comprueba que el titulo no se repita
            if (gameRepo.getAll().stream().anyMatch(g -> g.getTittle().equals(game.tittle()))) {
                errores.add(new ErrorDto("Tittle", ErrorType.DUPLICADO));
            }
            //Comprueba que la clasificacion de edad del juego este entre las disponibles
            if (Arrays.stream(GameAgeClasification.values()).noneMatch(c -> c.equals(game.gameAgeClasification()))) {
                errores.add(new ErrorDto("AgeClasification", ErrorType.NO_ENCONTRADO));
            }
        }
        return errores;
    }
}


