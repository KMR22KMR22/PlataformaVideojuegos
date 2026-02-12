package Controller;

import Model.Form.LibraryForm;
import Repository.InMemory.LibraryRepoInMemory;

import java.util.ArrayList;
import java.util.List;

public class LibraryController {

    private LibraryRepoInMemory repository = new LibraryRepoInMemory();
    private List<String> errores = new ArrayList<>();

    public List<String> validateLibrary(LibraryForm library) {
        errores.clear();

        //Usuario
        validateUser(library);

        //Juego
        validateGame(library);

        //Fecha de adquisicion
        validatePurchaseDate(library);

        //Tiempo de juego total
        validateTimePlaying(library);

        //Ultima fecha de juego
        validateLastPlayedDate(library);

        //Estado de instalacion
        validateInstalacionState(library);

        return errores;

    }
}
