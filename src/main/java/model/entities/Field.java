package model.entities;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "FIELDS")
@JsonIgnoreProperties({ "cannons", "strategies" })
public class Field {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String name;

    @OneToMany(mappedBy = "field", fetch = FetchType.LAZY)
    private Set<WaterCannon> cannons = new HashSet<>();

    @ManyToMany(mappedBy = "applyTo", fetch = FetchType.LAZY)
    private Set<Strategy> strategies = new HashSet<>();

    public Field() {    }

    public Field(String name) {
        this.name = name;
    }

    @JsonCreator
    public Field(
            @JsonProperty("id") Integer id,
            @JsonProperty("fieldName") String name){
        this(name);
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void addCannon(WaterCannon cannon){
        cannons.add(cannon);
    }

    public List<WaterCannon> getCannons(){
        return new LinkedList<>(cannons);
    }

    public Integer getId(){
        return id;
    }

    public void addStrategy(Strategy strategy){
        strategies.add(strategy);
    }

    public List<Strategy> getStrategies(){
        return new LinkedList<>(strategies);
    }

}
