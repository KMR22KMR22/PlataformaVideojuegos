package org.example.Controller;

import org.example.Exeptions.ValidationException;
import org.example.Mapper.Mapper;
import org.example.Model.DTO.Game.GameDTO;
import org.example.Model.DTO.Library.LibraryDTO;
import org.example.Model.DTO.Purchase.PaymentMethods;
import org.example.Model.Entidad.GameEntity;
import org.example.Model.Entidad.UserEntity;
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
     * @param gameid Id del juego a comprar
     * @param userid Id del usuario que va comprar e jeugo
     * @return Lista con errores, en caso de no haber devuelve la lista vacia
     * */
    public List<GameDTO> showPersonalLibrary(Long userid, Optional<>){

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
