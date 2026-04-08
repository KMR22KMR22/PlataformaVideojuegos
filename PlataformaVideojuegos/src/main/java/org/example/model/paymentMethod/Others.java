package org.example.model.paymentMethod;

public class Others implements IPaymentMethod {
    @Override
    public boolean makePayment() {
        return false;
    }
}
