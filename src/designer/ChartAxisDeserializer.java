

package designer;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import designer.exception.DesignerBaseException;
import designer.options.AxisField;
import designer.widget.*;

import java.lang.reflect.Type;
import java.util.List;

/**
 * @author kimi
 */
public class ChartAxisDeserializer implements JsonDeserializer<ChartAxis> {


    @Override
    public ChartAxis deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        if(jsonElement == null){
            return null;
        } else {
            try {
                JsonObject jsonObject = jsonElement.getAsJsonObject();

                JsonElement positionEle = jsonObject.get(DesignerConstant.keyelement_position);
                JsonElement typeEle = jsonObject.get(DesignerConstant.keyword_type);
                JsonElement axisEle = jsonObject.get(DesignerConstant.fix_element_Axis);
                ChartAxis chartAxis;
                if (typeEle == null) {
                    chartAxis = new ChartAxis(EDesignerDataType.measurment);
                } else {
                    chartAxis = new ChartAxis(EDesignerDataType.valueOf(typeEle.getAsString()));
                }
                EAxisPositon position;
                if (positionEle != null) {
                    position = EAxisPositon.valueOf(positionEle.getAsString());
                    chartAxis.setPositon(position);

                } else if (axisEle != null) {
                    EDimensionAxis axis = EDimensionAxis.valueOf(axisEle.getAsString());
                    position = axis.getDefaultPosition();
                    chartAxis.setPositon(position).setAxis(axis);
                } else {
                    throw new DesignerBaseException("参数错误至少含有axis 或者position");
                }

                chartAxis.setAxis(position.getAxisName());

                if (typeEle == null) {
                    chartAxis.setType(position.getDataType());
                }

                JsonElement nameEle = jsonObject.get(DesignerConstant.keyword_name);
                if (nameEle != null) {
                    String name = nameEle.getAsString();
                    chartAxis.setName(name);
                }

                JsonElement fieldEle = jsonObject.get(DesignerConstant.Grid_element_fields);
                if (fieldEle != null) {
                    JsonArray fieldArray = fieldEle.getAsJsonArray();
                    Gson gson = new Gson();
                    List<AxisField> fieldList = gson.fromJson(fieldArray, new TypeToken<List<AxisField>>() {
                    }.getType());

                    chartAxis.setFieldList(fieldList);
                }

                return  chartAxis;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
    }
}
