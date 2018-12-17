package designer.options;

public enum EChangeType {
    all,hasgrid,charttype,
    dimension,measurment,axis,
    filter,
    theme,
    unknown;

    public static EChangeType parse (String type) {
        if (all.name().equalsIgnoreCase(type)) {
            return all;
        } else if (hasgrid.name().equalsIgnoreCase(type)) {
            return hasgrid;
        }else if (charttype.name().equalsIgnoreCase(type)) {
            return charttype;
        }else if (dimension.name().equalsIgnoreCase(type)) {
            return dimension;
        }else if (measurment.name().equalsIgnoreCase(type)) {
            return measurment;
        }else if (axis.name().equalsIgnoreCase(type)) {
            return axis;
        } else if (filter.name().equalsIgnoreCase(type)) {
            return filter;
        }else if (theme.name().equalsIgnoreCase(type)) {
            return theme;
        } else {
            return unknown;
        }

    }
}
