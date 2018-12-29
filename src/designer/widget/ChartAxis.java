package designer.widget;

import designer.options.AxisField;

import java.util.ArrayList;
import java.util.List;

/**
 * @author kimi
 * @description weidget的坐标轴信息
 * @date 2018-12-27 17:53
 */


public class ChartAxis {
    private EAxisPositon positon;
    private EDesignerDataType type;
    private List<AxisField> fieldList;
    private String name;
    public ChartAxis(EDesignerDataType dataType) {
        this.type = dataType;
    }

    public EAxisPositon getPositon() {
        return positon;
    }

    public ChartAxis setPositon(EAxisPositon positon) {
        this.positon = positon;
        return this;
    }

    public EDesignerDataType getType() {
        return type;
    }

    public ChartAxis setType(EDesignerDataType type) {
        this.type = type;
        return this;
    }

    public List<AxisField> getFieldList() {
        return fieldList;
    }

    public ChartAxis setFieldList(List<AxisField> fieldList) {
        this.fieldList = fieldList;
        return this;
    }

    public ChartAxis putField(AxisField field) {
        if (fieldList == null) {
            fieldList = new ArrayList<>();
        }
        fieldList.add(field);
        return this;
    }

    public String getName() {
        return name;
    }

    public ChartAxis setName(String name) {
        this.name = name;
        return this;
    }
}
