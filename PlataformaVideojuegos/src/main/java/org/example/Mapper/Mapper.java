package org.example.Mapper;

import org.example.Model.DTO.Game.GameDTO;
import org.example.Model.DTO.Purchase.PurchaseDTO;
import org.example.Model.DTO.User.UserDTO;
import org.example.Model.Entidad.GameEntity;
import org.example.Model.Entidad.PurchaseEntity;
import org.example.Model.Entidad.UserEntity;

public class Mapper {

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


}
