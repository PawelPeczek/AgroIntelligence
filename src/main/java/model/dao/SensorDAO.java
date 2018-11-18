package model.dao;

import model.entities.Sensor;
import model.entities.WaterCannon;
import org.hibernate.Session;

import java.util.List;
import java.util.Optional;

import org.hibernate.Transaction;

public class SensorDAO extends SimpleInsertDAO {

    public Optional<Sensor> createSensor(){
        Sensor sensor = new Sensor();
        return createEntity(sensor).map(e -> (Sensor)e);
    }

    public Optional<Sensor> createSensor(int cannonID){
        Optional<Sensor> result;
        Sensor sensor = new Sensor();
        try (Session session = HibernateHelper.getSession()) {
            Transaction tx = session.beginTransaction();
            tx.begin();
            addCannonSensorRelation(session, sensor, cannonID);
            session.save(sensor);
            result = Optional.of(sensor);
            tx.commit();
        } catch (Exception ex){
            result = Optional.empty();
        }
        return result;
    }

    public Optional<Sensor> createSensor(List<Integer> cannonIDs){
        Optional<Sensor> result;
        Sensor sensor = new Sensor();
        try (Session session = HibernateHelper.getSession()) {
            Transaction tx = session.beginTransaction();
            tx.begin();
            for (Integer id : cannonIDs) {
                addCannonSensorRelation(session, sensor, id);
            }
            session.save(sensor);
            result = Optional.of(sensor);
            tx.commit();
        } catch (Exception ex){
            result = Optional.empty();
        }
        return result;
    }

    private void addCannonSensorRelation(Session session, Sensor sensor, int cannonID){
        WaterCannon cannon = session.get(WaterCannon.class, cannonID);
        if(cannon == null){
            throw new IllegalArgumentException("Bad ID.");
        }
        cannon.addSensor(sensor);
        sensor.addCannon(cannon);
    }


}
