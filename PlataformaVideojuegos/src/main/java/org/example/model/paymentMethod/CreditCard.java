package org.example.model.paymentMethod;

import org.example.exeptions.ValidationException;

public class CreditCard implements IPaymentMethod {



    @Override
    public void makePayment(float gameCost, Long userId) throws ValidationException {
        System.out.println("Procesando pago con Tarjeta de Credito.....");
    }
}
