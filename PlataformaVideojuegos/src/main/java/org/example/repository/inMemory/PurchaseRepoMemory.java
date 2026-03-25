package org.example.repository.inMemory;

import org.example.model.entidad.PurchaseEntity;
import org.example.model.form.PurchaseForm;
import org.example.model.form.updates.PurchaseUpdate;
import org.example.repository.Interface.IPurchaseRepo;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PurchaseRepoMemory implements IPurchaseRepo {

    private static final List<PurchaseEntity> PURCHASES = new ArrayList<>();
    private static Long NEXT_ID = 0L;


    @Override
    public Optional<PurchaseEntity> create(PurchaseForm form) {
        var purchase = new PurchaseEntity(NEXT_ID++, form.idUser(), form.idGame(), form.paymentMethod(), form.priceWithoutDiscount(), form.discountApplicated());
        PURCHASES.add(purchase);
        return Optional.of(purchase);
    }

    @Override
    public Optional<PurchaseEntity> getById(Long id) {
        return PURCHASES.stream().filter(p -> p.getIdUser() == id).findFirst();
    }

    @Override
    public List<PurchaseEntity> getAll() {return new ArrayList<>(PURCHASES);
    }

    public List<PurchaseEntity> getPurchasesCustomer(Long idUser){
        return PURCHASES.stream()
                .filter(p -> p.getIdUser() == idUser)
                .toList();}

    @Override
    public Optional<PurchaseEntity> update(Long id, PurchaseUpdate form) {
        getById(id).orElseThrow(()-> new IllegalArgumentException("Compra no encontrada"));

        var purchaseUpdated = new PurchaseEntity(form.id(), form.idUser(), form.idGame(), form.purchaseDate(), form.paymentMethod(), form.priceWithoutDiscount(), form.discountApplicated(), form.satate());
        PURCHASES.removeIf(p -> p.getId().equals(id));
        PURCHASES.add(purchaseUpdated);
        return Optional.of(purchaseUpdated);
    }


    @Override
    public boolean delete(Long id) {
        return PURCHASES.removeIf(p -> p.getId().equals(id));
    }
}
