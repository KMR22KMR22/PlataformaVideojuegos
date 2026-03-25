package org.example.model.dto.purchase;

import org.example.model.dto.game.GameDTO;
import org.example.model.dto.user.UserDTO;
import org.example.model.paymentMethods.IPaymentMethod;

import java.time.LocalDate;

public record PurchaseDTO(
        Long id,
        Long idUser,
        UserDTO user,
        Long idGame,
        GameDTO game,
        LocalDate purchaseDate,
        IPaymentMethod paymentMethod,
        float priceWithoutDiscount,
        float discountApplicated,
        PurchaseState satate) {

}
