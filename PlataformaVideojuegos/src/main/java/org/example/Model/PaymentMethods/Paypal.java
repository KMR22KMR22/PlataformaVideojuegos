package org.example.Model.PaymentMethods;

public class Paypal implements IPaymentMethod {
    @Override
    public boolean makePayment() {
        return false;
    }
}
