package dao;

import models.Breed;
import entity.Cat;
import models.Colour;
import org.hibernate.query.Query;

import java.util.List;
import java.util.Optional;

public class CatDao extends AbstractDao {

    public Cat update(Cat cat) {
        return (Cat) doInTransaction(
                (session, transaction) ->
                        session.merge(cat)
        );
    }

    public void remove(Cat cat) {
        doInTransactionWithoutResult(
                (session, transaction) ->
                        session.remove(cat)
        );
    }

    public Optional<Cat> getById(int id) {
        return Optional.ofNullable(
                doWithSession(
                        session -> session.get(Cat.class, id)
                )
        );
    }

    public List<Cat> getAll() {
        return doWithSession(session ->
                session.createQuery("select c FROM Cat c", Cat.class).getResultList()
        );
    }

    public List<Cat> getAllFriends(int id) {
        return doWithSession(
                session -> {
                    Query query = session.createQuery("SELECT k.friends FROM Cat k WHERE k.id=:id");
                    query.setParameter("id", id);
                    return query.getResultList();
                }
        );
    }

    public List<Cat> getByBreed(Breed breed) {
        return doWithSession(
                session -> {
                    Query query = session.createQuery("SELECT k.friends FROM Cat k WHERE k.breed=:breed");
                    query.setParameter("breed", breed);
                    return query.getResultList();
                }
        );
    }

    public List<Cat> getByColour(Colour colour) {
        return doWithSession(
                session -> {
                    Query query = session.createQuery("SELECT k.friends FROM Cat k WHERE k.colour=:colour");
                    query.setParameter("colour", colour);
                    return query.getResultList();
                }
        );
    }
}
