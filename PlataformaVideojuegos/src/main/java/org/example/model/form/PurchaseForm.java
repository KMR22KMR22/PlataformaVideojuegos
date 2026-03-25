package org.example.model.form;

import org.example.model.paymentMethods.IPaymentMethod;

public record PurchaseForm(Long idUser,
                           Long idGame,
                           IPaymentMethod paymentMethod,
                           float priceWithoutDiscount,
                           float discountApplicated) {


}

