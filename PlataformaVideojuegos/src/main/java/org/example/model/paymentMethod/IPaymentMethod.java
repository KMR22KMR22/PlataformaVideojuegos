package org.example.model.paymentMethod;

import org.example.exeptions.ValidationException;

public interface IPaymentMethod {

    void makePayment(float gameCost, Long userId) throws ValidationException ;
}
