package org.example.Model.Form;

import org.example.Model.PaymentMethods.IPaymentMethod;

import java.time.LocalDate;

public record PurchaseForm (Long idUser,
                            Long idGame,
                            IPaymentMethod paymentMethod,
                            float priceWithoutDiscount,
                            float discountApplicated){


}

