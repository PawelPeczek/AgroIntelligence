package model.dao;

import model.entities.Field;
import model.entities.Strategy;
import model.entities.WaterCannon;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.Optional;

public class WaterCannonDAO {
    public Optional<WaterCannon> createCannon(int fieldID){
        Optional<WaterCannon> result;
        WaterCannon cannon = new WaterCannon();
        try (Session session = HibernateHelper.getSession()) {
            Transaction tx = session.beginTransaction();
            tx.begin();
            Field field = session.get(Field.class, fieldID);
            if (field == null) {
                result = Optional.empty();
            } else {
                cannon.setField(field);
                field.addCannon(cannon);
                session.save(cannon);
                result = Optional.of(cannon);
            }
            tx.commit();
        } catch (Exception ex){
            result = Optional.empty();
        }
        return result;
    }

    public Optional<WaterCannon> addCannonToField(int cannonID, int fieldId){
        Optional<WaterCannon> result;
        try (Session session = HibernateHelper.getSession()) {
            Transaction tx = session.beginTransaction();
            tx.begin();
            WaterCannon cannon = session.get(WaterCannon.class, cannonID);
            Field field = session.get(Field.class, fieldId);
            if (cannon == null || field == null) {
                result = Optional.empty();
            } else {
                field.addCannon(cannon);
                cannon.setField(field);
                result = Optional.of(cannon);
            }
            tx.commit();
        } catch (Exception ex){
            result = Optional.empty();
        }
        return result;
    }
}
