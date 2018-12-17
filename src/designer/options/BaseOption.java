package designer.options;

import designer.engine.DesignContext;
import foundation.variant.Segment;

import java.util.List;

/**
 * @author kimi
 * @description 基本图表options
 * @date 2018-12-17 16:49
 */


public class BaseOption implements IOption{

    private Topic topic;


    public void BaseOption(EChangeType type, DesignContext context) {

        if (EChangeType.all.equals(type)) {
            reCreateOption(context);
        } else if (EChangeType.hasgrid.equals(type)) {
            boolean hasGrid = context.isHasGrid();
            toggleGridOption(hasGrid);
        } else if (EChangeType.charttype.equals(type)) {
            EChartType chartType = context.getChartType();
            changeChartType(chartType);
        } else if (EChangeType.dimension.equals(type)) {
            List<String> dimensionList = context.getDimensionList();
            changeDimension(dimensionList);
        }else if (EChangeType.measurment.equals(type)) {
            List<String> measurmentList = context.getMeasurmentList();
            changeMeasurment(measurmentList);
        }else if (EChangeType.dimension.equals(type)) {
            List<String> measurmentList = context.getMeasurmentList();
            List<String> dimensionList = context.getDimensionList();
            changeAxis(dimensionList,measurmentList);
        }else if (EChangeType.filter.equals(type)) {
            List<Segment> filterSegmentList = context.getFilterSegmentList();
            reloadChartFilter(filterSegmentList);
        } else if (EChangeType.theme.equals(type)) {
            EDesignerChartTheme theme = context.getTheme();
            changeOptionTheme(theme);
        }

    }

    private void changeOptionTheme(EDesignerChartTheme theme) {

    }

    private void reloadChartFilter(List<Segment> filterSegmentList) {
    }

    private void changeAxis(List<String> dimensionList, List<String> measurmentList) {
        changeDimension(dimensionList);
        changeMeasurment(measurmentList);
    }

    private void changeMeasurment(List<String> measurmentList) {
    }

    private void changeDimension(List<String> dimensionList) {
    }

    private void changeChartType(EChartType chartType) {

    }

    private void toggleGridOption(boolean hasGrid) {
    }

    private void reCreateOption(DesignContext context) {

    }

    public void BaseOption(DesignContext context) {
        EChangeType type = EChangeType.all;
        BaseOption(type, context);
    }

    @Override
    public String getTopic() {
        return null;
    }
}
