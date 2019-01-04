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
}
