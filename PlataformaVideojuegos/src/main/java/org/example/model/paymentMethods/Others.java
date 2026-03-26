package org.example.model.paymentMethods;

public class Others implements IPaymentMethod {
    @Override
    public boolean makePayment() {
        return false;
    }
}
