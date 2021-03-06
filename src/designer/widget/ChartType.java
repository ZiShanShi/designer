package designer.widget;

import designer.options.EChartType;

/**
 * @author kimi
 * @description 图表类型
 * @date 2018-12-19 10:55
 */


public class ChartType {
    private Integer minDimensionNum;
    private Integer maxDimensionNum;
    private Integer minMensurmentNum;
    private Integer maxMensurmentNum;
    private EChartType type;
    private Boolean isOpen;

    public ChartType(){this.type = EChartType.bar;}

    public ChartType(EChartType type) {
        this.type = type;
    }

    public Integer getMinDimensionNum() {
        return minDimensionNum;
    }

    public ChartType setMinDimensionNum(Integer minDimensionNum) {
        this.minDimensionNum = minDimensionNum;
        return this;
    }

    public Integer getMaxDimensionNum() {
        return maxDimensionNum;
    }

    public ChartType setMaxDimensionNum(Integer maxDimensionNum) {
        this.maxDimensionNum = maxDimensionNum;
        return this;
    }

    public Integer getMinMensurmentNum() {
        return minMensurmentNum;
    }

    public ChartType setMinMensurmentNum(Integer minMensurmentNum) {
        this.minMensurmentNum = minMensurmentNum;
        return this;
    }

    public Integer getMaxMensurmentNum() {
        return maxMensurmentNum;
    }

    public ChartType setMaxMensurmentNum(Integer maxMensurmentNum) {
        this.maxMensurmentNum = maxMensurmentNum;
        return this;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public ChartType setOpen(boolean open) {
        isOpen = open;
        return this;
    }

    public EChartType getType() {
        return type;
    }

    public ChartType setType(EChartType type) {
        this.type = type;
        return this;
    }

    @Override
    public int hashCode() {
        return type.hashCode() + isOpen.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ChartType) {
            return this.hashCode() == obj.hashCode();
        }
        return super.equals(obj);
    }
}
