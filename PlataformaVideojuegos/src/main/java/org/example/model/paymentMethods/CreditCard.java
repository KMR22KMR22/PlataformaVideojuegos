package org.example.model.paymentMethods;

public class CreditCard implements IPaymentMethod {
    @Override
    public boolean makePayment() {
        return false;
    }
}
