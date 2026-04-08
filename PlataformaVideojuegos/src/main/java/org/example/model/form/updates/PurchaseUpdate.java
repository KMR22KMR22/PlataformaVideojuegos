package org.example.model.form.updates;

import org.example.model.dto.purchase.PurchaseState;

import java.time.LocalDate;

public record PurchaseUpdate(
        Long id,
        Long idUser,
        Long idGame,
        LocalDate purchaseDate,
        PaymentMethod paymentMethod,
        float priceWithoutDiscount,
        float discountApplicated,
        PurchaseState satate) {
}
