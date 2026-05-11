package org.example.model.paymentMethod;

import org.example.repository.Interface.IUserRepo;

public class PaymentFactory {

    public static IPaymentMethod getPaymentMethod(PaymentMethod pm, IUserRepo userRepo) {
        switch (pm) {

            case CREDIT_CARD:
                return new CreditCard();

            case PAYPAL:
                return new Paypal();

            case STEAM_WALLET:
                return new SteamWallet(userRepo);

            case TRASFERENCE:
                return new Transference();

            case OTHERS:
                return new Others();

            default:
                throw new IllegalArgumentException(
                        "Método de pago no soportado: " + pm
                );
        }
    }
}
