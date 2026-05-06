package org.example.repository.Hibernate;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.example.model.entidad.PurchaseEntity;
import org.example.model.form.PurchaseForm;
import org.example.model.form.updates.PurchaseUpdate;
import org.example.repository.Interface.IPurchaseRepo;
import org.example.transaction.ISesionManager;

import java.util.List;
import java.util.Optional;

public class PurchaseRepoHibernate implements IPurchaseRepo {
    private final ISesionManager sm;

    public PurchaseRepoHibernate (ISesionManager sm) {
        this.sm = sm;
    }

    @Override
    public Optional<PurchaseEntity> create(PurchaseForm form) {
        var session = sm.getSession();
        var purchase = (new PurchaseEntity(0L, form.idUser(), form.idGame(), form.paymentMethod(), form.priceWithoutDiscount(), form.discountApplicated()));
        session.persist(purchase);
        return Optional.of(purchase);
    }

    @Override
    public Optional<PurchaseEntity> getById(Long id) {
        var session = sm.getSession();
        var purchase = session.find(PurchaseEntity.class, id);
        return Optional.ofNullable(purchase);
    }

    @Override
    public List<PurchaseEntity> getAll() {
        var session = sm.getSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<PurchaseEntity> cq = cb.createQuery(PurchaseEntity.class);
        Root<PurchaseEntity> root = cq.from(PurchaseEntity.class);

        cq.select(root);

        return session.createQuery(cq).getResultList();
    }

    @Override
    public Optional<PurchaseEntity> update(Long id, PurchaseUpdate form) {
        var session = sm.getSession();
        var purchaseOpt = this.getById(id);

        if (purchaseOpt.isEmpty())
            return Optional.empty();

        session.merge(new PurchaseEntity(id, form.idUser(), form.idGame(), form.purchaseDate(), form.paymentMethod(), form.priceWithoutDiscount(),
                form.discountApplicated(), form.satate()));

        return getById(id);
    }

    @Override
    public boolean delete(Long id) {
        var session = sm.getSession();

        var purchase = this.getById(id);
        if (purchase.isEmpty())
            return false;

        session.remove(purchase);

        return true;
    }
}
