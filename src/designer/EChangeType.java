package designer;

public enum EChangeType {
    all,
    hasgrid,
    axis,// 字段数据的增减
    inverseX,inverseY, inversePositon, inverse, // 坐标轴位置的互换
    gridField, //表格展示字段
    filters, //筛选条件 与前面展示有联动
    theme, // 主题切换
    chartOption, // 具体配置
    unknown;

    public static EChangeType parse (String type) {
        if (all.name().equalsIgnoreCase(type)) {
            return all;
        } else if (hasgrid.name().equalsIgnoreCase(type)) {
            return hasgrid;
        } else if (axis.name().equalsIgnoreCase(type)) {
            return axis;
        } else if (filters.name().equalsIgnoreCase(type)) {
            return filters;
        }else if (theme.name().equalsIgnoreCase(type)) {
            return theme;
        }else if (inverse.name().equalsIgnoreCase(type)) {
            return inverse;
        }else if (inverseY.name().equalsIgnoreCase(type)) {
            return inverseY;
        }else if (gridField.name().equalsIgnoreCase(type)) {
            return gridField;
        }else if (inverseX.name().equalsIgnoreCase(type)) {
            return inverseX;
        }else if (chartOption.name().equalsIgnoreCase(type)) {
            return chartOption;
        }
        else {
            return unknown;
        }
    }
}
