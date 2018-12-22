package designer.options;

import designer.options.echart.series.*;
import foundation.util.Util;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * @author kimi
 * @description series 生成器
 * @date 2018-12-17 17:11
 * //
 */


public class SeriesBuilder {

    public static SeriesBuilder builder;

    public static SeriesBuilder getInstance() {
        if (builder != null) {
            synchronized (SeriesBuilder.class) {
                if (builder != null) {
                    builder = new SeriesBuilder();
                }
            }
        }
        return builder;
    }

    public static Series createBaseDataSeries(EChartType type, String measurmentName, List<String> measurmentDataList) {
        Series series = null;
        switch (type) {
            //TODO 未完
            case bar:
                 series = new Bar(measurmentName);
                 break;
            case map:
                series = new Map(measurmentName);
                break;
            case pie:
                series = new Pie(measurmentName);
                break;
            case line:
                series = new Line(measurmentName);
                break;
            case gauge:
                series = new Gauge(measurmentName);
                break;
            case graph:
                series = new Graph(measurmentName);
                break;
            case radar:
                series = new RadarSeries(measurmentName);
                break;
            case lines:
                series = new Lines(measurmentName);
                break;
            case funnel:
                series = new Funnel(measurmentName);
                break;
            case scatter:
                series = new Scatter(measurmentName);
                break;

            default:
                series = new Bar(measurmentName);
                break;
        }

        series.data(StringUtils.join(measurmentDataList.toArray(), Util.comma));
        return  series;
    }
}
