package designer.options;

/**
 * @author kimi
 * @description 度量
 * @date 2018-12-21 11:58
 */


public class AxisField {
    private String name;
    private EChartType type;
    private String caption;

    public AxisField(EChartType type) {
        this.type = type;
    }

    public AxisField(String name) {
        this.name = name;
    }

    public AxisField(String name, EChartType type) {
        this(type);
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public AxisField setName(String name) {
        this.name = name;
        return this;
    }

    public EChartType getType() {
        return type;
    }

    public AxisField setType(EChartType type) {
        this.type = type;
        return this;
    }

    public String getCaption() {
        return caption;
    }

    public AxisField setCaption(String caption) {
        this.caption = caption;
        return this;
    }
}

