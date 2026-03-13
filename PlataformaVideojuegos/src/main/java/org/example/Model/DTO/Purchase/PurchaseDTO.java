package org.example.Model.DTO.Purchase;

import org.example.Model.DTO.Game.GameDTO;
import org.example.Model.DTO.User.UserDTO;
import org.example.Model.PaymentMethods.IPaymentMethod;

import java.time.LocalDate;

public record PurchaseDTO (
        Long id,
        Long idUser,
        UserDTO user,
        Long idGame,
        GameDTO game,
        LocalDate purchaseDate,
        IPaymentMethod paymentMethod,
        float priceWithoutDiscount,
        float discountApplicated,
        PurchaseState satate){

}
