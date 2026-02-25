package org.example.Model.Form;

import org.example.Model.DTO.Purchase.PaymentMethods;

import java.time.LocalDate;

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
