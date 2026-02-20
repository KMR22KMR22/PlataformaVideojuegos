package org.example.Model.Form;

import org.example.Model.DTO.Purchase.PaymentMethods;

public class PurchaseForm {

    private PaymentMethods paymentMethod;


    //Getters

    public PaymentMethods getPaymentMethod() {
        return paymentMethod;
    }


    //Constructor
    public PurchaseForm(PaymentMethods paymentMethod) {

        this.paymentMethod = paymentMethod;
    }
}
