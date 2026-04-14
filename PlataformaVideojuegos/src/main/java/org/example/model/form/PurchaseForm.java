package org.example.model.form;

import org.example.model.paymentMethod.PaymentMethod;

public record PurchaseForm(Long idUser,
                           Long idGame,
                           PaymentMethod paymentMethod,
                           float priceWithoutDiscount,
                           float discountApplicated) {


}

