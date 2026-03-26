package org.example.model.paymentMethods;

public class Transference implements IPaymentMethod {
    @Override
    public boolean makePayment() {
        return false;
    }
}
