package org.example.Model.PaymentMethods;

public class SteamWallet implements IPaymentMethod {
    @Override
    public boolean makePayment() {
        return false;
    }
}
