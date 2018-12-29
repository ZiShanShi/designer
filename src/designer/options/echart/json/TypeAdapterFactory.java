package designer.options.echart.json;

import com.google.gson.*;
import designer.DesignerConstant;
import designer.options.echart.code.TriggerOn;

import java.lang.reflect.Type;

/**
 * @author kimi
 * @description
 * @date 2018-12-25 9:49
 */


public class TypeAdapterFactory implements JsonSerializer<TriggerOn>,JsonDeserializer<TriggerOn>{


    @Override
    public TriggerOn deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        String triggonOnString = jsonElement.getAsString();
        for (String key : DesignerConstant.fixJson2J.keySet()) {
            int i = triggonOnString.indexOf(key);
            if (i != -1) {
                String replaceValue = DesignerConstant.fixJson2J.get(key);
                triggonOnString = triggonOnString.replace(key,replaceValue);
            }
        }
        return TriggerOn.valueOf(triggonOnString);
    }

    @Override
    public JsonElement serialize(TriggerOn triggerOn, Type type, JsonSerializationContext jsonSerializationContext) {
        return new JsonPrimitive(triggerOn.toString());
    }
}
