package designer.widget;

public enum EAxisPositon {
    left,right,bottom,top;

    public  EDimensionAxis getAxisName(){
        EDimensionAxis axis = null;
        switch (this) {
            case bottom:
            case top:
                axis = EDimensionAxis.x;
                break;
            case left:
            case right:
                axis = EDimensionAxis.y;
                break;
            default:
                break;

        }
        return axis;
    }

    public  EDesignerDataType getDataType(){
        EDesignerDataType dataType = null;
        switch (this) {
            case bottom:
            case top:
                dataType = EDesignerDataType.dimension;
                break;
            case left:
            case right:
                dataType = EDesignerDataType.measurment;
                break;
            default:
                break;

        }
        return dataType;
    }
}
