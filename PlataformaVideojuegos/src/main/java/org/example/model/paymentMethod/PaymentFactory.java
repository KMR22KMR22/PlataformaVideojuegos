package org.example.model.paymentMethod;

public class PaymentFactory {

    public static IPaymentMethod getPaymentMethod(PaymentMethod pm) {
        if(pm == PaymentMethod.CREDIT_CARD){
            return new CreditCard();
        }
        if(pm == PaymentMethod.PAYPAL){
            return new Paypal();
        }
        if(pm == PaymentMethod.STEAM_WALLET){
            return new SteamWallet();
        }
        if(pm == PaymentMethod.TRASFERENCE){
            return new Transference();
        }
        if(pm == PaymentMethod.OTHERS){
            return new Others();
        }
        return null;
    }


}
