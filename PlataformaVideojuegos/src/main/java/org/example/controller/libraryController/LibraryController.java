package org.example.controller.libraryController;

import org.example.controller.Util;
import org.example.exeptions.ValidationException;
import org.example.mapper.Mapper;
import org.example.model.dto.library.InstalationState;
import org.example.model.dto.library.LibraryDTO;
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
     * @param idUser Id del usuario que va a comprar el juego
     * @param order  Orden en que se muestran los juegos (opcional)
     * @return Lista de bibliotecas con los juegos que posee el usuario
     *
     */
    public List<LibraryDTO> showPersonalLibrary(Long idUser, Optional<OrderParameters> order) throws ValidationException {
        
        List<LibraryDTO> libraries = showLibraryStats(idUser);

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

                case LAST_SESIO:
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
     * @param idUser Id del usuario que va comprar e jeugo
     * @param idgame id del juego
     * @return LibraryDTO con los datos de la libreria que se creó
     *
     */
    public LibraryDTO addGameToLibrary(Long idgame, Long idUser) throws ValidationException {
        List<ErrorDto> errors = new ArrayList<>();

        //Inicio Transaccion
        var library = tm.inTransaction(()-> {
            //Valido
            errors.addAll(validate(idgame, idUser));

            if (!errors.isEmpty()) {
                throw new IllegalArgumentException();
            }

            //Creo el formulario de la biblioteca
            LibraryForm libraryForm = new LibraryForm(idUser, idgame, LocalDate.now());

            return libraryRepo.create(libraryForm).orElse(null);
        });

        //Vuelvo a revisar si hay errores para lanzar exepcion
        Util.thowException(errors);
        
        return Mapper.mapFrom(library);
    }


    /**
     * Quita un juego de la biblioteca del usuario
     *
     * @param idGame Id del juego a comprar
     * @param idUser Id del usuario que va comprar e jeugo
     * @throws ValidationException
     *
     */
    public void deleteLibrary(Long idUser, Long idGame) throws ValidationException {
        var errors = new ArrayList<ErrorDto>();

        //Inicio Transaccion
        tm.inTransaction(()->{

            LibraryEntity library = libraryRepo.getAll().stream()
                    .filter(l -> Objects.equals(l.getIdUser(), idUser) && Objects.equals(l.getIdGame(), idGame))
                    .findFirst()
                    .orElse(null);

            if (library == null) {
                errors.add(new ErrorDto("IdLibrary", ErrorType.NO_ENCONTRADO));
            }
            
            //Mando exepcion para que se detenga la ejecucion de la lambda
            if (!errors.isEmpty()) {
                throw new IllegalArgumentException();
            }

            boolean deleted = libraryRepo.delete(library.getId());
            
            //Compruebo que se haya eliminado la biblioteca
            if (!deleted){
                errors.add(new ErrorDto("Library", ErrorType.NO_ELIMINADO));
            }
            
            //Vuelvo a comprobar si hay errores, ya que si llega a este punto es que no hubo error al encontrar la biblioteca, pero si hubo error al borrarla
            if (!errors.isEmpty()) {
                throw new IllegalArgumentException();
            }
            
            return true;
            //Aquí devuelvo true solo porque la lambda me obliga, pero no necesito devolver nada, ya que lo estoy controlando todo con las exepciones
        });

        //Vuelvo a revisar si hay errores para lanzar exepcion
        Util.thowException(errors);
    }


    /**
     * Registra y actualiza las horas jugadas de un juego
     *
     * @param idGame Id del juego a comprar
     * @param idUser Id del usuario que va comprar e jeugo
     * @param time   Tiempo nuevo jugado
     * @return LibraryDTO con las horas de juego actualizadas
     * @throws ValidationException
     *
     */
    public LibraryDTO updateGameTime(Long idUser, Long idGame, Long time) throws ValidationException {
        List<ErrorDto> errors = new ArrayList<>();

        //Inicio Transaccion
        LibraryEntity libraryEntity = tm.inTransaction(()->{
            //Encuentro la biblioteca que coincida con el id del juego y del usuario
            LibraryEntity library = libraryRepo.getByUserGameId(idUser, idGame).orElse(null);
            //Compruebo que el tiempo que entra no sea menor que cero
            if (time <= 0) {
                errors.add(new ErrorDto("TimePlaying", ErrorType.VALOR_DEMASIADO_BAJO));
            }
            //Compruebo que se haya encontrado la biblioteca
            if (library == null) {
                errors.add(new ErrorDto("IdLibrary", ErrorType.NO_ENCONTRADO));
            }
            
            if (!errors.isEmpty()) {
                throw new IllegalArgumentException();
            }

            Long updatedTime = library.getTimePlaying() + time;

            LibraryUpdate libraryForm = new LibraryUpdate(library.getId(), library.getIdUser(), library.getIdGame(), library.getAcquisitionDate(), updatedTime, library.getLastPlayed(), library.getInstalationState());

            return libraryRepo.update(libraryForm.id(), libraryForm).orElse(null);
            
        });
        
        //Vuelvo a revisar si hay errores para lanzar exepcion
        Util.thowException(errors);

        return Mapper.mapFrom(libraryEntity);
    }


    /**
     * Ver la última vez que se jugó a un juego específico
     *
     * @param idGame Id del juego a comprar
     * @param idUser Id del usuario que va comprar e jeugo
     * @return LibraryDTO
     *
     */
    public LibraryDTO consultLastSession(Long idUser, Long idGame) throws ValidationException {
        List<ErrorDto> errors = new ArrayList<>();

        //Inicio Transaccion
        LibraryEntity foundLibrary = tm.inTransaction(()->{
            //busco la biblioteca
            return libraryRepo.getByUserGameId(idUser, idGame).orElse(null);
        });
        
        //Compruebo que la biblioteca exista
        if (foundLibrary == null) {
            errors.add(new ErrorDto("LibraryIDUser and LibraryIDGame", ErrorType.NO_ENCONTRADO));
        }

        //Compruebo errores para lanzar exepcion
        Util.thowException(errors);

        return Mapper.mapFrom(foundLibrary);
    }


    /**
     * Buscar juegos en la biblioteca personal según criterios
     *
     * @param idUser           Id del juego a comprar
     * @param text             Texto por el que se va a filtrar (Optional)
     * @param instalationState estado de instalacion por el que se va a filtrar (Optional)
     * @return Lista con las bibliotecas filtradas
     *
     */
    public List<LibraryDTO> filterLibrary(Long idUser, Optional<String> text, Optional<InstalationState> instalationState) throws ValidationException {
        List<ErrorDto> errors = new ArrayList<>();

        //Inicio Transaccion
        List<LibraryDTO> librariesFound = tm.inTransaction(()-> {
            //Compruebo que el usuario exista
            UserEntity user = userRepo.getById(idUser).orElse(null);
            if (user == null) {
                errors.add(new ErrorDto("UserID", ErrorType.NO_ENCONTRADO));
            }

            //Encuentro las bibliotecas que coincidan con el jugador y las mapeo a DTOs
            List<LibraryDTO> libraries = libraryRepo.getAll().stream()
                    .filter(l -> Objects.equals(l.getIdUser(), idUser))
                    .map(l -> Mapper.mapFrom(l))
                    .toList();
            //Compruebo que se haya encontrado al menos una biblioteca
            if (libraries.isEmpty()) {
                errors.add(new ErrorDto("LibraryIDUser", ErrorType.NO_ENCONTRADO));
            }
            //Compruebo si hay algun error
            if (!errors.isEmpty()) {
                throw new IllegalArgumentException();
            }

            //Si se ingreso el text o el instalationState filtro por esos parametros
            if (text.isPresent() || instalationState.isPresent()) {
                if (text.isPresent()) {
                    libraries = libraries.stream()
                            .filter(l -> l.game().title().equals(text))
                            .toList();
                }
                if (instalationState.isPresent()) {
                    libraries = libraries.stream()
                            .filter(l -> l.instalationState().equals(instalationState))
                            .toList();
                }
            }
            return libraries;
        });
        
        Util.thowException(errors);
        
        return librariesFound;
    }

    /**
     * Muestra métricas generales de la biblioteca del usuario
     *
     * @param idUser Id del usuario que va comprar e jeugo
     * @return Lista con todas las bibliotecas que coincidan con el usuario
     *
     */
    public List<LibraryDTO> showLibraryStats(Long idUser) throws ValidationException {
        List<ErrorDto> errors = new ArrayList<>();

        //Inicio Transaccion
        List<LibraryDTO> libraries = tm.inTransaction(()-> {
            //Compruebo que el usuario exista
            UserEntity user = userRepo.getById(idUser).orElse(null);
            if (user == null) {
                errors.add(new ErrorDto("UserID", ErrorType.NO_ENCONTRADO));
            }
            
            if (!errors.isEmpty()) {
                throw new IllegalArgumentException();
            }

            //Encuentro las bibliotecas que coincidan con el jugador y las mapeo a DTOs
            return libraryRepo.getAll().stream()
                    .filter(l -> Objects.equals(l.getIdUser(), idUser))
                    .map(l -> Mapper.mapFrom(l))
                    .toList();
        });
        
        //Vuelvo a revisar si hay errores para lanzar exepcion
        Util.thowException(errors);

        return libraries;
    }


    /**
     * Realiza las validaciones de la Biblioteca que necesitan acceso a datos
     *
     * @param idGame Id del juego a comprar
     * @param idUser Id del usuario que va comprar e jeugo
     * @return Lista con errores, en caso de no haber devuelve la lista vacia
     *
     */
    public List<ErrorDto> validate(Long idGame, Long idUser) {
        List<ErrorDto> errors = new ArrayList<>();

        //Compruebo que el usuario exista en el repositorio
        if (userRepo.getById(idUser).isEmpty()) {
            errors.add(new ErrorDto("IdUser", ErrorType.NO_ENCONTRADO));
        }

        //Compruebo que el juego exista en el repositorio
        if (gameRepo.getById(idGame).isEmpty()) {
            errors.add(new ErrorDto("Idgame", ErrorType.NO_ENCONTRADO));
        }

        if (libraryRepo.getAll().stream().anyMatch(g -> g.getIdGame().equals(idGame))
                && libraryRepo.getAll().stream().anyMatch(g -> g.getIdUser().equals(idUser))) {
            errors.add(new ErrorDto("IdGame, IdUser", ErrorType.DUPLICADO));
        }
        return errors;
    }
}
