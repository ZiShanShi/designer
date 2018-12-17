

package designer.options.echart.series;

import designer.options.echart.code.SeriesType;
import lombok.Getter;
import lombok.Setter;

/**
 * 雷达图
 *
 * @author liuxu
 * @date 18-7-10下午2:37
 */

public class RadarSeries extends Series<RadarSeries> {

    /**
     * 构造函数
     */
    public RadarSeries() {
        this.type(SeriesType.radar);
    }

    /**
     * 构造函数,参数:name
     *
     * @param name
     */
    public RadarSeries(String name) {
        super(name);
        this.type(SeriesType.radar);
    }

}
