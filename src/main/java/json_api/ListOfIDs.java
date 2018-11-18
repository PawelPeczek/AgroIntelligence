package json_api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class ListOfIDs {
    private final List<Integer> entities;

    @JsonCreator
    public ListOfIDs(@JsonProperty("ids") List<Integer> fields){
        this.entities = fields;
    }

    public List<Integer> getEntities() {
        return entities;
    }
}
