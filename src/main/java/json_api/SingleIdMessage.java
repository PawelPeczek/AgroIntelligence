package json_api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class SingleIdMessage {
    protected final Integer id_one;

    @JsonCreator
    public SingleIdMessage(@JsonProperty("primaryId") Integer id_one){
        this.id_one = id_one;
    }

    public Integer getId_one() {
        return id_one;
    }
}
