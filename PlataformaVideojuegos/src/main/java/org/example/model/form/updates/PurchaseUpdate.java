package org.example.model.form.updates;

import org.example.model.dto.purchase.PurchaseState;
import org.example.model.paymentMethods.IPaymentMethod;

import java.time.LocalDate;

public record PurchaseUpdate(
        Long id,
        Long idUser,
        Long idGame,
        LocalDate purchaseDate,
        IPaymentMethod paymentMethod,
        float priceWithoutDiscount,
        float discountApplicated,
        PurchaseState satate) {
}
