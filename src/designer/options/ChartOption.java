package designer.options;

import designer.DesignerComponentFactory;
import designer.DesignerUtil;
import designer.EChangeType;
import designer.engine.DesignContext;
import designer.options.echart.json.GsonOption;
import designer.options.echart.json.GsonUtil;
import designer.widget.theme.Theme;
import foundation.variant.Segment;
import net.sf.json.JSONObject;

import java.util.List;

/**
 * @author kimi
 * @description 基本图表options
 * @date 2018-12-17 16:49
 */


public class ChartOption implements IOption{
    private transient String topicId;
    private GsonOption realChartOption;
    private Theme theme;
    private transient boolean isLink;
    private transient String path;
    public ChartOption() {
        this.realChartOption = DesignerComponentFactory.getInstance().getDefautOption();


    }

    public ChartOption(String topicId) {
        this.topicId = topicId;
        this.realChartOption = DesignerComponentFactory.getInstance().getDefautOption();
    }

    public ChartOption(GsonOption realChartOption) {
        this.realChartOption = realChartOption;
    }

    public JSONObject parseJson() {
        JSONObject jsonObject = new JSONObject();
        String format = GsonUtil.format(realChartOption);
        if (theme == null) {
            return JSONObject.fromObject(format);
        }
        String themeName = theme.getName();
        JSONObject realChartJSONObject = DesignerUtil.combineTheme(format, themeName);
        jsonObject.put("theme", themeName);
        jsonObject.put("option", realChartJSONObject);
        return jsonObject;
    }

    public void operator(EChangeType type, DesignContext context) {

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
            String theme = context.getTheme();
            changeOptionTheme(theme);
        }

    }

    private void changeOptionTheme(String theme) {
        Theme changedTheme = DesignerComponentFactory.getInstance().getThemeByName(theme);
        this.theme = changedTheme;
        //重组options
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

    public void operator(DesignContext context) {
        EChangeType type = EChangeType.all;
        operator(type, context);
    }

    public GsonOption getRealChartOption() {
        return realChartOption;
    }

    public ChartOption setRealChartOption(GsonOption realChartOption) {
        this.realChartOption = realChartOption;
        return this;
    }

    public Theme getTheme() {
        return theme;
    }

    public ChartOption setTheme(Theme theme) {
        this.theme = theme;
        return this;
    }

    public boolean isLink() {
        return isLink;
    }

    public ChartOption setLink(boolean link) {
        isLink = link;
        return this;
    }

    public String getTopicId() {
        return topicId;
    }

    public ChartOption setTopicId(String topicId) {
        this.topicId = topicId;
        return this;
    }

    public String getPath() {
        return path;
    }

    public ChartOption setPath(String path) {
        this.path = path;
        return this;
    }
}
