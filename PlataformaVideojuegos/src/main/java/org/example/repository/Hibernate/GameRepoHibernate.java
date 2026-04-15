package org.example.repository.Hibernate;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.example.model.entidad.GameEntity;
import org.example.model.form.GameForm;
import org.example.model.form.updates.GameUpdate;
import org.example.repository.Interface.IGameRepo;
import org.example.transaction.ISesionManager;

import java.util.List;
import java.util.Optional;

public class GameRepoHibernate implements IGameRepo {
    private final ISesionManager sm;

    public GameRepoHibernate (ISesionManager sm) {
        this.sm = sm;
    }

    @Override
    public Optional<GameEntity> create(GameForm form) {

        var session = sm.getSession();
        var game = (new GameEntity(-1L, form.tittle(), form.description(), form.developer(), form.launchDate(), form.basePrice(),
                form.category(), form.gameAgeClasification(), form.availabeLanguages()));
        session.persist(game);
        return Optional.of(game);
    }

    @Override
    public Optional<GameEntity> getById(Long id) {
        var session = sm.getSession();
        var game = session.find(GameEntity.class, id);
        return Optional.ofNullable(game);
    }

    @Override
    public List<GameEntity> getAll() {
        var session = sm.getSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<GameEntity> cq = cb.createQuery(GameEntity.class);
        Root<GameEntity> root = cq.from(GameEntity.class);

        cq.select(root);

        return session.createQuery(cq).getResultList();
    }

    @Override
    public Optional<GameEntity> update(Long id, GameUpdate form) {
        var session = sm.getSession();
        var gameOpt = this.getById(id);

        if (gameOpt.isEmpty())
            return Optional.empty();

        session.merge(new GameEntity(id, form.tittle(), form.description(),
                form.developer(), form.launchDate(),
                form.basePrice(), form.currentDescount(), form.category(),
                form.gameAgeClasification(), form.availabeLanguages(), form.State()));

        return getById(id);
    }

    @Override
    public boolean delete(Long id) {
        var session = sm.getSession();

        var game = this.getById(id);
        if (game.isEmpty())
            return false;

        session.remove(game);

        return true;
    }

    @Override
    public Optional<GameEntity> findByName(String name) {
        var session = sm.getSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<GameEntity> cq = cb.createQuery(GameEntity.class);
        Root<GameEntity> root = cq.from(GameEntity.class);

        cq.select(root).where(cb.equal(root.get("titulo"), name));

        return session.createQuery(cq).getResultStream().findFirst();
    }
}
