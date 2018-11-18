package model.entities;


import com.fasterxml.jackson.annotation.JsonCreator;

import javax.persistence.*;
import java.sql.Date;

@Entity
@Table(name = "HUMIDITY_MEASUREMENTS")
public class HumidityMeasurement {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(name = "TIME")
    private Date time;

    private Float value;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SENSOR_FK")
    private Sensor sensor;

    public HumidityMeasurement() {    }

    public HumidityMeasurement(Date time, Sensor sensor, Float value) {
        this.time = time;
        this.sensor = sensor;
        this.value = value;
    }

    public HumidityMeasurement(Integer id, Date time, Sensor sensor, Float value) {
        this(time, sensor, value);
        this.id = id;
    }


    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public Sensor getSensor() {
        return sensor;
    }

    public void setSensor(Sensor sensor) {
        this.sensor = sensor;
    }

    public Float getValue() {
        return value;
    }

    public void setValue(Float value) {
        this.value = value;
    }

    public Integer getId(){
        return id;
    }


}
