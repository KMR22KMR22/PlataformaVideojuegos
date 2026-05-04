package org.example;

import org.example.controller.PurchaseController;
import org.example.controller.UserController;
import org.example.controller.gameController.GameController;
import org.example.controller.libraryController.LibraryController;
import org.example.controller.reviewController.ReviewController;
import org.example.exeptions.ValidationException;
import org.example.model.dto.game.GameAgeClasification;
import org.example.model.entidad.GameEntity;
import org.example.model.entidad.ReviewEntity;
import org.example.model.form.GameForm;
import org.example.model.form.UserForm;
import org.example.model.paymentMethod.PaymentMethod;
import org.example.repository.inMemory.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) throws ValidationException {
        UserRepoInMemory userRepo = new UserRepoInMemory();
        GameRepoInMemory gameRepo = new GameRepoInMemory();
        LibraryRepoInMemory libraryRepo = new LibraryRepoInMemory();
        PurchaseRepoMemory purRepo = new PurchaseRepoMemory();
        ReviewRepoInMemory reviewRepo = new ReviewRepoInMemory();
        CountryRepoInMemory countryRepo = new CountryRepoInMemory();

        UserForm userForm = new UserForm("Pepito", "Pepito@gmail.com", "Pcoco._e6", "Pepe", "Espanha", LocalDate.of(2003, 9, 22), " ");
        GameForm gameForm = new GameForm("Resident Evil", "Juego de zombies", "CapCom", LocalDate.now(), 60, "Terror", GameAgeClasification.PEGI_18, List.of("Español", "Ingles"));

        //UserController userController = new UserController(userRepo, countryRepo);
        //GameController gameController = new GameController(gameRepo);
        //PurchaseController purchaseController = new PurchaseController(purRepo, gameRepo, userRepo, libraryRepo);
        //LibraryController libraryController = new LibraryController(libraryRepo, userRepo, gameRepo);
        //ReviewController reviewController = new ReviewController(reviewRepo, userRepo, gameRepo, libraryRepo);

        //Creo un pais
        //countryRepo.create("Espanha");
        //Creo un usuario
        //userController.registerNewUser(userForm);
        //Creo un juego
        //gameController.addNewGame(gameForm);
        //Creo una compra
        //purchaseController.makePurchase(1L, 1L, PaymentMethod.TRASFERENCE);
        //creo una biblioteca
        //libraryController.addGameToLibrary(1L, 1L);
        //Creo una reseña
        //reviewController.writeReview(1L, 1L, true, "Bueniiiisimo rebueniiiisimo, spectacular,aslvhasfbd");

        //Compruebo que se haya creado la compra
        List<GameEntity> gameList = new ArrayList<>();
        gameList.addAll(gameRepo.getAll());
        if(gameList.isEmpty()){
            System.out.println("No funciona");
        }else {
            System.out.println("Funciona");
        }
    }
}