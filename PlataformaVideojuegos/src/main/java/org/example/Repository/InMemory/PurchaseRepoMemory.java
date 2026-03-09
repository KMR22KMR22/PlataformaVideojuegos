package org.example.Repository.InMemory;

import org.example.Model.Entidad.PurchaseEntity;
import org.example.Model.Form.PurchaseForm;
import org.example.Repository.Interface.IPurchaseRepo;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PurchaseRepoMemory implements IPurchaseRepo {

    private static final List<PurchaseEntity> PURCHASES = new ArrayList<>();
    private static Long net_id = 0L;


    @Override
    public Optional<PurchaseEntity> crear(PurchaseForm form) {
        var purchase = new PurchaseEntity(net_id++, form.idUser(), form.idGame(), form.paymentMethod(), form.priceWithoutDiscount(), form.discountApplicated());
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
    public boolean eliminar(Long aLong) {
        return false;
    }
}
