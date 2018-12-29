

package designer.options;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import foundation.data.Entity;
import foundation.persist.Field;
import net.sf.json.JSONObject;

import java.lang.reflect.Type;
import java.util.List;

/**
 * @author kimi
 */
public class GridDataDeserializer implements JsonSerializer<Entity> {


    @Override
    public JsonElement serialize(Entity entity, Type type, JsonSerializationContext jsonSerializationContext) {
        JSONObject oneEntity = new JSONObject();
        List<Field> fields = entity.getTableMeta().getFields();
        for (Field field : fields) {
            String name = field.getName();
            String value = entity.getString(name);
            if (value == null) {
                continue;
            }
            oneEntity.put(name, value);
        }
        return  new JsonPrimitive(oneEntity.toString());
    }
}
