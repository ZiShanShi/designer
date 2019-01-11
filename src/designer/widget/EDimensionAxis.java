package designer.widget;

public enum EDimensionAxis {
    x,y;

    public EAxisPositon getDefaultPosition() {
        EAxisPositon positon = null;
        switch (this) {
            case x:
                positon =  EAxisPositon.bottom;
                break;
            case y:
                positon =  EAxisPositon.left;
                break;
            default:
                break;

        }
        return positon;
    }
}
