package org.example.Controller;

import org.example.Model.Form.PurchaseForm;
import org.example.Repository.InMemory.PurchaseRepoMemory;

import java.util.ArrayList;
import java.util.List;

public class PurchaseController {

    private PurchaseRepoMemory repository = new PurchaseRepoMemory();
    private List<String> errores = new ArrayList<>();

    public List<String> validatePurchase(PurchaseForm purchase) {
        errores.clear();

        //Usuario
        validateUser(purchase);

        //Juego
        validateGame(purchase);

        //Fecha de compra
        validatePurchaseDate(purchase);

        //Metodo de pago
        validatePaymentMethod(purchase);

        //Precio de descuento
        validateDiscountPrice(purchase);

        //Precio sin descuento
        validatePrieceWithOutDiscount(purchase);

        //Descuento Aplicado
        validateDiscountAplicated(purchase);

        //Estado
        validateState(purchase);

        return errores;
    }
}
