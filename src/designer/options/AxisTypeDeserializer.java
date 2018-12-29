

package designer.options;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import designer.widget.EDesignerDataType;

import java.lang.reflect.Type;

/**
 * @author kimi
 */
public class AxisTypeDeserializer implements JsonSerializer<EDesignerDataType> {


    @Override
    public JsonElement serialize(EDesignerDataType eDesignerDataType, Type type, JsonSerializationContext jsonSerializationContext) {

        return  new JsonPrimitive(eDesignerDataType.toAxis());

    }
}
