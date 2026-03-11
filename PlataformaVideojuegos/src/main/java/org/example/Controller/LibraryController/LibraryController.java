package org.example.Controller.LibraryController;

import org.example.Exeptions.ValidationException;
import org.example.Mapper.Mapper;
import org.example.Model.DTO.Game.GameDTO;
import org.example.Model.DTO.Library.LibraryDTO;
import org.example.Model.Entidad.LibraryEntity;
import org.example.Model.Form.Errors.ErrorDto;
import org.example.Model.Form.Errors.ErrorType;
import org.example.Model.Form.LibraryForm;
import org.example.Model.Form.Updates.LibraryUpdateForm;
import org.example.Repository.InMemory.GameRepoInMemory;
import org.example.Repository.InMemory.LibraryRepoInMemory;
import org.example.Repository.InMemory.UserRepoInMemory;
import org.example.Repository.Interface.IGameRepo;
import org.example.Repository.Interface.ILibraryRepo;
import org.example.Repository.Interface.IUserRepo;

import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class LibraryController {

    private ILibraryRepo libraryRepo;
    private IUserRepo userRepo;
    private IGameRepo gameRepo;


    //Constructor


    public LibraryController(ILibraryRepo libraryRepo, IUserRepo userRepo, IGameRepo gameRepo) {
        this.libraryRepo = libraryRepo;
        this.userRepo = userRepo;
        this.gameRepo = gameRepo;
    }

    /** Agregar un juego adquirido a la biblioteca del usuario
     * @param idUser Id del usuario que va comprar e jeugo
     * @param idgame id del juego
     * @return LibraryDTO
     * */
    public LibraryDTO addGameToLibrary(Long idgame, Long idUser) throws ValidationException {
        List<ErrorDto>  errors = new ArrayList<>();

        errors.addAll(validate(idgame, idUser));
        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }

        LibraryForm libraryForm = new LibraryForm(idUser, idgame, LocalDate.now());

        var libraryOpt = libraryRepo.crear(libraryForm);
        var library = libraryOpt.orElse(null);

        return Mapper.mapFrom(library);
    }


    /** Lista todos los juegos que posee un usuario en su biblioteca
     * @param idUser Id del usuario que va comprar e jeugo
     * @param order Orden en que se muestran los juegos (opcional)
     * @return Lista de bibliotecas en la biblioteca con sus datos de uso
     * */
    public List<LibraryDTO> showPersonalLibrary(Long idUser, Optional<OrderParameters> order){

        //Encuentro las bibliotecas que coincidan con el jugador
        List<LibraryDTO> libraries = libraryRepo.obtenerTodos().stream()
                .filter(l -> l.getIdUser() == idUser)
                .map(l -> Mapper.mapFrom(l))
                .toList();

        if(order.isPresent()){

            switch (order.get()) {

                case ALPHABETICAL:
                    return libraries.stream()
                            .sorted((g1, g2) -> g1.game().title().compareToIgnoreCase(g2.game().title()))
                            .toList();

                case GAME_TYPE:
                    return libraries.stream()
                            .sorted((g1, g2) -> g1..compareToIgnoreCase(g2.category()))
                            .toList();

                case LAST_SESIO:
                    return libraries.stream()
                            .sorted((g1, g2) -> g1.getLastPlayed().compareTo(g2.getLastPlayed()))
                            .toList();

                case ADQUISITION_DATE:
                    return libraries.stream()
                            .sorted((g1, g2) -> g1.getAcquisitionDate().compareTo(g2.getAcquisitionDate()))
                            .toList();
            }
        }


        return libraries;
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
     * @param idGame Id del juego a comprar
     * @param idUser Id del usuario que va comprar e jeugo
     * @throws ValidationException
     * @return LibraryDTO con las horas de juego actualizadas
     * */
    public LibraryDTO updateGameTime(Long idUser, Long idGame, Duration time) throws ValidationException {
        List<ErrorDto>  errors = new ArrayList<>();

        //Encuentro la biblioteca que coincidan con el id del juego y del usuario
        LibraryEntity library = libraryRepo.obtenerTodos().stream()
                .filter(l -> l.getIdUser() == idUser && l.getIdGame() == idGame)
                .findFirst().orElse(null);

        if(library == null){
            errors.add(new ErrorDto("IdLibrary", ErrorType.NO_ENCONTRADO));
        }
        if(time.isNegative() || time.isZero()){
            errors.add(new ErrorDto("TimePlaying", ErrorType.VALOR_DEMASIADO_BAJO));
        }
        if(!errors.isEmpty()){
            throw new ValidationException(errors);
        }

        Duration updatedTime = library.getTimePlaying().plus(time);

        LibraryUpdateForm libraryForm = new LibraryUpdateForm(library.getId(), library.getIdUser(), library.getIdGame(), library.getAdquisitionDate(), updatedTime, library.getLastPlayed(), library.getInstalationState());

        LibraryEntity libraryEntity = libraryRepo.actualizar(libraryForm.id(), libraryForm).get();

        return Mapper.mapFrom(libraryEntity);
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
