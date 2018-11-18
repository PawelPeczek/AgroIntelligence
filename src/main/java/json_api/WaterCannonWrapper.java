package json_api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import model.entities.HumidityMeasurement;
import model.entities.Sensor;
import model.entities.WaterCannon;
import java.util.List;

import java.util.Date;

public class WaterCannonWrapper {


    private final Integer fieldId;
    private final Integer id;

    @JsonCreator
    public WaterCannonWrapper(
            @JsonProperty("cannonId") Integer id,
            @JsonProperty("fieldId") Integer fieldId) {
        this.fieldId = fieldId;
        this.id = id;
    }

    public static WaterCannonWrapper wrapWaterCannon(WaterCannon cannon){
        return new WaterCannonWrapper(
            cannon.getId(),
            cannon.getField().getId()
        );
    }


    public Integer getFieldId() {
        return fieldId;
    }

    public Integer getId() {
        return id;
    }
}
