package org.example.Model.Form;

import org.example.Model.DTO.Game.GameDTO;
import org.example.Model.DTO.Purchase.PaymentMethods;
import org.example.Model.DTO.Purchase.PurchaseState;
import org.example.Model.DTO.User.UserDTO;

import java.time.LocalDate;

public record PurchaseForm (Long idUser,
                            Long idGame,
                            PaymentMethods paymentMethod,
                            float priceWithoutDiscount,
                            float discountApplicated){


}

