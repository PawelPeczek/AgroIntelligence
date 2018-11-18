package model.dao;

import model.entities.Field;
import model.entities.Strategy;
import model.entities.WaterCannon;
import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.sql.Time;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class FieldDAO extends SimpleInsertDAO {

    public FieldDAO(){}

    public Optional<Field> createField(String name){
        Field field = new Field(name);
        return createEntity(field).map(e -> (Field) e);
    }

    public Optional<Field> editField(int id, String name){
        Optional<Field> result;
        try (Session session = HibernateHelper.getSession()) {
            Transaction tx = session.beginTransaction();
            tx.begin();
            Field field = session.get(Field.class, id);
            if (field == null) {
                result = Optional.empty();
            } else {
                field.setName(name);
                session.merge(field);
                result = Optional.of(field);
            }
            tx.commit();
        } catch (Exception ex){
            result = Optional.empty();
        }
        return result;
    }

    public Optional<List<Field>> getAllFields(){
        Optional<List<Field>> result;
        List<Field> fields = new LinkedList<>();
        try (Session session = HibernateHelper.getSession()) {
            Transaction tx = session.beginTransaction();
            tx.begin();
            Query q = session.createQuery("from Field");
            for (Object o : q.list()) {
                fields.add((Field) o);
            }
            tx.commit();
            result = Optional.of(fields);
        } catch (Exception ex) {
            result = Optional.empty();
        }
        return result;
    }

    public Optional<Field> getFieldByID(int id){
        Optional<Field> result;
        try (Session session = HibernateHelper.getSession()) {
            Transaction tx = session.beginTransaction();
            tx.begin();
            result = Optional.of(session.get(Field.class, id));
            tx.commit();
        } catch (Exception ex) {
            result = Optional.empty();
        }
        return result;
    }

    public Optional<List<WaterCannon>> getAllCannons(int fieldId){
        Optional<List<WaterCannon>> result;
        try (Session session = HibernateHelper.getSession()) {
            Transaction tx = session.beginTransaction();
            tx.begin();
            Field field = session.get(Field.class, fieldId);
            result = Optional.of(field.getCannons());
            tx.commit();
        } catch (Exception ex){
            result = Optional.empty();
        }
        return result;
    }


    public Optional<List<Strategy>> getAllStrategies(int fieldId){
        Optional<List<Strategy>> result;
        try (Session session = HibernateHelper.getSession()) {
            Transaction tx = session.beginTransaction();
            tx.begin();
            Field field = session.get(Field.class, fieldId);
            result = Optional.of(field.getStrategies());
            tx.commit();
        } catch (Exception ex){
            result = Optional.empty();
        }
        return result;
    }

    // if multiple strategies matches - undefined behaviour
    public Optional<Strategy> getCurrentStrategy(int fieldId){
        Optional<Strategy> result;
        try (Session session = HibernateHelper.getSession()) {
            Transaction tx = session.beginTransaction();
            tx.begin();
            Field field = session.get(Field.class, fieldId);
            result = returnFirstMatchingStrategy(field);
            tx.commit();
        } catch (Exception ex){
            result = Optional.empty();
        }
        return result;
    }

    private Optional<Strategy> returnFirstMatchingStrategy(Field field){
        Date nowDate = new Date();
        Time now = new Time(nowDate.getHours(), nowDate.getMinutes(), nowDate.getSeconds());
        for(Strategy s : field.getStrategies()){
            if(s.getStartTime().before(now) && s.getEndTime().after(now)){
                return Optional.of(s);
            }
        }
        return Optional.empty();
    }
}
