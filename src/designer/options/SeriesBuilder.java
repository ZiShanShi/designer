package designer.options;

import designer.DesignerComponentFactory;
import designer.options.echart.series.*;

import java.util.ArrayList;
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

    public static Series createBaseDataSeries(EChartType type, String measurmentName, List<String> measurmentData) {
        Series defaultTemplate = null;
        try {
            defaultTemplate = getDefaultTemplate(type);
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }

        switch (type) {
            //TODO 未完 暂未知有何不同
            case bar:
                if (defaultTemplate == null) {
                    defaultTemplate = new Bar(measurmentName);
                }
                defaultTemplate.name(measurmentName);
                 break;
            case map:
                if (defaultTemplate == null) {
                    defaultTemplate = new Map(measurmentName);
                }
                defaultTemplate.name(measurmentName);
                 break;
            case pie:
                if (defaultTemplate == null) {
                    defaultTemplate = new Pie(measurmentName);
                }
                defaultTemplate.name(measurmentName);
                break;
            case line:
                if (defaultTemplate == null) {
                    defaultTemplate = new Line(measurmentName);
                }
                defaultTemplate.name(measurmentName);
                break;
            case gauge:
                defaultTemplate.name(measurmentName);
                break;
            case graph:
                if (defaultTemplate == null) {
                    defaultTemplate = new Graph(measurmentName);
                }
                defaultTemplate.name(measurmentName);
                break;
            case radar:
                if (defaultTemplate == null) {
                    defaultTemplate = new RadarSeries(measurmentName);
                }
                defaultTemplate.name(measurmentName);
                break;
            case lines:
                if (defaultTemplate == null) {
                    defaultTemplate = new Lines(measurmentName);
                }
                defaultTemplate.name(measurmentName);
                break;
            case funnel:
                if (defaultTemplate == null) {
                    defaultTemplate = new Funnel(measurmentName);
                }
                defaultTemplate.name(measurmentName);
                break;
            case scatter:
                if (defaultTemplate == null) {
                    defaultTemplate = new Scatter(measurmentName);
                }
                defaultTemplate.name(measurmentName);
                break;

            default:
                defaultTemplate = new Bar(measurmentName);
                break;
        }

        defaultTemplate.data(measurmentData);
        return  defaultTemplate;
    }

    private static Series getDefaultTemplate(EChartType type) throws CloneNotSupportedException {
        ArrayList<Series> defaultSerieList = DesignerComponentFactory.getInstance().getDefautOption().series();
        for (Series series : defaultSerieList) {
            if (series.type().name().equalsIgnoreCase(type.name())) {
                return (Series) series.clone();
            }
        }
        return null;
    }
}
