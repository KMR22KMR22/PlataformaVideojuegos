package org.example.model.paymentMethod;

public class SteamWallet implements IPaymentMethod {
    @Override
    public boolean makePayment() {
        return false;
    }
}
