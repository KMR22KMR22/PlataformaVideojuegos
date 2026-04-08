package org.example.model.paymentMethod;

public class CreditCard implements IPaymentMethod {
    @Override
    public boolean makePayment() {
        return false;
    }
}
