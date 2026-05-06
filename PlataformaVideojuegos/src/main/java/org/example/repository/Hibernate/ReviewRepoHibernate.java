package org.example.repository.Hibernate;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.example.model.entidad.ReviewEntity;
import org.example.model.form.ReviewForm;
import org.example.model.form.updates.ReviewUpdate;
import org.example.repository.Interface.IReviewRepo;
import org.example.transaction.ISesionManager;

import java.util.List;
import java.util.Optional;

public class ReviewRepoHibernate implements IReviewRepo {
    private final ISesionManager sm;

    public ReviewRepoHibernate (ISesionManager sm) {
        this.sm = sm;
    }

    @Override
    public Optional<ReviewEntity> getByUserGameId(Long idUser, Long idGame) {
        var session = sm.getSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<ReviewEntity> cq = cb.createQuery(ReviewEntity.class);
        Root<ReviewEntity> root = cq.from(ReviewEntity.class);

        cq.select(root)
                .where(
                        cb.and(
                                cb.equal(root.get("idUser"), idUser),
                                cb.equal(root.get("idGame"), idGame)
                        )
                );

        return session.createQuery(cq)
                .uniqueResultOptional();
    }

    @Override
    public List<ReviewEntity> getByidGame(Long idGame) {
        var session = sm.getSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<ReviewEntity> cq = cb.createQuery(ReviewEntity.class);
        Root<ReviewEntity> root = cq.from(ReviewEntity.class);

        cq.select(root)
                .where(cb.equal(root.get("idGame"), idGame));

        return session.createQuery(cq).getResultList();
    }

    @Override
    public List<ReviewEntity> getByUserId(Long id) {
        var session = sm.getSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<ReviewEntity> cq = cb.createQuery(ReviewEntity.class);
        Root<ReviewEntity> root = cq.from(ReviewEntity.class);

        cq.select(root)
                .where(cb.equal(root.get("idUser"), id));

        return session.createQuery(cq).getResultList();
    }

    @Override
    public Optional<ReviewEntity> create(ReviewForm form) {
        var session = sm.getSession();
        var review = (new ReviewEntity(0L, form.idUser(), form.idGame(), form.recommended(), form.reviwText(), form.hoursPlayed()));
        session.persist(review);
        return Optional.of(review);
    }

    @Override
    public Optional<ReviewEntity> getById(Long id) {
        var session = sm.getSession();
        var review = session.find(ReviewEntity.class, id);
        return Optional.ofNullable(review);
    }

    @Override
    public List<ReviewEntity> getAll() {
        var session = sm.getSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<ReviewEntity> cq = cb.createQuery(ReviewEntity.class);
        Root<ReviewEntity> root = cq.from(ReviewEntity.class);

        cq.select(root);

        return session.createQuery(cq).getResultList();
    }

    @Override
    public Optional<ReviewEntity> update(Long id, ReviewUpdate form) {
        var session = sm.getSession();
        var reviewOpt = this.getById(id);

        if (reviewOpt.isEmpty())
            return Optional.empty();

        session.merge(new ReviewEntity(id, form.idUser(), form.idGame(), form.recommended(), form.reviwText(), form.hoursPlayed(), form.publicationDate(), form.lastEditionDate(), form.state()));

        return getById(id);
    }

    @Override
    public boolean delete(Long id) {
        var session = sm.getSession();

        var review = this.getById(id);
        if (review.isEmpty())
            return false;

        session.remove(review);

        return true;
    }
}
