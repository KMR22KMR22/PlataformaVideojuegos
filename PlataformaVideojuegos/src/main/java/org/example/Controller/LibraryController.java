package org.example.Controller;

import org.example.Exeptions.GenericExeption;
import org.example.Model.Entidad.GameEntity;
import org.example.Model.Entidad.LibraryEntity;
import org.example.Model.Entidad.UserEntity;
import org.example.Model.Errors.GenericErrors;
import org.example.Repository.InMemory.GameRepoInMemory;
import org.example.Repository.InMemory.LibraryRepoInMemory;
import org.example.Repository.InMemory.UserRepoInMemory;

import java.util.ArrayList;
import java.util.List;

public class LibraryController {

    private LibraryRepoInMemory repository = new LibraryRepoInMemory();
    private UserRepoInMemory userRepo = new UserRepoInMemory();
    private GameRepoInMemory gameRepo = new GameRepoInMemory();


    //En el validador de la biblioteca no voy a devolver una lista de errores, sino exepciones, ya que cualquier error aqui no dependera del usuario, sino de las reglas internas del sistema
    public void createAndValidateLibrary(GameEntity game, UserEntity user) {
        //Compuebo que exista un usuario y un usuario
        if (game == null || user == null) {throw new GenericExeption(GenericErrors.NOT_VALUE.getMessage());}

        //Compruebo que el usuario exista en el repositorio
        userRepo.obtenerPorId(user.getId()).orElseThrow(()  -> new GenericExeption(GenericErrors.NOT_FOUND.getMessage()));

        //Compruebo que el juego exista en el repositorio
        gameRepo.obtenerPorId(game.getId()).orElseThrow(()  -> new GenericExeption(GenericErrors.NOT_FOUND.getMessage()));



    }
}
