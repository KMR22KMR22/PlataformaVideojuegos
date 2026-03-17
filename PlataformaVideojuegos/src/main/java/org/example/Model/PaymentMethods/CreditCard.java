package org.example.Model.PaymentMethods;

public class CreditCard implements IPaymentMethod {
    @Override
    public boolean makePayment() {
        return false;
    }
}
