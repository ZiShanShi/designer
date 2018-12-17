

package designer.options.echart.series;

import designer.options.echart.AbstractData;
import designer.options.echart.style.ItemStyle;
import lombok.Getter;
import lombok.Setter;

/**
 * Description: MarkPoint
 *
 * @author kimi
 */

public class MarkPoint extends AbstractData<MarkPoint> {
    /**
     * 标注类型
     *
     * @see designer.options.echart.series.Series#symbol
     */
    private Object symbol;
    /**
     * 标注大小
     *
     * @see designer.options.echart.series.Series#symbolSize
     */
    private Object symbolSize;
    /**
     * 标注图形旋转角度
     *
     * @see designer.options.echart.series.Series#symbolRoate
     */
    private Object symbolRoate;
    /**
     * 是否启动大规模标注模式
     */
    private Boolean large;
    /**
     * 标注图形炫光特效
     *
     * @see designer.options.echart.series.Effect
     */
    private Effect effect;
    /**
     * 标注图形样式属性
     *
     * @see designer.options.echart.series.Series#itemStyle
     */
    private ItemStyle itemStyle;
    /**
     * 地图特有，标注图形定位坐标
     *
     * @see designer.options.echart.series.Map#geoCoord
     */
    private GeoCoord geoCoord;

    /**
     * 设置effect值
     *
     * @param effect
     */
    public MarkPoint effect(Effect effect) {
        this.effect = effect;
        return this;
    }

    /**
     * 设置itemStyle值
     *
     * @param itemStyle
     */
    public MarkPoint itemStyle(ItemStyle itemStyle) {
        this.itemStyle = itemStyle;
        return this;
    }

    /**
     * 获取symbol值
     */
    public Object symbol() {
        return this.symbol;
    }

    /**
     * 设置symbol值
     *
     * @param symbol
     */
    public MarkPoint symbol(Object symbol) {
        this.symbol = symbol;
        return this;
    }

    /**
     * 获取symbolSize值
     */
    public Object symbolSize() {
        return this.symbolSize;
    }

    /**
     * 设置symbolSize值
     *
     * @param symbolSize
     */
    public MarkPoint symbolSize(Object symbolSize) {
        this.symbolSize = symbolSize;
        return this;
    }

    /**
     * 获取symbolRoate值
     */
    public Object symbolRoate() {
        return this.symbolRoate;
    }

    /**
     * 设置symbolRoate值
     *
     * @param symbolRoate
     */
    public MarkPoint symbolRoate(Object symbolRoate) {
        this.symbolRoate = symbolRoate;
        return this;
    }

    /**
     * 获取large值
     */
    public Boolean large() {
        return this.large;
    }

    /**
     * 设置large值
     *
     * @param large
     */
    public MarkPoint large(Boolean large) {
        this.large = large;
        return this;
    }

    /**
     * 标注图形炫光特效
     *
     * @see designer.options.echart.series.Effect
     */
    public Effect effect() {
        if (this.effect == null) {
            this.effect = new Effect();
        }
        return this.effect;
    }

    /**
     * 标线图形样式属性
     *
     * @see designer.options.echart.style.ItemStyle
     * @see designer.options.echart.series.Series#itemStyle
     */
    public ItemStyle itemStyle() {
        if (this.itemStyle == null) {
            this.itemStyle = new ItemStyle();
        }
        return this.itemStyle;
    }

    /**
     * 获取geoCoord值
     */
    public GeoCoord geoCoord() {
        if (this.geoCoord == null) {
            this.geoCoord = new GeoCoord();
        }
        return this.geoCoord;
    }

    /**
     * 设置name,x,y值
     *
     * @param name
     * @param x
     * @param y
     */
    public MarkPoint geoCoord(String name, String x, String y) {
        this.geoCoord().put(name, x, y);
        return this;
    }
}
