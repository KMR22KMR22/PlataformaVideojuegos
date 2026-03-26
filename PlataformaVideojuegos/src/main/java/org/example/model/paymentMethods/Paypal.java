package org.example.model.paymentMethods;

public class Paypal implements IPaymentMethod {
    @Override
    public boolean makePayment() {
        return false;
    }
}
