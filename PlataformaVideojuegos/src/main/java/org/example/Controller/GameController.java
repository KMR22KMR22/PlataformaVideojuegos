package org.example.Controller;

import org.example.Exeptions.GenericExeption;
import org.example.Model.DTO.Game.GameAgeClasification;
import org.example.Model.DTO.Game.GameState;
import org.example.Model.Entidad.GameEntity;
import org.example.Model.Errors.GenericErrors;
import org.example.Model.Form.GameForm;
import org.example.Repository.InMemory.GameRepoInMemory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class GameController {

    private GameRepoInMemory gameRepo = new GameRepoInMemory();

    /** Registra un nuevo videojuego en el catálogo de Steam
     * @param gameform Formulario con los datos introducidos por el usuario
     * @return Lista de errores encontrados, si no encuentra ninguno devolvera la lista vacia
     * */
    public List<String> addNewGame(GameForm gameform) {
        List<String> errores = new ArrayList<>();

        //LLamo al validate del formulario y guardo la lista de errores
        errores.addAll(gameform.validate());

        //LLamo al validate del controlador y guardo la lista de errores
        errores.addAll(validate(gameform));

        //Si no se detectaron errores en el formulario se llamo a la funcion de cear nueva GameEntity que esta en el repositorio
        if (errores.isEmpty()) {
            gameRepo.crear(gameform);
        }
        return errores;
    }



    /** Filtra y busca juegos en el catálogo según múltiples criterios
     * @param texto Texto libre para filtrar por título o descripción (opcional)
     * @param category Categoría del juego (opcional)
     * @param minPrice Precio minimo posible
     * @param maxPrice Precio maximo posible
     * @param ageClasification Clasificación por edad del juego (opcional)
     * @param gameState Estado del juego (opcional)
     * @return Lista de juegos encontrados, en caso de no encontrar ninguno se devuelve la lista vacia
     * */
    public List<GameEntity> findGames(Optional<String> texto, Optional<String> category, Optional<Integer> minPrice, Optional<Integer> maxPrice, Optional<GameAgeClasification> ageClasification, Optional<GameState> gameState) {
        List<GameEntity> Coincidenses = new ArrayList<>();

        //Compruebo que al menos haya un parametro de entrada que tenga valor, si todos son null devuelvo una exepcion
        if (texto.isEmpty() && category.isEmpty() && minPrice.isEmpty()
                && maxPrice.isEmpty() && ageClasification.isEmpty() && gameState.isEmpty()) {
            throw new GenericExeption(GenericErrors.NO_PARAMETERS.getMessage());
        }

        //Filtro la lista de juegos del repositorio y devuelvo una lista con los coches que coincidan con todos los parametros de busqueda a la vez
        return gameRepo.obtenerTodos().stream()
                .filter(g -> texto.isEmpty() ||
                        (!texto.get().isBlank() && g.getTittle().contains(texto.get())))
                .filter(g -> category.isEmpty() ||
                        (!category.get().isBlank() && g.getCategory().contains(category.get())))
                .filter(g -> minPrice.isEmpty() || g.getBasePrice() >= minPrice.get())
                .filter(g -> maxPrice.isEmpty() || g.getBasePrice() <= maxPrice.get())
                .filter(g -> ageClasification.isEmpty() || g.getAgeClasification().equals(ageClasification.get()))
                .filter(g -> gameState.isEmpty() || g.getState().equals(gameState.get()))
                .toList();
    }





    /** Lista todos los juegos disponibles en la plataforma
     * @param orderParameter Parametro por el que el usuario quiere que se ordenen los juegos al mostrarse (optional)
     * @return Lista con todos los juegos ordenados por titulo, precio o fecha de lanzamiento. En caso de no aclararse se muestran todos los juegos disponibles en el orden que ya esten guardados
     * */
    public List<GameEntity> consultCataloge(Optional<OrderParameters> orderParameter){

        //Guardo los juegos almacenados en el repositorio los cuales esten como disponible en una lista
        List<GameEntity> games = gameRepo.obtenerTodos().stream()
                .filter(g -> g.getState().equals(GameState.DISPONIBLE))
                .toList();

        if (orderParameter.isPresent()) {
            switch(orderParameter.get()) {
                case ALPHABETICAL:
                    return games.stream()
                            .sorted((g1, g2) -> g1.getTittle().compareToIgnoreCase(g2.getTittle()))
                            .toList();
                case PRICE:
                    return games.stream()
                            .sorted((g1, g2) -> Float.compare(g1.getBasePrice(), g2.getBasePrice()))
                            .toList();
                case DATE:
                    return games.stream()
                            .sorted((g1, g2) -> g1.getLaunchDate().compareTo(g2.getLaunchDate()))
                            .toList();
            }
        }

        return games;
    }



    /**Muestra toda la información completa de un juego específico
     * @param id id del juego a buscar
     * @return Información completa del juego o mensaje de error si no existe
     * */
    public GameEntity consultGameDetails(Long id){
        GameEntity game = gameRepo.obtenerPorId(id).orElseThrow(() -> new GenericExeption(GenericErrors.NOT_EXISTS.getMessage()));

        return
    }





    /**Realiza las validaciones del GameForm que necesitan acceso a datos
     * @param game Formulario con los datos introducidos por el usuario
     * @return Lista con errores en caso de haber
     * */
    public List<String>  validate(GameForm game) {

        List<String> errores = new ArrayList<>();

        //Comprueba que el titulo no se repita
        if (gameRepo.obtenerTodos().stream().anyMatch(g -> g.equals(game.getTittle()))) {
            errores.add(GenericErrors.ALREADY_EXISTS.getMessage());
        }
        //Comprueba que la clasificacion de edad del juego este entre las disponibles
        if(Arrays.stream(GameAgeClasification.values()).noneMatch(c -> c.equals(game.getAgeClasification()))) {
            errores.add(GenericErrors.NOT_EXISTS.getMessage());
        }
        return errores;
    }






}


