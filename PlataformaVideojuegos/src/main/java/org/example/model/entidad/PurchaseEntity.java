package org.example.model.entidad;

import jakarta.persistence.*;
import org.example.model.dto.purchase.PurchaseState;

import org.example.model.paymentMethod.PaymentMethod;
import java.time.LocalDate;

@Table(name = "juegos")
@Entity
public class PurchaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "id_usuario")
    private Long idUser;
    @Column(name = "id_jeugo")
    private Long idGame;
    @Column(name = "fecha_compra")
    private LocalDate purchaseDate;
    @Column(name = "metodo_pago")
    private PaymentMethod paymentMethod;
    @Column(name = "presio_sin_descuento")
    private float priceWithoutDiscount;
    @Column(name = "descuento_aplicado")
    private float discountApplicated;
    @Column(name = "estado")
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

    public PaymentMethod getPaymentMethod() {
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

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public void setPriceWithoutDiscount(float priceWithoutDiscount) {
        this.priceWithoutDiscount = priceWithoutDiscount;
    }

    public void setDiscountApplicated(int discountApplicated) {
        this.discountApplicated = discountApplicated;
    }

    public void setSatate(PurchaseState satate) {
        this.satate = satate;
    }


    //Constructor Actualizar

    public PurchaseEntity(Long id, Long idUser, Long idGame, LocalDate purchaseDate, PaymentMethod paymentMethod, float priceWithoutDiscount, float discountApplicated, PurchaseState satate) {
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

    public PurchaseEntity(Long id, Long idUser, Long idGame, PaymentMethod paymentMethod, float priceWithoutDiscount, float discountApplicated) {
        this.id = id;
        this.idUser = idUser;
        this.idGame = idGame;
        this.purchaseDate = LocalDate.now();
        this.paymentMethod = paymentMethod;
        this.priceWithoutDiscount = priceWithoutDiscount;
        this.discountApplicated = discountApplicated;
        this.satate = PurchaseState.PENDIENTE;
    }

    @Override
    public String toString() {
        return "PurchaseEntity{" +
                "id=" + id +
                ", idUser=" + idUser +
                ", idGame=" + idGame +
                ", purchaseDate=" + purchaseDate +
                ", paymentMethod=" + paymentMethod +
                ", priceWithoutDiscount=" + priceWithoutDiscount +
                ", discountApplicated=" + discountApplicated +
                ", satate=" + satate +
                '}';
    }
}
