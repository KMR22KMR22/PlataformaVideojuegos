package org.example.mapper;

import org.example.model.dto.game.GameDTO;
import org.example.model.dto.library.LibraryDTO;
import org.example.model.dto.purchase.PurchaseDTO;
import org.example.model.dto.review.ReviewDTO;
import org.example.model.dto.user.UserDTO;
import org.example.model.entidad.*;
import org.example.repository.inMemory.GameRepoInMemory;
import org.example.repository.inMemory.UserRepoInMemory;

public class Mapper {

    private static UserRepoInMemory userRepo = new UserRepoInMemory();
    private static GameRepoInMemory gameRepo = new GameRepoInMemory();

    //User
    public static UserDTO mapFrom(UserEntity entity) {
        if (entity == null)
            return null;

        return new UserDTO(
                entity.getId(),
                entity.getUserName(),
                entity.getEmail(),
                entity.getRealName(),
                entity.getCountry(),
                entity.getBirthDate(),
                entity.getRegistrationDate(),
                entity.getAvatar(),
                entity.getPortfolioBalance(),
                entity.getAccountState());
    }

    //Game
    public static GameDTO mapFrom(GameEntity entity) {
        if (entity == null)
            return null;

        return new GameDTO(
                entity.getId(),
                entity.getTittle(),
                entity.getDescription(),
                entity.getDeveloper(),
                entity.getLaunchDate(),
                entity.getBasePrice(),
                entity.getCurrentDescount(),
                entity.getCategory(),
                entity.getAgeClasification(),
                entity.getAvailabeLanguages(),
                entity.getState());
    }

    //Purchase
    public static PurchaseDTO mapFrom(PurchaseEntity entity) {
        if (entity == null)
            return null;

        UserDTO userDTO = findUser(entity.getIdUser());
        GameDTO gameDTO = finGame(entity.getIdGame());
        return new PurchaseDTO(
                entity.getId(),
                entity.getIdUser(),
                userDTO,
                entity.getIdGame(),
                gameDTO,
                entity.getPurchaseDate(),
                entity.getPaymentMethod(),
                entity.getPriceWithoutDiscount(),
                entity.getDiscountApplicated(),
                entity.getSatate());
    }

    //Library
    public static LibraryDTO mapFrom(LibraryEntity entity) {
        if (entity == null)
            return null;

        UserDTO userDTO = findUser(entity.getIdUser());
        GameDTO gameDTO = finGame(entity.getIdGame());
        return new LibraryDTO(
                entity.getId(),
                entity.getIdUser(),
                userDTO,
                entity.getIdGame(),
                gameDTO,
                entity.getAcquisitionDate(),
                entity.getTimePlaying(),
                entity.getLastPlayed(),
                entity.getInstalationState());
    }

    //Review
    public static ReviewDTO mapFrom(ReviewEntity entity) {
        if (entity == null)
            return null;

        UserDTO userDTO = findUser(entity.getIdUser());
        GameDTO gameDTO = finGame(entity.getIdGame());
        return new ReviewDTO(
                entity.getId(),
                entity.getIdUser(),
                userDTO,
                entity.getIdGame(),
                gameDTO,
                entity.isRecommended(),
                entity.getReviwText(),
                entity.getHoursPlayed(),
                entity.getPublicationDate(),
                entity.getLastEditionDate(),
                entity.getState());
    }

    public static UserDTO findUser(Long id) {
        return mapFrom(userRepo.getById(id).get());
    }

    public static GameDTO finGame(Long id) {
        return mapFrom(gameRepo.getById(id).get());
    }


}
