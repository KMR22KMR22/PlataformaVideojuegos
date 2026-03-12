package org.example.Mapper;

import org.example.Model.DTO.Game.GameDTO;
import org.example.Model.DTO.Library.LibraryDTO;
import org.example.Model.DTO.Purchase.PurchaseDTO;
import org.example.Model.DTO.User.UserDTO;
import org.example.Model.Entidad.GameEntity;
import org.example.Model.Entidad.LibraryEntity;
import org.example.Model.Entidad.PurchaseEntity;
import org.example.Model.Entidad.UserEntity;
import org.example.Repository.InMemory.GameRepoInMemory;
import org.example.Repository.InMemory.UserRepoInMemory;

public class Mapper {

    private static UserRepoInMemory userRepo = new UserRepoInMemory();
    private static GameRepoInMemory gameRepo = new GameRepoInMemory();

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
                entity.getAdquisitionDate(),
                entity.getTimePlaying(),
                entity.getLastPlayed(),
                entity.getInstalationState());
    }

    public static UserDTO findUser(Long id){
        return mapFrom(userRepo.getById(id).get());
    }

    public static GameDTO finGame(Long id){
        return mapFrom(gameRepo.getById(id).get());
    }


}
