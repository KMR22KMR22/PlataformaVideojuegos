package main.java.Model.Form;

import main.java.Model.DTO.Purchase.PaymentMethods;
import main.java.Model.DTO.Purchase.PurchaseState;

import java.time.LocalDate;

public class PurchaseForm {

    private Long id;
    private int idUser;
    private int idGame;
    private LocalDate purchaseDate;
    private PaymentMethods paymentMethod;
    private float priceWithoutDiscount;
    private float discountApplicated;
    private PurchaseState satate;


    //Getters


    public Long getId() {
        return id;
    }

    public int getIdUser() {
        return idUser;
    }

    public int getIdGame() {
        return idGame;
    }

    public LocalDate getPurchaseDate() {
        return purchaseDate;
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

    public PurchaseState getSatate() {
        return satate;
    }


    //Constructor


    public PurchaseForm(Long id, int idUser, int idGame, LocalDate purchaseDate, PaymentMethods paymentMethod, float priceWithoutDiscount, float discountApplicated, PurchaseState satate) {
        this.id = id;
        this.idUser = idUser;
        this.idGame = idGame;
        this.purchaseDate = purchaseDate;
        this.paymentMethod = paymentMethod;
        this.priceWithoutDiscount = priceWithoutDiscount;
        this.discountApplicated = discountApplicated;
        this.satate = satate;
    }
}
