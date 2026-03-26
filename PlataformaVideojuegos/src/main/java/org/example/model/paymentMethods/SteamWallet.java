package org.example.model.paymentMethods;

public class SteamWallet implements IPaymentMethod {
    @Override
    public boolean makePayment() {
        return false;
    }
}
