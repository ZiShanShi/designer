package designer.widget;

public enum EAxisPositon {
    left,right,bottom,top;

    public  String getAxisName(){
        String axis = null;

        switch (this) {
            case bottom:
            case top:
                axis = "x";
                break;
            case left:
            case right:
                axis = "y";
                break;
            default:
                break;

        }
        return axis;
    }
}
