package org.example.repository.Hibernate;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.example.model.entidad.Country;
import org.example.repository.Interface.ICountryRepo;
import org.example.transaction.ISesionManager;

import java.util.List;
import java.util.Optional;

public class CountryRepoHibernate implements ICountryRepo {
    private final ISesionManager sm;

    public CountryRepoHibernate (ISesionManager sm) {
        this.sm = sm;
    }

    @Override
    public Optional<Country> create(String form) {
        var session = sm.getSession();
        var country = new Country(null, form);
        session.persist(country);
        return Optional.of(country);
    }

    @Override
    public Optional<Country> getById(Long id) {
        var session = sm.getSession();
        var country = session.find(Country.class, id);
        return Optional.ofNullable(country);
    }

    @Override
    public List<Country> getAll() {
        var session = sm.getSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Country> cq = cb.createQuery(Country.class);
        Root<Country> root = cq.from(Country.class);

        cq.select(root);

        return session.createQuery(cq).getResultList();
    }

    @Override
    public Optional<Country> update(Long id, Country form) {
        var session = sm.getSession();
        var countryOpt = this.getById(id);

        if (countryOpt.isEmpty())
            return Optional.empty();

        session.merge(new Country(id, form.getName()));

        return getById(id);
    }

    @Override
    public boolean delete(Long id) {
        var session = sm.getSession();

        var country = this.getById(id);
        if (country.isEmpty())
            return false;

        session.remove(country);

        return true;
    }
}
