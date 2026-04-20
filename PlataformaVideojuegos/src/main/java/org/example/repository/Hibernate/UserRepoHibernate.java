package org.example.repository.Hibernate;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.example.model.entidad.UserEntity;
import org.example.model.form.UserForm;
import org.example.model.form.updates.UserUpdate;
import org.example.repository.Interface.IUserRepo;
import org.example.transaction.ISesionManager;

import java.util.List;
import java.util.Optional;

public class UserRepoHibernate implements IUserRepo {
    private final ISesionManager sm;

    public UserRepoHibernate (ISesionManager sm) {
        this.sm = sm;
    }

    @Override
    public Optional<UserEntity> create(UserForm form) {
        var session = sm.getSession();
        var user = (new UserEntity(-1L, form.userName(), form.email(), form.password(), form.realName(), form.country(),
                form.birthDate(), form.avatar(), 0));
        session.persist(user);
        return Optional.of(user);
    }

    @Override
    public Optional<UserEntity> getById(Long id) {
        var session = sm.getSession();
        var user = session.find(UserEntity.class, id);
        return Optional.ofNullable(user);
    }

    @Override
    public List<UserEntity> getAll() {
        var session = sm.getSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<UserEntity> cq = cb.createQuery(UserEntity.class);
        Root<UserEntity> root = cq.from(UserEntity.class);

        cq.select(root);

        return session.createQuery(cq).getResultList();
    }

    @Override
    public Optional<UserEntity> update(Long id, UserUpdate form) {
        var session = sm.getSession();
        var userOpt = this.getById(id);

        if (userOpt.isEmpty())
            return Optional.empty();

        session.merge(new UserEntity(id, form.userName(), form.email(), form.password(), form.realName(), form.country(),
                form.birthDate(), form.registrationDate(), form.avatar(), form.portfolioBalance(), form.accountState()));

        return getById(id);
    }

    @Override
    public boolean delete(Long id) {
        var session = sm.getSession();

        var user = this.getById(id);
        if (user.isEmpty())
            return false;

        session.remove(user);

        return true;
    }
}
