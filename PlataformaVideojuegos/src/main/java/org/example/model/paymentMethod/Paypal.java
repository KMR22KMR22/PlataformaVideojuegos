package org.example.model.paymentMethod;

public class Paypal implements IPaymentMethod {
    @Override
    public boolean makePayment() {
        return false;
    }
}
