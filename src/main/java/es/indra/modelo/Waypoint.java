package es.indra.modelo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.camel.util.json.JsonObject;
@Data @NoArgsConstructor @AllArgsConstructor
public class Waypoint
{
    public String serieDrone;
    public long timestamp;
    public double coordX;
    public double coordY;

    public String toString()
    {
        JsonObject jsonObject = new JsonObject();
        jsonObject.put("serieDrone",serieDrone);
        jsonObject.put("timestamp",timestamp);
        jsonObject.put("coordX",coordX);
        jsonObject.put("coordY",coordY);

        String rta = jsonObject.toString();

        return  rta;
    }
}
