package model.entities;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.sql.Time;
import java.util.*;

@Entity
@Table(name = "STRATEGIES")
@JsonIgnoreProperties(value = {"applyTo"})
public class Strategy {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String name;

    @JsonIgnore
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "STARTEGIES_TO_FIELDS",
            joinColumns = { @JoinColumn(name = "STRATEGY_ID") },
            inverseJoinColumns = { @JoinColumn(name = "FIELD_ID") }
    )
    private Set<Field> applyTo = new HashSet<>();

    @Column(name = "START_TIME")
    private Time startTime;

    @Column(name = "END_TIME")
    private Time endTime;

    private Float minHumidity;

    private Float maxHumidity;

    public Strategy() {    }

    public Strategy(String name, Time startTime, Time endTime, Float minHumidity, Float maxHumidity) {
        this.name = name;
        this.startTime = startTime;
        this.endTime = endTime;
        this.minHumidity = minHumidity;
        this.maxHumidity = maxHumidity;
    }

    @JsonCreator
    public Strategy(
            @JsonProperty("id") Integer id,
            @JsonProperty("name") String name,
            @JsonProperty("startTime") Time startTime,
            @JsonProperty("endTime") Time endTime,
            @JsonProperty("minHumidity") Float minHumidity,
            @JsonProperty("maxHumidity") Float maxHumidity) {
        this(name, startTime, endTime, minHumidity, maxHumidity);
        this.id = id;
    }

        public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Time getStartTime() {
        return startTime;
    }

    public void setStartTime(Time startTime) {
        this.startTime = startTime;
    }

    public Time getEndTime() {
        return endTime;
    }

    public void setEndTime(Time endTime) {
        this.endTime = endTime;
    }

    public Float getMinHumidity() {
        return minHumidity;
    }

    public void setMinHumidity(Float minHumidity) {
        this.minHumidity = minHumidity;
    }

    public Float getMaxHumidity() {
        return maxHumidity;
    }

    public void setMaxHumidity(Float maxHumidity) {
        this.maxHumidity = maxHumidity;
    }
    @JsonIgnore
    public List<Field> getAllFields(){
        return new LinkedList<>(applyTo);
    }

    public Integer getId(){
        return id;
    }

    public void addField(Field field){
        applyTo.add(field);
    }
}
