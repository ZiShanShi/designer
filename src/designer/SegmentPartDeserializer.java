

package designer;

import com.google.gson.*;
import designer.widget.EFilterLink;
import designer.widget.ESqlValueType;
import designer.widget.SegmentPart;

import java.lang.reflect.Type;

/**
 * @author kimi
 */
public class SegmentPartDeserializer implements JsonDeserializer<SegmentPart> {


    @Override
    public SegmentPart deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        if(jsonElement == null){
            return null;
        } else {
            try {
                JsonObject jsonObject = jsonElement.getAsJsonObject();
                String name = jsonObject.get("name").getAsString();
                String value = jsonObject.get("value").getAsString();
                SegmentPart segmentPart = new SegmentPart();
                segmentPart.setName(name).setValue(value);
                JsonElement linkEle = jsonObject.get("link");
                JsonElement typeEle = jsonObject.get("type");
                if (linkEle != null) {
                    EFilterLink link = EFilterLink.parse(linkEle.getAsString());
                    segmentPart.setLink(link);
                }

                if (typeEle != null) {
                    ESqlValueType valueType = ESqlValueType.valueOf(typeEle.getAsString());
                    segmentPart.setValueType(valueType);
                }

                return  segmentPart;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
    }
}
