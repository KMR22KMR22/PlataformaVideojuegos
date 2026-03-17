package org.example.Model.Form.Updates;

import org.example.Model.DTO.Purchase.PurchaseState;
import org.example.Model.PaymentMethods.IPaymentMethod;

import java.time.LocalDate;

public record PurchaseUpdate(
        Long id,
        Long idUser,
        Long idGame,
        LocalDate purchaseDate,
        IPaymentMethod paymentMethod,
        float priceWithoutDiscount,
        float discountApplicated,
        PurchaseState satate){
}
