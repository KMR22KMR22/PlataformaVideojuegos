package org.example.model.paymentMethod;

public class Others implements IPaymentMethod {

    @Override
    public void makePayment(float gameCost) {
        PaymentUtil.chackBalance(gameCost,);
    }
}
