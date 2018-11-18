package model.dao;

import model.entities.HumidityMeasurement;
import model.entities.Sensor;
import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.sql.Date;
import java.util.LinkedList;
import java.util.List;

import java.util.Optional;

public class HumidityMeasurementDAO {

    public HumidityMeasurementDAO(){}

    public Optional<HumidityMeasurement> addMeasurement(Date time, int sensorID, Float value){
        if(value.compareTo((float) 0) < 0){
            return Optional.empty();
        }
        Optional<HumidityMeasurement> result;
        try (Session session = HibernateHelper.getSession()) {
            Transaction tx = session.beginTransaction();
            tx.begin();
            Sensor sensor = session.get(Sensor.class, sensorID);
            System.out.println("ID: " + sensorID);
            if (sensor == null) {
                result = Optional.empty();
            } else {
                HumidityMeasurement measurement = new HumidityMeasurement(time, sensor, value);
                sensor.addMeasurement(measurement);
                session.save(measurement);
                result = Optional.of(measurement);
            }
            tx.commit();
        } catch (Exception ex){
            result = Optional.empty();
        }
        return result;
    }

    public Optional<List<HumidityMeasurement>> getAllMeasurements(){
        Optional<List<HumidityMeasurement>> result;
        List<HumidityMeasurement> measurements = new LinkedList<>();
        try (Session session = HibernateHelper.getSession()) {
            Transaction tx = session.beginTransaction();
            tx.begin();
            Query q = session.createQuery("from HumidityMeasurement");
            for (Object o : q.list()) {
                HumidityMeasurement measurement = (HumidityMeasurement) o;
                Hibernate.initialize(measurement.getSensor());
                measurements.add(measurement);
            }
            result = Optional.of(measurements);
            tx.commit();
        } catch (Exception ex){
            result = Optional.empty();
        }
        return result;
    }

    public Optional<HumidityMeasurement> getLatestMeasurement(int sensorId){
        Optional<HumidityMeasurement> result;
        try (Session session = HibernateHelper.getSession()) {
            Transaction tx = session.beginTransaction();
            tx.begin();
            List resultList = session
                    .createQuery("from HumidityMeasurement as hm WHERE hm.sensor.id = :id order by hm.time desc")
                    .setParameter("id", sensorId)
                    .list();

            if (resultList.size() <= 0) {
                result = Optional.empty();
            } else {
                HumidityMeasurement measurement = (HumidityMeasurement) resultList.get(0);
                Hibernate.initialize(measurement.getSensor());
                result = Optional.of(measurement);
            }
            tx.commit();
        } catch (Exception ex){
            result = Optional.empty();
        }
        return result;
    }
}
