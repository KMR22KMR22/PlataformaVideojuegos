package org.example.Model.PaymentMethods;

public class Transference implements IPaymentMethod {
    @Override
    public boolean makePayment() {
        return false;
    }
}
