package org.example.Controller;

import org.example.Model.DTO.Library.LibraryDTO;
import org.example.Model.Entidad.GameEntity;
import org.example.Model.Entidad.UserEntity;
import org.example.Repository.InMemory.GameRepoInMemory;
import org.example.Repository.InMemory.LibraryRepoInMemory;
import org.example.Repository.InMemory.UserRepoInMemory;

public class LibraryController {

    private LibraryRepoInMemory repository = new LibraryRepoInMemory();
    private UserRepoInMemory userRepo = new UserRepoInMemory();
    private GameRepoInMemory gameRepo = new GameRepoInMemory();


    //public LibraryDTO createAndValidateLibrary(GameEntity game, UserEntity user) {
    //    //Compuebo que exista un usuario y un usuario
    //    if (game == null || user == null) {throw new IllegalArgumentException("Parametros no recibidos");}
//
    //    //Compruebo que el usuario exista en el repositorio
    //    userRepo.obtenerPorId(user.getId()).orElseThrow(()  -> new IllegalArgumentException("Usuario no encontrado"));
//
    //    //Compruebo que el juego exista en el repositorio
    //    gameRepo.obtenerPorId(game.getId()).orElseThrow(()  -> new IllegalArgumentException("Juego no encontrado"));
//
//
    //    return
    //}
}
