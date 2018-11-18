package model.dao;

import model.entities.Field;
import model.entities.Strategy;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.sql.Time;
import java.util.Date;
import java.util.Optional;

public class StrategyDAO extends SimpleInsertDAO {

    public Optional<Strategy> addStrategy(String name, Time startTime, Time endTime, float minHumidity, float maxHumidity){
        Strategy strategy = new Strategy(name, startTime, endTime, minHumidity, maxHumidity);
        return createEntity(strategy).map(e -> (Strategy) e);
    }

    public Optional<Strategy> addFieldToStrategy(int strategyId, int fieldId){
        Optional<Strategy> result;
        try (Session session = HibernateHelper.getSession()) {
            Transaction tx = session.beginTransaction();
            tx.begin();
            Strategy strategy = session.get(Strategy.class, strategyId);
            Field field = session.get(Field.class, fieldId);
            if (strategy == null || field == null) {
                result = Optional.empty();
            } else {
                field.addStrategy(strategy);
                strategy.addField(field);
                result = Optional.of(strategy);
            }
            tx.commit();
        } catch (Exception ex){
            result = Optional.empty();
        }
        return result;
    }
}
