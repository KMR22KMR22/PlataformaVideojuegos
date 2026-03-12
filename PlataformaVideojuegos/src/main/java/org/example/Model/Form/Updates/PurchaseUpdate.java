package org.example.Model.Form.Updates;

import org.example.Model.DTO.Purchase.PaymentMethods;
import org.example.Model.DTO.Purchase.PurchaseState;

import java.time.LocalDate;

public record PurchaseUpdate(
        Long id,
        Long idUser,
        Long idGame,
        LocalDate purchaseDate,
        PaymentMethods paymentMethod,
        float priceWithoutDiscount,
        float discountApplicated,
        PurchaseState satate){
}
