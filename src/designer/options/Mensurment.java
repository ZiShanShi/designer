package designer.options;

/**
 * @author kimi
 * @description 度量
 * @date 2018-12-21 11:58
 */


public class Mensurment {
    private String name;
    private EChartType type;

    public Mensurment(EChartType type) {
        this.type = type;
    }

    public Mensurment(String name, EChartType type) {
        this(type);
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Mensurment setName(String name) {
        this.name = name;
        return this;
    }

    public EChartType getType() {
        return type;
    }

    public Mensurment setType(EChartType type) {
        this.type = type;
        return this;
    }
}

