package org.example.Model.DTO.Purchase;

import org.example.Model.DTO.Game.GameDTO;
import org.example.Model.DTO.User.UserDTO;

import java.time.LocalDate;

public record PurchaseDTO (
        Long id,
        Long idUser,
        UserDTO user,
        Long idGame,
        GameDTO game,
        LocalDate purchaseDate,
        PaymentMethods paymentMethod,
        float priceWithoutDiscount,
        float discountApplicated,
        PurchaseState satate){

}
