package org.example.repository.Hibernate;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.example.model.entidad.LibraryEntity;
import org.example.model.form.LibraryForm;
import org.example.model.form.updates.LibraryUpdate;
import org.example.repository.Interface.ILibraryRepo;
import org.example.transaction.ISesionManager;

import java.util.List;
import java.util.Optional;

public class LibraryRepoHibernate implements ILibraryRepo {
    private final ISesionManager sm;

    public LibraryRepoHibernate (ISesionManager sm) {
        this.sm = sm;
    }

    @Override
    public Optional<LibraryEntity> getByUserGameId(Long idUser, Long idGame) {
        var session = sm.getSession();

        var library = session.createQuery(
                        "FROM LibraryEntity l WHERE l.user.id = :idUser AND l.game.id = :idGame",
                        LibraryEntity.class)
                .setParameter("idUser", idUser)
                .setParameter("idGame", idGame)
                .getResultStream()
                .findFirst()
                .orElse(null);

        return Optional.ofNullable(library);
    }

    @Override
    public Optional<LibraryEntity> create(LibraryForm form) {
        var session = sm.getSession();
        var library = (new LibraryEntity(-1L, form.idUser(), form.idGame(), form.acquisitionDate()));
        session.persist(library);
        return Optional.of(library);
    }

    @Override
    public Optional<LibraryEntity> getById(Long id) {
        var session = sm.getSession();
        var library = session.find(LibraryEntity.class, id);
        return Optional.ofNullable(library);
    }

    @Override
    public List<LibraryEntity> getAll() {
        var session = sm.getSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<LibraryEntity> cq = cb.createQuery(LibraryEntity.class);
        Root<LibraryEntity> root = cq.from(LibraryEntity.class);

        cq.select(root);

        return session.createQuery(cq).getResultList();
    }

    @Override
    public Optional<LibraryEntity> update(Long id, LibraryUpdate form) {
        var session = sm.getSession();
        var libraryOpt = this.getById(id);

        if (libraryOpt.isEmpty())
            return Optional.empty();

        session.merge(new LibraryEntity(id, form.idUser(), form.idGame(), form.acquisitionDate(), form.timePlaying(), form.lastPlayed(), form.instalationState()));

        return getById(id);
    }

    @Override
    public boolean delete(Long id) {
        var session = sm.getSession();

        var library = this.getById(id);
        if (library.isEmpty())
            return false;

        session.remove(library);

        return true;
    }
}
