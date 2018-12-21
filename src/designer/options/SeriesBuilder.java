package designer.options;

import designer.options.echart.series.*;

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

    public static Series createBaseDataSeries(EChartType type, String mensurmentName, List<String> mensurmentDataList) {
        Series series = null;
        switch (type) {
            //TODO 未完
            case bar:
                 series = new Bar(mensurmentName);
                 break;
            case map:
                series = new Map(mensurmentName);
                break;
            case pie:
                series = new Pie(mensurmentName);
                break;
            case line:
                series = new Line(mensurmentName);
                break;
            case gauge:
                series = new Gauge(mensurmentName);
                break;
            case graph:
                series = new Graph(mensurmentName);
                break;
            case radar:
                series = new RadarSeries(mensurmentName);
                break;
            case lines:
                series = new Lines(mensurmentName);
                break;
            case funnel:
                series = new Funnel(mensurmentName);
                break;
            case scatter:
                series = new Scatter(mensurmentName);
                break;

            default:
                series = new Bar(mensurmentName);
                break;
        }

        series.data(mensurmentDataList);
        return  series;
    }
}
