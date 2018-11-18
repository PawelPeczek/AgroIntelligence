package model.entities;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "SENSORS")
@JsonIgnoreProperties({ "cannons", "measurements" })
public class Sensor {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "CANNONS_TO_SENSORS",
            joinColumns = { @JoinColumn(name = "SENSOR_ID") },
            inverseJoinColumns = { @JoinColumn(name = "CANNON_ID") }
    )
    private Set<WaterCannon> cannons = new HashSet<>();

    @OneToMany(mappedBy = "sensor", fetch = FetchType.LAZY)
    private Set<HumidityMeasurement> measurements = new HashSet<>();

    public Sensor() {    }

    @JsonCreator
    public Sensor(@JsonProperty("id") Integer id){
        this.id = id;
    }

    public List<WaterCannon> getCannons() {
        return new LinkedList<>(cannons);
    }

    public void addCannon(WaterCannon cannon){
        cannons.add(cannon);
    }

    @JsonIgnore
    public List<HumidityMeasurement> getAllMeasurements(){
        return new LinkedList<>(measurements);
    }

    public void addMeasurement(HumidityMeasurement measurement){
        measurements.add(measurement);
    }

    public Integer getId(){
        return id;
    }


}
