package model.entities;

import java.util.LinkedList;
import java.util.List;
import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "WATER_CANNONS")
public class WaterCannon {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "FIELD_FK")
    private Field field;

    @ManyToMany(mappedBy = "cannons", fetch = FetchType.LAZY)
    private Set<Sensor> sensors = new HashSet<>();


    public WaterCannon() {    }

    public WaterCannon(Field field) {
        this.field = field;
    }

    public Field getField() {
        return field;
    }

    public void setField(Field field) {
        this.field = field;
    }

    public Integer getId(){
        return id;
    }

    public List<Sensor> getAllSensors(){
        return new LinkedList<>(sensors);
    }

    public void addSensor(Sensor sensor){
        sensors.add(sensor);
    }

}
