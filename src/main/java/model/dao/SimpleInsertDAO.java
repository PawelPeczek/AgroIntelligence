package model.dao;

import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.Optional;

public abstract class SimpleInsertDAO {
    protected Optional<Object> createEntity(Object entity){
        Optional<Object> result;
        try (Session session = HibernateHelper.getSession()) {
            Transaction tx = session.beginTransaction();
            tx.begin();
            session.save(entity);
            result = Optional.of(entity);
            tx.commit();
        } catch (Exception ex) {
            result = Optional.empty();
        }
        return result;
    }
}
