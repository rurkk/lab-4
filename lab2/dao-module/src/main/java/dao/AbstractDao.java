package dao;

import org.hibernate.Session;
import org.hibernate.Transaction;
import utils.SessionFactoryInstance;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

public abstract class AbstractDao {
    protected void doInTransactionWithoutResult(BiConsumer<Session, Transaction> operation) {
        doInTransaction(((session, transaction) -> {
                    operation.accept(session, transaction);
                    return null;
                })
        );
    }

    protected <T> T doInTransaction(BiFunction<Session, Transaction, T> operation) {
        return doWithSession(session -> {
            Transaction transaction = null;

            try {
                transaction = session.beginTransaction();

                T result = operation.apply(session, transaction);

                transaction.commit();
                return result;
            } catch (Exception e) {
                System.err.println(e);
                if (transaction != null) {
                    transaction.rollback();
                }
            }

            return null;
        });
    }

    protected <T> T doWithSession(Function<Session, T> actor) {
        try (Session session = SessionFactoryInstance.getInstance().openSession()) {
            return actor.apply(session);
        }
    }
}
