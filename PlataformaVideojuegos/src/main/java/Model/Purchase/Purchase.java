package Model.Purchase;

import Model.Game.Game;
import Model.User.User;

import java.time.LocalDate;

public class Purchase {

    private int id;
    private int idUser;
    private int idGame;
    private LocalDate purchaseDate;
    private PaymentMethods paymentMethod;
    private float priceWithoutDiscount;
    private float discountApplicated;
    private PurchaseState satate;


    //Getters


    public int getId() {
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

    public Enum getPaymentMethod() {
        return paymentMethod;
    }

    public float getPriceWithoutDiscount() {
        return priceWithoutDiscount;
    }

    public float getDiscountApplicated() {
        return discountApplicated;
    }

    public Enum getSatate() {
        return satate;
    }


    //Constructor


    public Purchase(int id, int idUser, int idGame, LocalDate purchaseDate, PaymentMethods paymentMethod, float priceWithoutDiscount, float discountApplicated, PurchaseState satate) {
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
