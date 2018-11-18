package json_api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class DoubleIDMessage extends SingleIdMessage {

    protected final Integer od_two;

    @JsonCreator
    public DoubleIDMessage(@JsonProperty("primaryId") Integer id_one, @JsonProperty("secondaryId") Integer id_two){
        super(id_one);
        this.od_two = id_two;
    }

    public Integer getOd_two() {
        return od_two;
    }

}
