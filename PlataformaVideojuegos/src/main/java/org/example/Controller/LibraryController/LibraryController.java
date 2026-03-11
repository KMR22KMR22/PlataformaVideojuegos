package org.example.Controller.LibraryController;

import org.example.Exeptions.ValidationException;
import org.example.Mapper.Mapper;
import org.example.Model.DTO.Game.GameDTO;
import org.example.Model.DTO.Library.LibraryDTO;
import org.example.Model.Entidad.LibraryEntity;
import org.example.Model.Form.Errors.ErrorDto;
import org.example.Model.Form.Errors.ErrorType;
import org.example.Model.Form.LibraryForm;
import org.example.Repository.InMemory.GameRepoInMemory;
import org.example.Repository.InMemory.LibraryRepoInMemory;
import org.example.Repository.InMemory.UserRepoInMemory;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class LibraryController {

    private LibraryRepoInMemory libraryRepo = new LibraryRepoInMemory();
    private UserRepoInMemory userRepo = new UserRepoInMemory();
    private GameRepoInMemory gameRepo = new GameRepoInMemory();

    public LibraryDTO addGameToLibrary(Long gameid, Long userid) throws ValidationException {
        List<ErrorDto>  errors = new ArrayList<>();

        errors.addAll(validate(gameid, userid));
        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }

        LibraryForm libraryForm = new LibraryForm(userid, gameid, LocalDate.now());

        var libraryOpt = libraryRepo.crear(libraryForm);
        var library = libraryOpt.orElse(null);

        return Mapper.mapFrom(library);
    }


    /** Lista todos los juegos que posee un usuario en su biblioteca
     * @param userid Id del usuario que va comprar e jeugo
     * @param order Orden en que se muestran los juegos (opcional)
     * @return Lista de juegos en la biblioteca con sus datos de uso
     * */
    public List<GameDTO> showPersonalLibrary(Long userid, Optional<OrderParameters> order){

        //Encuentro las bibliotecas que coincidan con el jugador y saco los ids de los juegos que tiene
        List<LibraryEntity> libraris = libraryRepo.obtenerTodos().stream()
                .filter(l -> l.getIdUser() == userid)
                .toList();

        //Convierto las bibliotecas en sus ids
        List<Long> gamesId = libraris.stream()
                .map(l -> l.getIdGame())
                .toList();

        //Filtro todos los juegos que tengan esos ids
        List<GameDTO> games = gameRepo.obtenerTodos().stream()
                .filter(g -> gamesId.contains(g.getId()))
                .map(g -> Mapper.mapFrom(g))
                .toList();

        if(order.isPresent()){

            switch (order.get()) {

                case ALPHABETICAL:
                    return games.stream()
                            .sorted((g1, g2) -> g1.title().compareToIgnoreCase(g2.title()))
                            .toList();

                case GAME_TYPE:
                    return games.stream()
                            .sorted((g1, g2) -> g1.category().compareToIgnoreCase(g2.category()))
                            .toList();

                case LAST_SESIO:
                    return libraris.stream()
                            .sorted((g1, g2) -> g1.getLastPlayed().compareTo(g2.getLastPlayed()))
                            .toList();

                case ADQUISITION_DATE:
                    return games.stream()
                            .sorted((g1, g2) -> g1.getAcquisitionDate().compareTo(g2.getAcquisitionDate()))
                            .toList();
            }
        }


        return games;
    }


    /** Quita un juego de la biblioteca del usuario
     * @param idGame Id del juego a comprar
     * @param idUser Id del usuario que va comprar e jeugo
     * @throws ValidationException
     * */
    public void deleteLibrary(Long idUser, Long idGame) throws ValidationException {
        var errors = new ArrayList<ErrorDto>();

        Optional<LibraryEntity> library = libraryRepo.obtenerTodos().stream()
                        .filter(l -> l.getIdUser() == idUser && l.getIdGame() == idGame)
                                .findFirst();

        if (!library.isPresent()) {
            errors.add(new ErrorDto("IdLibrary", ErrorType.NO_ENCONTRADO));
            throw new ValidationException(errors);
        }
        libraryRepo.eliminar(library.get().getId());
    }


    /**Realiza las validaciones de la Biblioteca que necesitan acceso a datos
     * @param gameid Id del juego a comprar
     * @param userid Id del usuario que va comprar e jeugo
     * @return Lista con errores, en caso de no haber devuelve la lista vacia
     * */
    public List<ErrorDto> validate(Long gameid, Long userid) {
        List<ErrorDto>  errors = new ArrayList<>();

        //Compruebo que el usuario exista en el repositorio
        if(userRepo.obtenerPorId(userid).isEmpty()){
            errors.add(new ErrorDto("IdUser", ErrorType.NO_ENCONTRADO));
        }

        //Compruebo que el juego exista en el repositorio
        if(gameRepo.obtenerPorId(gameid).isEmpty()){
            errors.add(new ErrorDto("Idgame", ErrorType.NO_ENCONTRADO));
        }

        if(libraryRepo.obtenerTodos().stream().anyMatch(g -> g.getIdGame().equals(gameid))
                && libraryRepo.obtenerTodos().stream().anyMatch(g -> g.getIdUser().equals(userid))){
            errors.add(new ErrorDto("IdGame, IdUser", ErrorType.DUPLICADO));
        }

        return errors;
    }
}
