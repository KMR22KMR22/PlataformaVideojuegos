package org.example.Model.DTO.Purchase;

import org.example.Model.DTO.Game.GameDTO;
import org.example.Model.DTO.User.UserDTO;

import java.time.LocalDate;

public class PurchaseDTO {

    private Long id;
    private Long idUser;
    private UserDTO user;
    private Long idGame;
    private GameDTO game;
    private LocalDate purchaseDate;
    private PaymentMethods paymentMethod;
    private float priceWithoutDiscount;
    private float discountApplicated;
    private PurchaseState satate;


    //Getters


    public Long getId() {
        return id;
    }

    public Long getIdUser() {
        return idUser;
    }

    public Long getIdGame() {
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


    public PurchaseDTO(Long id, Long idUser, Long idGame, LocalDate purchaseDate, PaymentMethods paymentMethod, float priceWithoutDiscount, float discountApplicated, PurchaseState satate) {
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
