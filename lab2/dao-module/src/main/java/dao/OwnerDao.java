package dao;

import entity.Cat;
import entity.Owner;
import org.hibernate.query.Query;

import java.util.List;
import java.util.Optional;

public class OwnerDao extends AbstractDao{
    public Owner update(Owner owner) {
        return (Owner) doInTransaction(
                (session, transaction) ->
                        session.merge(owner)
        );
    }

    public void remove(Owner owner) {
        doInTransactionWithoutResult(
                (session, transaction) ->
                        session.remove(owner)
        );
    }

    public Optional<Owner> getById(int id) {
        return Optional.ofNullable(
                doWithSession(
                        session -> session.get(Owner.class, id)
                )
        );
    }

    public List<Owner> getAll() {
        return doWithSession(session ->
                session.createQuery("select c FROM Owner c", Owner.class).getResultList()
        );
    }

    public List<Cat> getAllCats(int id) {
        return doWithSession(
                session -> {
                    Query query = session.createQuery("SELECT o.catList FROM Owner o WHERE o.id=:id");
                    query.setParameter("id", id);
                    return query.getResultList();
                }
        );
    }
}
