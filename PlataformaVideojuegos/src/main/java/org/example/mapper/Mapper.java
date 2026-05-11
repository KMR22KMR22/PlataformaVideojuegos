package org.example.mapper;

import org.example.model.dto.game.GameDTO;
import org.example.model.dto.library.LibraryDTO;
import org.example.model.dto.purchase.PurchaseDTO;
import org.example.model.dto.review.ReviewDTO;
import org.example.model.dto.user.UserDTO;
import org.example.model.entidad.*;
import org.example.model.form.errors.ErrorDto;
import org.example.repository.inMemory.GameRepoInMemory;
import org.example.repository.inMemory.UserRepoInMemory;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Mapper {



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

        //Si los idiomas estan en null creo una lista vacia de idiomas porque si dejo la lista nula luego en el momento de otener los idiomas lanza una exepcion por ser null
        List<String> languages = entity.getAvailabeLanguages();
        if (languages == null) {
            languages = new ArrayList<>();
        }

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
                languages,
                entity.getState());
    }

    //Purchase
    public static PurchaseDTO mapFrom(PurchaseEntity entity, Optional<UserDTO> userDTO, Optional<GameDTO> gameDTO) {
        if (entity == null)
            return null;


        return new PurchaseDTO(
                entity.getId(),
                entity.getIdUser(),
                userDTO.orElse(null),
                entity.getIdGame(),
                gameDTO.orElse(null),
                entity.getPurchaseDate(),
                entity.getPaymentMethod(),
                entity.getPriceWithoutDiscount(),
                entity.getDiscountApplicated(),
                entity.getSatate());
    }

    //Library
    public static LibraryDTO mapFrom(LibraryEntity entity, Optional<UserDTO> userDTO, Optional<GameDTO> gameDTO) {
        if (entity == null)
            return null;

        return new LibraryDTO(
                entity.getId(),
                entity.getIdUser(),
                userDTO.orElse(null),
                entity.getIdGame(),
                gameDTO.orElse(null),
                entity.getAcquisitionDate(),
                entity.getTimePlaying(),
                entity.getLastPlayed(),
                entity.getInstalationState());
    }

    //Review
    public static ReviewDTO mapFrom(ReviewEntity entity, Optional<UserDTO> userDTO, Optional<GameDTO> gameDTO) {
        if (entity == null)
            return null;

        return new ReviewDTO(
                entity.getId(),
                entity.getIdUser(),
                userDTO.orElse(null),
                entity.getIdGame(),
                gameDTO.orElse(null),
                entity.isRecommended(),
                entity.getReviwText(),
                entity.getHoursPlayed(),
                entity.getPublicationDate(),
                entity.getLastEditionDate(),
                entity.getState());
    }
}
