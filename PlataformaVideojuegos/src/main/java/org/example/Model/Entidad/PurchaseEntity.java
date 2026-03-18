package org.example.Model.Entidad;

import org.example.Model.DTO.Purchase.PurchaseState;
import org.example.Model.PaymentMethods.IPaymentMethod;

import java.time.LocalDate;

public class PurchaseEntity {

    private Long id;
    private Long idUser;
    private Long idGame;
    private LocalDate purchaseDate;
    private IPaymentMethod paymentMethod;
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

    public IPaymentMethod getPaymentMethod() {
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


    //Setters

    public void setId(Long id) {
        this.id = id;
    }

    public void setIdUser(Long idUser) {
        this.idUser = idUser;
    }

    public void setIdGame(Long idGame) {
        this.idGame = idGame;
    }

    public void setPurchaseDate(LocalDate purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public void setPaymentMethod(IPaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public void setPriceWithoutDiscount(float priceWithoutDiscount) {
        this.priceWithoutDiscount = priceWithoutDiscount;
    }

    public void setDiscountApplicated(float discountApplicated) {
        this.discountApplicated = discountApplicated;
    }

    public void setSatate(PurchaseState satate) {
        this.satate = satate;
    }


    //Constructor Actualizar

    public PurchaseEntity(Long id, Long idUser, Long idGame, LocalDate purchaseDate, IPaymentMethod paymentMethod, float priceWithoutDiscount, float discountApplicated, PurchaseState satate) {
        this.id = id;
        this.idUser = idUser;
        this.idGame = idGame;
        this.purchaseDate = purchaseDate;
        this.paymentMethod = paymentMethod;
        this.priceWithoutDiscount = priceWithoutDiscount;
        this.discountApplicated = discountApplicated;
        this.satate = satate;
    }


    //Constructor Creacion

    public PurchaseEntity(Long id, Long idUser, Long idGame, IPaymentMethod paymentMethod, float priceWithoutDiscount, float discountApplicated) {
        this.id = id;
        this.idUser = idUser;
        this.idGame = idGame;
        this.purchaseDate = LocalDate.now();
        this.paymentMethod = paymentMethod;
        this.priceWithoutDiscount = priceWithoutDiscount;
        this.discountApplicated = discountApplicated;
        this.satate = PurchaseState.PENDIENTE;
    }
}
