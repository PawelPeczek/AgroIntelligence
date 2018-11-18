package json_api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import model.entities.HumidityMeasurement;

import java.sql.Date;


public class HumidityWrapper {

    private final Integer id;
    @JsonFormat
            (shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    private final Date time;
    private final Integer sensorId;
    private final Float value;

    @JsonCreator
    public HumidityWrapper(
            @JsonProperty("id") Integer id,
            @JsonProperty("time") Date time,
            @JsonProperty("sensorId") Integer sensorId,
            @JsonProperty("value") Float value) {
        this.time = time;
        this.sensorId = sensorId;
        this.value = value;
        this.id = id;
    }

    public static HumidityWrapper wrapHumidity(HumidityMeasurement measurement){
        return new HumidityWrapper(
                measurement.getId(),
                measurement.getTime(),
                measurement.getSensor().getId(),
                measurement.getValue()
        );
    }

    public Integer getId() {
        return id;
    }

    public Date getTime() {
        return time;
    }

    public Integer getSensorId() {
        return sensorId;
    }

    public Float getValue() {
        return value;
    }
}
