package org.example.Controller.LibraryController;

import org.example.Exeptions.ValidationException;
import org.example.Mapper.Mapper;
import org.example.Model.DTO.Library.InstalationState;
import org.example.Model.DTO.Library.LibraryDTO;
import org.example.Model.Entidad.LibraryEntity;
import org.example.Model.Form.Errors.ErrorDto;
import org.example.Model.Form.Errors.ErrorType;
import org.example.Model.Form.LibraryForm;
import org.example.Model.Form.Updates.LibraryUpdate;
import org.example.Repository.Interface.IGameRepo;
import org.example.Repository.Interface.ILibraryRepo;
import org.example.Repository.Interface.IUserRepo;

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



    /** Lista todos los juegos que posee un usuario en su biblioteca
     * @param idUser Id del usuario que va a comprar el juego
     * @param order Orden en que se muestran los juegos (opcional)
     * @return Lista de bibliotecas con los juegos que posee el usuario
     * */
    public List<LibraryDTO> showPersonalLibrary(Long idUser, Optional<OrderParameters> order){

        //Compruebo que el usuario exista
        userRepo.getById(idUser).orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        //Encuentro las bibliotecas que coincidan con el jugador y las mapeo a DTOs
        List<LibraryDTO> libraries = libraryRepo.getAll().stream()
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
                            .sorted((g1, g2) -> g1.game().category().compareToIgnoreCase(g2.game().category()))
                            .toList();

                case LAST_SESIO:
                    return libraries.stream()
                            .sorted((g1, g2) -> g1.lastPlayed().compareTo(g2.lastPlayed()))
                            .toList();

                case ADQUISITION_DATE:
                    return libraries.stream()
                            .sorted((g1, g2) -> g1.adquisitionDate().compareTo(g2.adquisitionDate()))
                            .toList();
            }
        }
        return libraries;
    }



    /** Agregar un juego adquirido a la biblioteca del usuario
     * @param idUser Id del usuario que va comprar e jeugo
     * @param idgame id del juego
     * @return LibraryDTO con los datos de la libreria que se creó
     * */
    public LibraryDTO addGameToLibrary(Long idgame, Long idUser) throws ValidationException {
        List<ErrorDto>  errors = new ArrayList<>();

        errors.addAll(validate(idgame, idUser));
        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }

        LibraryForm libraryForm = new LibraryForm(idUser, idgame, LocalDate.now());

        var libraryOpt = libraryRepo.create(libraryForm);
        var library = libraryOpt.orElse(null);

        return Mapper.mapFrom(library);
    }



    /** Quita un juego de la biblioteca del usuario
     * @param idGame Id del juego a comprar
     * @param idUser Id del usuario que va comprar e jeugo
     * @throws ValidationException
     * */
    public void deleteLibrary(Long idUser, Long idGame) throws ValidationException {
        var errors = new ArrayList<ErrorDto>();

        Optional<LibraryEntity> library = libraryRepo.getAll().stream()
                        .filter(l -> l.getIdUser() == idUser && l.getIdGame() == idGame)
                                .findFirst();

        if (!library.isPresent()) {
            errors.add(new ErrorDto("IdLibrary", ErrorType.NO_ENCONTRADO));
            throw new ValidationException(errors);
        }
        libraryRepo.delete(library.get().getId());
    }


    /** Registra y actualiza las horas jugadas de un juego
     * @param idGame Id del juego a comprar
     * @param idUser Id del usuario que va comprar e jeugo
     * @param time Tiempo nuevo jugado
     * @throws ValidationException
     * @return LibraryDTO con las horas de juego actualizadas
     * */
    public LibraryDTO updateGameTime(Long idUser, Long idGame, Long time) throws ValidationException {
        List<ErrorDto>  errors = new ArrayList<>();

        //Encuentro la biblioteca que coincida con el id del juego y del usuario
        LibraryEntity library = libraryRepo.getByUserGameId(idUser, idGame).orElse(null);

        if(library == null){
            errors.add(new ErrorDto("IdLibrary", ErrorType.NO_ENCONTRADO));
        }
        if(time <= 0){
            errors.add(new ErrorDto("TimePlaying", ErrorType.VALOR_DEMASIADO_BAJO));
        }
        if(!errors.isEmpty()){
            throw new ValidationException(errors);
        }

        Long updatedTime = library.getTimePlaying() + time;

        LibraryUpdate libraryForm = new LibraryUpdate(library.getId(), library.getIdUser(), library.getIdGame(), library.getAdquisitionDate(), updatedTime, library.getLastPlayed(), library.getInstalationState());

        LibraryEntity libraryEntity = libraryRepo.update(libraryForm.id(), libraryForm).get();

        return Mapper.mapFrom(libraryEntity);
    }


    /**Ver la última vez que se jugó a un juego específico
     * @param idGame Id del juego a comprar
     * @param idUser Id del usuario que va comprar e jeugo
     * @return LibraryDTO
     * */
    public LibraryDTO consultLastSession(Long idUser, Long idGame){
        LibraryEntity library = libraryRepo.getByUserGameId(idUser, idGame).orElse(null);
        if(library == null){
            throw new IllegalStateException("Libreria no encontrada");
        }

        return  Mapper.mapFrom(library);
    }



    /**Buscar juegos en la biblioteca personal según criterios
     * @param idUser Id del juego a comprar
     * @param text Texto por el que se va a filtrar (Optional)
     * @param instalationState estado de instalacion por el que se va a filtrar (Optional)
     * @return Lista con las bibliotecas filtradas
     * */
    public List<LibraryDTO> filterLibrary(Long idUser, Optional<String> text, Optional<InstalationState> instalationState) {
        //Compruebo que el usuario exista
        userRepo.getById(idUser).orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        //Encuentro las bibliotecas que coincidan con el jugador y las mapeo a DTOs
        List<LibraryDTO> libraries = libraryRepo.getAll().stream()
                .filter(l -> l.getIdUser() == idUser)
                .map(l -> Mapper.mapFrom(l))
                .toList();

        if(text.isPresent() || instalationState.isPresent()){
            if(text.isPresent()){
                libraries = libraries.stream()
                        .filter(l -> l.game().title().equals(text))
                        .toList();
            }
            if(instalationState.isPresent()){
                libraries = libraries.stream()
                        .filter(l -> l.instalationState().equals(instalationState))
                        .toList();
            }
        }
        return libraries;
    }



    /**Realiza las validaciones de la Biblioteca que necesitan acceso a datos
     * @param gameid Id del juego a comprar
     * @param userid Id del usuario que va comprar e jeugo
     * @return Lista con errores, en caso de no haber devuelve la lista vacia
     * */
    public List<ErrorDto> validate(Long gameid, Long userid) {
        List<ErrorDto>  errors = new ArrayList<>();

        //Compruebo que el usuario exista en el repositorio
        if(userRepo.getById(userid).isEmpty()){
            errors.add(new ErrorDto("IdUser", ErrorType.NO_ENCONTRADO));
        }

        //Compruebo que el juego exista en el repositorio
        if(gameRepo.getById(gameid).isEmpty()){
            errors.add(new ErrorDto("Idgame", ErrorType.NO_ENCONTRADO));
        }

        if(libraryRepo.getAll().stream().anyMatch(g -> g.getIdGame().equals(gameid))
                && libraryRepo.getAll().stream().anyMatch(g -> g.getIdUser().equals(userid))){
            errors.add(new ErrorDto("IdGame, IdUser", ErrorType.DUPLICADO));
        }

        return errors;
    }
}
