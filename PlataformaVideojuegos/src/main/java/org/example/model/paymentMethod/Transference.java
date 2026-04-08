package org.example.model.paymentMethod;

public class Transference implements IPaymentMethod {
    @Override
    public boolean makePayment() {
        return false;
    }
}
