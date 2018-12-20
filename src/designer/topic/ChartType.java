package designer.topic;

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


    public EChartType getType() {
        return type;
    }

    public ChartType setType(EChartType type) {
        this.type = type;
        return this;
    }
}
