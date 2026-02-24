package org.example.Repository.InMemory;

import org.example.Model.Entidad.PurchaseEntity;
import org.example.Model.Form.PurchaseForm;
import org.example.Repository.Interface.IPurchaseRepo;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PurchaseRepoMemory implements IPurchaseRepo {

    private final List<PurchaseEntity> purchases = new ArrayList<>();
    private Long idCounter = 0L;


    @Override
    public Optional<PurchaseEntity> crear(PurchaseForm form) {
        var purchase = new PurchaseEntity(idCounter++, form.getIdUser(), form.getIdGame(), form.getPurchaseDate(), form.getPaymentMethod(), form.getPriceWithoutDiscount(), form.getDiscountApplicated(), form.getSatate());
        purchases.add(purchase);
        return Optional.of(purchase);
    }

    @Override
    public Optional<PurchaseEntity> obtenerPorId(Long id) {
        return purchases.stream().filter(p -> p.getIdUser() == id).findFirst();
    }

    @Override
    public List<PurchaseEntity> obtenerTodos() {return new ArrayList<>(purchases);
    }



    @Override
    public boolean eliminar(Long aLong) {
        return false;
    }
}
