package org.example.controller.gameController;

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
import org.example.repository.Interface.IReviewRepo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class GameController {

    public static final int MIN_DISCOUNT = 0;
    public static final int MAX_DISCOUNT = 100;
    private IGameRepo gameRepo;
    private IReviewRepo reviewRepo;


    //Constructor

    public GameController(IGameRepo gameRepo, IReviewRepo reviewRepo) {
        this.gameRepo = gameRepo;
        this.reviewRepo = reviewRepo;
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
        List<ErrorDto> errores = new ArrayList<>();

        //LLamo al validate del formulario y guardo la lista de errores
        errores.addAll(gameForm.validate());

        //LLamo al validate del controlador y guardo la lista de errores
        errores.addAll(validate(gameForm));

        if (!errores.isEmpty()) {
            throw new ValidationException(errores);
        }

        var gameOpt = gameRepo.create(gameForm);
        var game = gameOpt.orElse(null);

        return Mapper.mapFrom(game);
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
    public List<GameDTO> findGames(Optional<String> texto, Optional<String> category, Optional<Integer> minPrice, Optional<Integer> maxPrice, Optional<GameAgeClasification> ageClasification, Optional<GameState> gameState) {

        //Compruebo que al menos haya un parametro de entrada que tenga valor, si todos son null devuelvo una exepcion
        if (texto.isEmpty() && category.isEmpty() && minPrice.isEmpty()
                && maxPrice.isEmpty() && ageClasification.isEmpty() && gameState.isEmpty()) {
            throw new IllegalArgumentException("No se han ingresado parametros de busqueda");
        }

        //Filtro la lista de juegos del repositorio, la mapeo a DTO y devuelvo una lista con los juegos que coincidan con todos los parametros de busqueda a la vez
        return gameRepo.getAll().stream()
                .filter(g -> texto.isEmpty() ||
                        (!texto.get().isBlank() && g.getTittle().contains(texto.get())))
                .filter(g -> category.isEmpty() ||
                        (!category.get().isBlank() && g.getCategory().contains(category.get())))
                .filter(g -> minPrice.isEmpty() || g.getBasePrice() >= minPrice.get())
                .filter(g -> maxPrice.isEmpty() || g.getBasePrice() <= maxPrice.get())
                .filter(g -> ageClasification.isEmpty() || g.getAgeClasification().equals(ageClasification.get()))
                .filter(g -> gameState.isEmpty() || g.getState().equals(gameState.get()))
                .map(g -> Mapper.mapFrom(g))
                .toList();
    }


    /**
     * Lista todos los juegos disponibles en la plataforma
     *
     * @param orderParameter Parametro por el que el usuario quiere que se ordenen los juegos al mostrarse (optional)
     * @return Lista con todos los juegos ordenados por título, precio o fecha de lanzamiento. En caso de no aclararse se muestran todos los juegos disponibles en el orden que ya esten guardados
     *
     */
    public List<GameDTO> consultHoleCataloge(Optional<OrderParameters> orderParameter) {

        //Guardo los juegos almacenados en el repositorio los cuales esten como disponible en una lista
        List<GameDTO> games = gameRepo.getAll().stream()
                .filter(g -> g.getState() != GameState.NO_DISPONIBLE)
                .map(g -> Mapper.mapFrom(g))
                .toList();

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
    public GameDTO consultGameDetails(Long id) {
        GameEntity game = gameRepo.getById(id).orElseThrow(() -> new IllegalArgumentException("Juego no encontrado"));

        return Mapper.mapFrom(game);
    }


    /**
     * Establece un porcentaje de descuento temporal a un juego
     *
     * @param id      id del juego a buscar
     * @param percent porciento a descontar del precio del juego
     * @return GameDTO con el descuento aplicado
     * @throws IllegalArgumentException
     *
     */
    public GameDTO applayDiscount(Long id, Integer percent) throws IllegalArgumentException {
        //Copruebo que el porciento que se quiere aplicar esté en un rango correcto
        if (percent < MIN_DISCOUNT) {
            throw new IllegalArgumentException("Porciento demasiado bajo");
        }
        if (percent > MAX_DISCOUNT) {
            throw new IllegalArgumentException("Porciento demasiado alto");
        }

        GameEntity entity = gameRepo.getById(id).get();

        var priceWithDiscount = entity.getBasePrice() * (1 - percent / 100f);

        GameUpdate form = new GameUpdate(entity.getId(), entity.getTittle(), entity.getDescription(), entity.getDeveloper(), entity.getLaunchDate(), entity.getBasePrice(), priceWithDiscount, entity.getCategory(), entity.getAgeClasification(), entity.getAvailabeLanguages(), entity.getState());

        GameEntity updatedGame = gameRepo.update(id, form).get();

        return Mapper.mapFrom(updatedGame);
    }


    /**
     * Modifica el estado de disponibilidad de un juego
     *
     * @param id       id del juego a buscar
     * @param newState nuevo estado al que se va a cambiar el juego (opcional)
     * @return Confirmación del cambio de estado o mensaje de error
     * @throws IllegalArgumentException
     *
     */
    public GameDTO changeGameState(Long id, GameState newState) throws IllegalArgumentException {

        //Compruebo que el nuevo estado esté entre los admisibles
        if (Arrays.stream(GameState.values())
                .noneMatch(gameState -> gameState
                        .equals(newState))) {
            throw new IllegalArgumentException("Estado invalido");
        }

        GameEntity entity = gameRepo.getById(id).get();

        GameUpdate form = new GameUpdate(entity.getId(), entity.getTittle(), entity.getDescription(), entity.getDeveloper(), entity.getLaunchDate(), entity.getBasePrice(), entity.getCurrentDescount(), entity.getCategory(), entity.getAgeClasification(), entity.getAvailabeLanguages(), entity.getState());

        GameEntity updatedGame = gameRepo.update(id, form).get();

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

        //Comprueba que el titulo no se repita
        if (gameRepo.getAll().stream().anyMatch(g -> g.equals(game.tittle()))) {
            errores.add(new ErrorDto("Tittle", ErrorType.DUPLICADO));
        }
        //Comprueba que la clasificacion de edad del juego este entre las disponibles
        if (Arrays.stream(GameAgeClasification.values()).noneMatch(c -> c.equals(game.gameAgeClasification()))) {
            errores.add(new ErrorDto("AgeClasification", ErrorType.NO_ENCONTRADO));
        }

        return errores;
    }


}


