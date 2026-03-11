package org.example.Repository.InMemory;

import org.example.Model.Entidad.LibraryEntity;
import org.example.Model.Entidad.PurchaseEntity;
import org.example.Model.Form.PurchaseForm;
import org.example.Model.Form.Updates.PurchaseUpdateForm;
import org.example.Repository.Interface.IPurchaseRepo;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PurchaseRepoMemory implements IPurchaseRepo {

    private static final List<PurchaseEntity> PURCHASES = new ArrayList<>();
    private static Long NEXT_ID = 0L;


    @Override
    public Optional<PurchaseEntity> crear(PurchaseForm form) {
        var purchase = new PurchaseEntity(NEXT_ID++, form.idUser(), form.idGame(), form.paymentMethod(), form.priceWithoutDiscount(), form.discountApplicated());
        PURCHASES.add(purchase);
        return Optional.of(purchase);
    }

    @Override
    public Optional<PurchaseEntity> obtenerPorId(Long id) {
        return PURCHASES.stream().filter(p -> p.getIdUser() == id).findFirst();
    }

    @Override
    public List<PurchaseEntity> obtenerTodos() {return new ArrayList<>(PURCHASES);
    }

    @Override
    public Optional<PurchaseEntity> actualizar(Long id, PurchaseUpdateForm form) {
        obtenerPorId(id).orElseThrow(()-> new IllegalArgumentException("Compra no encontrada"));

        var purchaseUpdated = new LibraryEntity(form.id(), form.idUser(), form.idGame(), form.adquisitionDate(), form.timePlaying(), form.lastPlayed(), form.instalationState());
        PURCHASES.removeIf(p -> p.getId().equals(id));
        PURCHASES.add(purchaseUpdated);
        return Optional.of(purchaseUpdated);
    }


    @Override
    public boolean eliminar(Long id) {
        return PURCHASES.removeIf(p -> p.getId().equals(id));
    }
}
