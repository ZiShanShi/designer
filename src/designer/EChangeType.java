package designer;

public enum EChangeType {
    all,hasgrid,charttype,
    dimension,measurment,axis,
    inverseX,inverseY,inverse,
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
        }else if (inverse.name().equalsIgnoreCase(type)) {
            return inverse;
        }else if (inverseY.name().equalsIgnoreCase(type)) {
            return inverseY;
        }else if (inverseX.name().equalsIgnoreCase(type)) {
            return inverseX;
        }
        else {
            return unknown;
        }
    }
}
