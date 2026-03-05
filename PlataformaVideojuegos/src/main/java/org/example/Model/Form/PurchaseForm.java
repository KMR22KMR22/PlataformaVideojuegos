package org.example.Model.Form;

import org.example.Model.DTO.Game.GameDTO;
import org.example.Model.DTO.Purchase.PaymentMethods;
import org.example.Model.DTO.Purchase.PurchaseState;
import org.example.Model.DTO.User.UserDTO;

import java.time.LocalDate;

public class PurchaseForm {

    private Long idUser;
    private Long idGame;
    private PaymentMethods paymentMethod;
    private float priceWithoutDiscount;
    private float discountApplicated;


    //Getters


    public Long getIdUser() {
        return idUser;
    }

    public Long getIdGame() {
        return idGame;
    }

    public PaymentMethods getPaymentMethod() {
        return paymentMethod;
    }

    public float getPriceWithoutDiscount() {
        return priceWithoutDiscount;
    }

    public float getDiscountApplicated() {
        return discountApplicated;
    }


    //Constructor


    public PurchaseForm(Long idUser, Long idGame, PaymentMethods paymentMethod, float priceWithoutDiscount, float discountApplicated) {
        this.idUser = idUser;
        this.idGame = idGame;
        this.paymentMethod = paymentMethod;
        this.priceWithoutDiscount = priceWithoutDiscount;
        this.discountApplicated = discountApplicated;
    }
}

