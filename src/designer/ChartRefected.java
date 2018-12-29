package designer;



/**
 * @author kimi
 * @description Option 反射的类型包装类
 * @date 2018-12-19 16:34
 */


public class ChartRefected {
    private boolean isSimple;
    private boolean isList = false;
    private Class<?> chartComponentClaz;
    private Object componentBean;
    private Object listParentObj;

    public ChartRefected(boolean isSimple, Class<?> chartComponentClaz, Object componentBean) {
        this(chartComponentClaz, componentBean);
        this.isSimple = isSimple;
    }

    public ChartRefected(Class<?> chartComponentClaz, Object componentBean) {
        this(chartComponentClaz);
        this.componentBean = componentBean;

    }public ChartRefected(Class<?> chartComponentClaz) {
        this.chartComponentClaz = chartComponentClaz;
    }

    public boolean isSimple() {
        return isSimple;
    }

    public ChartRefected setSimple(boolean simple) {
        isSimple = simple;
        return this;
    }

    public Class<?> getChartComponentClaz() {
        return chartComponentClaz;
    }

    public ChartRefected setChartComponentClaz(Class<?> chartComponentClaz) {
        this.chartComponentClaz = chartComponentClaz;
        return this;
    }

    public Object getComponentBean() {
        return componentBean;
    }

    public ChartRefected setComponentBean(Object componentBean) {
        this.componentBean = componentBean;
        return this;
    }

    public boolean isList() {
        return isList;
    }

    public ChartRefected setList(boolean list) {
        isList = list;
        return this;
    }

    public void setListParentObj(Object listParentObj) {
        this.listParentObj = listParentObj;
    }

    public Object getListParentObj() {
        return listParentObj;
    }
}
