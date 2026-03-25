package org.example.Model.PaymentMethods;

public class Others implements IPaymentMethod {
    @Override
    public boolean makePayment() {
        return false;
    }
}
