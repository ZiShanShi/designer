package designer.widget;

public enum EDesignerDataType {
    dimension,measurment,agg;

    public String toAxis() {
        switch (this) {
            case dimension:
                return "x";
            case measurment:
            case agg:
                return "y";

            default:
                break;

        }
        return null;
    }
}
