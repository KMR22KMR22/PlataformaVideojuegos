package org.example.model.form;

public record PurchaseForm(Long idUser,
                           Long idGame,
                           PaymentMethod paymentMethod,
                           float priceWithoutDiscount,
                           float discountApplicated) {


}

