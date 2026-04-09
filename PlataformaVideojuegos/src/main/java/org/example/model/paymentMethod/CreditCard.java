package org.example.model.paymentMethod;

public class CreditCard implements IPaymentMethod {

    @Override
    public void makePayment(float gameCost) {
        PaymentUtil.chackBalance(gameCost,);
    }
}
