package designer.topic;

import designer.DesignerConstant;
import designer.DesignerUtil;
import designer.options.*;
import designer.options.echart.Option;
import designer.options.echart.axis.CategoryAxis;
import designer.options.echart.axis.ValueAxis;
import designer.options.echart.series.Series;
import foundation.data.Entity;
import foundation.data.EntitySet;
import foundation.persist.DataHandler;
import foundation.persist.sql.NamedSQL;
import foundation.persist.sql.SQLRunner;
import foundation.util.ContentBuilder;
import foundation.util.Util;
import org.apache.commons.lang3.StringUtils;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author kimi
 * @description topic bean
 * @date 2018-12-19 10:54
 */


public class Topic {
    private String id;
    private String name;
    private EDimensonAxis dimensonAxis;
    private boolean hasGrid;
    private List<ChartType> chartTypeList;
    private EChartType defaultType;
    private List<String> dimensionList;
    private List<Mensurment> mensurmentList;
    private EDataType dataType;
    private String dataName;
    private List<SegmentPart> segmentList;
    private ChartOption chartOption;
    private GridOption gridOption;

    public Topic(String id) {
        this.id = id;
        chartTypeList = new ArrayList<>();
        dimensionList = new ArrayList<>();
        mensurmentList = new ArrayList<>();
        segmentList = new ArrayList<>();
    }

    public Topic(String id, String name) {
        this(id);
        this.name = name;
    }

    public void putMensurment(Mensurment mensurment) {
        if (mensurmentList == null) {
            mensurmentList = new ArrayList<>();
        }
        mensurmentList.add(mensurment);
    }

    public void putSegment(SegmentPart segmentPart) {
        if (segmentList == null) {
            segmentList = new ArrayList<>();
        }
        segmentList.add(segmentPart);
    }

    public String getId() {
        return id;
    }

    public Topic setId(String id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public Topic setName(String name) {
        this.name = name;
        return this;
    }

    public boolean isHasGrid() {
        return hasGrid;
    }

    public Topic setHasGrid(boolean hasGrid) {
        this.hasGrid = hasGrid;
        return this;
    }

    public List<ChartType> getChartTypeList() {
        return chartTypeList;
    }

    public Topic setChartTypeList(List<ChartType> chartTypeList) {
        this.chartTypeList = chartTypeList;
        return this;
    }

    public List<String> getDimensionList() {
        return dimensionList;
    }

    public Topic setDimensionList(List<String> dimensionList) {
        this.dimensionList = dimensionList;
        return this;
    }


    public EDataType getDataType() {
        return dataType;
    }

    public Topic setDataType(EDataType dataType) {
        this.dataType = dataType;
        return this;
    }

    public String getDataName() {
        return dataName;
    }

    public Topic setDataName(String dataName) {
        this.dataName = dataName;
        return this;
    }

    public List<SegmentPart> getSegmentList() {
        return segmentList;
    }

    public Topic setSegmentList(List<SegmentPart> segmentList) {
        this.segmentList = segmentList;
        return this;
    }

    public ChartOption getChartOption() {
        return chartOption;
    }

    public Topic setChartOption(ChartOption chartOption) {
        this.chartOption = chartOption;
        return this;
    }

    public GridOption getGridOption() {
        return gridOption;
    }

    public Topic setGridOption(GridOption gridOption) {
        this.gridOption = gridOption;
        return this;
    }

    public EChartType getDefaultType() {
        return defaultType;
    }

    public Topic setDefaultType(EChartType defaultType) {
        this.defaultType = defaultType;
        return this;
    }

    public EDimensonAxis getDimensonAxis() {
        return dimensonAxis;
    }

    public Topic setDimensonAxis(EDimensonAxis dimensonAxis) {
        this.dimensonAxis = dimensonAxis;
        return this;
    }

    public void invalidate()  {
        try {
            //1 组合 完整的options
            DesignerUtil.combine(chartOption);

            //2 加载数据
            Option realChartOption = chartOption.getRealChartOption();
            invalidateData(realChartOption);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void invalidateData(Option realChartOption)throws Exception {
        String filters = combineFilter();
        //TODO 先不考虑数据权限问题
        EntitySet dataSet = null;
        if (EDataType.db.equals(dataType)) {
            String fields = combineField(dimensionList, mensurmentList);
            dataSet = DataHandler.getDataSet(dataName, fields, filters, StringUtils.join(dimensionList.toArray(), Util.comma),StringUtils.join(dimensionList.toArray(), Util.comma));
        } else if (EDataType.sql.equals(dataType)) {
            NamedSQL instance = NamedSQL.getInstance(dataName);
            instance.setParam(DesignerConstant.FILTERS, filters);
            dataSet = SQLRunner.getEntitySet(instance);
        }

        loadSeries(realChartOption, dataSet);

        loadGrid(dataSet);
    }

    private void loadGrid(EntitySet dataSet) {
        //TODO 未完成
    }

    private void loadSeries(Option realChartOption, EntitySet dataSet) {
        HashMap<String, List<String>> mensurmentListMap = new HashMap<>();
        List<String> categoryValueList = new ArrayList<>();
        ArrayList<Series> dataSerieList = new ArrayList<>();

        for (Mensurment mensurment : mensurmentList) {
            ArrayList<String> mensurmentDataList = new ArrayList<>();
            mensurmentListMap.put(mensurment.getName(), mensurmentDataList);
        }

        for (Entity entity : dataSet) {
            ContentBuilder builder = new ContentBuilder(Util.String_Escape_newLine);
            for (String dimension : dimensionList) {
                String dimensionValue = entity.getString(dimension);
                builder.append(dimensionValue);
            }

            for (Mensurment mensurment : mensurmentList) {
                String mensurmentName = mensurment.getName();
                List<String> mensurmentDataList = mensurmentListMap.get(mensurmentName);
                String mensurmentValue = entity.getString(mensurmentName);
                mensurmentDataList.add(mensurmentValue);
            }

            categoryValueList.add(builder.toString());
        }
        CategoryAxis categoryAxis = new CategoryAxis();
        categoryAxis.data(categoryValueList.toArray());

        if (EDimensonAxis.x.equals(dimensonAxis)) {
            realChartOption.xAxis(categoryAxis);
            realChartOption.yAxis(new ValueAxis());
        } else {
            realChartOption.yAxis(categoryAxis);
            realChartOption.xAxis(new ValueAxis());
        }

        realChartOption.legend().data(mensurmentList);

        for (Mensurment mensurment : mensurmentList) {
            EChartType type = mensurment.getType();
            String mensurmentName = mensurment.getName();
            List<String> mensurmentDataList = mensurmentListMap.get(mensurmentName);

            Series dataSeries = SeriesBuilder.createBaseDataSeries(type, mensurmentName, mensurmentDataList);
            //TODO series 样式如何加
            dataSerieList.add(dataSeries);
        }

        realChartOption.series(dataSerieList);
    }

    private String combineField(List<String> dimensionList, List<Mensurment> mensurmentList) {
        ContentBuilder builder = new ContentBuilder(Util.comma);
        for (String dimension : dimensionList) {
            builder.append(dimension);
        }
        for (Mensurment mensurment : mensurmentList) {
            builder.append(MessageFormat.format("SUM({0}) as {0} ",mensurment.getName()));
        }
        return  builder.toString();
    }

    private String combineFilter() {
        ContentBuilder builder = new ContentBuilder(DesignerConstant.AND);
        for (SegmentPart segmentPart : segmentList) {
            builder.append(segmentPart.toString());
        }
        return builder.toString();
    }
}
