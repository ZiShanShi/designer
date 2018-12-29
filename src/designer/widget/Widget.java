package designer.widget;

import designer.DesignerConstant;
import designer.cache.EOptionSourceType;
import designer.cache.FieldNode;
import designer.cache.ICacheSourceType;
import designer.exception.DesignerOptionException;
import designer.options.*;
import designer.options.echart.Option;
import designer.options.echart.axis.Axis;
import designer.options.echart.axis.CategoryAxis;
import designer.options.echart.axis.ValueAxis;
import designer.options.echart.json.GsonOption;
import designer.options.echart.json.GsonUtil;
import designer.options.echart.series.Series;
import foundation.data.Entity;
import foundation.data.EntitySet;
import foundation.data.Page;
import foundation.persist.DataHandler;
import foundation.persist.sql.NamedSQL;
import foundation.persist.sql.SQLRunner;
import foundation.util.ContentBuilder;
import foundation.util.Util;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;

import java.text.MessageFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author kimi
 * @description widget bean
 * @date 2018-12-19 10:54
 */


public class Widget implements ICacheSourceType {
    private String id;
    private String name;
    private boolean hasGrid;
    private List<ChartType> chartTypeList;
    private EChartType defaultType;
    private List<ChartAxis> axisList;


    private EDataType dataType;
    private String dataName;
    private List<SegmentPart> segmentList;
    private transient ChartOption chartOption;
    private GridOption gridOption;
    private transient Map<EOptionSourceType,Set<FieldNode>> fieldNodeSourceMap;
    private transient Map<FieldNode,EOptionSourceType> nodeSourceTypeMap;
    private transient Map<String,String> xmlBeanMap;


    public Widget(String id) {
        this.id = id;
        chartTypeList = new ArrayList<>();
        axisList = new ArrayList<>();
        segmentList = new ArrayList<>();
        fieldNodeSourceMap = new HashMap<>();
        nodeSourceTypeMap = new HashMap<>();
        xmlBeanMap = new HashMap<>();

        xmlBeanMap.put("filters","segmentList");
        xmlBeanMap.put("chartType","chartTypeList");
        xmlBeanMap.put("axis","axisList");

    }

    public Widget(String id, String name) {
        this(id);
        this.name = name;
    }


    public String putSegment(SegmentPart segmentPart) {
        if (segmentList == null) {
            segmentList = new ArrayList<>();
        }
        segmentList.add(segmentPart);
        return "segmentList";
    }
    public Widget putFieldNodeSourceSet(EOptionSourceType type , Set<FieldNode> fieldNodeSourceSet) {
        fieldNodeSourceMap.put(type, fieldNodeSourceSet);
        for (FieldNode fieldNode : fieldNodeSourceSet) {
            nodeSourceTypeMap.put(fieldNode,type);
        }
        return this;
    }
    public Map<EOptionSourceType, Set<FieldNode>> getFieldNodeSourceMap() {
        return fieldNodeSourceMap;
    }

    public Widget setFieldNodeSourceMap(Map<EOptionSourceType, Set<FieldNode>> fieldNodeSourceMap) {
        this.fieldNodeSourceMap = fieldNodeSourceMap;
        return this;
    }

    @Override
    public EOptionSourceType getTypeFromNode(FieldNode node) {
        return  nodeSourceTypeMap.get(node);
    }

    @Override
    public Set<FieldNode> getFieldNodeSet(EOptionSourceType type) {
        Set<FieldNode> fieldNodes = fieldNodeSourceMap.get(type);
        if (fieldNodes == null) {
            fieldNodes = new HashSet<>();
        }
        fieldNodeSourceMap.put(type, fieldNodes);
        return fieldNodes;
    }

    public String getId() {
        return id;
    }

    public String setId(String id) {
        this.id = id;
        return "id";
    }

    public String getName() {
        return name;
    }

    public String setName(String name) {
        this.name = name;
        return "name";
    }

    public boolean isHasGrid() {
        return hasGrid;
    }

    public String setHasGrid(boolean hasGrid) {
        this.hasGrid = hasGrid;
        return "hasGrid";
    }

    public List<ChartType> getChartTypeList() {
        return chartTypeList;
    }

    public String setChartTypeList(List<ChartType> chartTypeList) {
        this.chartTypeList = chartTypeList;
        return "chartTypeList";
    }


    public String putAxises(ChartAxis chartAxis) {
        if (axisList == null) {
            axisList = new ArrayList<>();
        }

        axisList.add(chartAxis);
        return "axisList";
    }

    public EDataType getDataType() {
        return dataType;
    }

    public String setDataType(EDataType dataType) {
        this.dataType = dataType;
        return "dataType";
    }

    public String getDataName() {
        return dataName;
    }

    public String setDataName(String dataName) {
        this.dataName = dataName;
        return "dataName";
    }

    public List<SegmentPart> getSegmentList() {
        return segmentList;
    }

    public String setSegmentList(List<SegmentPart> segmentList) {
        this.segmentList = segmentList;
        return "segmentList";
    }

    public ChartOption getChartOption() {
        return chartOption;
    }

    public String setChartOption(ChartOption chartOption) {
        this.chartOption = chartOption;
        return "chartOption";
    }

    public GridOption getGridOption() {
        return gridOption;
    }

    public String setGridOption(GridOption gridOption) {
        this.gridOption = gridOption;
        return "gridOption";
    }

    public EChartType getDefaultType() {
        return defaultType;
    }

    public String setDefaultType(EChartType defaultType) {
        this.defaultType = defaultType;
        return "defaultType";
    }



    public void invalidate()  {
        try {

            //2.1 加载标题,数据
            Option realChartOption = chartOption.getRealChartOption();
            if (!Util.isEmptyStr(name)) {
                realChartOption.title().text(name);
            }

            invalidateData();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void invalidateData()throws Exception {
        String filters = combineFilter();
        //TODO 先不考虑数据权限问题
        EntitySet dataSet = null;
        String groupBy = null;
        if (gridOption != null) {
            List<String> groupList = gridOption.getGroupList();
            groupBy = StringUtils.join(groupList.toArray(), Util.comma);
        }
        //TODO 查数据前 需要把当前维度 关联的主数据字段一起查出来
        if (EDataType.db.equals(dataType)) {
            String segment = "customercode";//StringUtils.join(dimensionList.toArray(), Util.comma)

            String fields = combineField(axisList);
            dataSet = DataHandler.getDataSet(dataName, fields, filters, segment, groupBy);
        } else if (EDataType.sql.equals(dataType)) {
            NamedSQL instance = NamedSQL.getInstance(dataName);
            instance.setParam(DesignerConstant.FILTERS, filters);
            dataSet = SQLRunner.getEntitySet(instance);
        }

        loadSeries(dataSet);

        loadGrid(dataSet);
    }


    private void loadGrid(EntitySet dataSet) {
        //TODO 未完成
        Page page = gridOption.getPage();
        if (page == null) {
            throw new DesignerOptionException("gridoption page is null");
        }
        page.setRecordCount(dataSet.size());
        page.setPageNo(1);
        //dataset 分页

        List<Entity> subData = dataSet.getSubData(page.getBeginRecordNo_1(), page.getEndRecordNo());
        gridOption.setData(subData);
    }

    private Object reSetAxis(ChartAxis chartAxis, EntitySet dataSet) {
        EDesignerDataType dataType = chartAxis.getType();
        EAxisPositon positon = chartAxis.getPositon();
        String axisName = positon.getAxisName();
        GsonOption gsonOption = chartOption.getRealChartOption();
        Axis axis = null;
        switch (dataType) {
            case measurment:
                axis = new ValueAxis();
                if (!Util.isEmptyStr(chartAxis.getName())) {
                    axis.name(chartAxis.getName());
                }
                int index = 0;

                chartAxis.getFieldList().stream()
                        .map(field -> setMeasurmentSeries(field,dataSet.getFieldList(field.getName()),axisName,gsonOption)).collect(Collectors.toList());

                break;
            case dimension:
                ArrayList<String> categoryDataList = new ArrayList<>();
                for (Entity entity : dataSet) {
                    List<AxisField> fieldList = chartAxis.getFieldList();
                    ContentBuilder builder = new ContentBuilder(Util.String_Escape_newLine);
                    for (AxisField axisField : fieldList) {
                        String name = axisField.getName();
                        String value = entity.getString(name);
                        builder.append(value);
                    }
                    categoryDataList.add(builder.toString());
                }

                CategoryAxis categoryAxis = new CategoryAxis();
                categoryAxis.data(categoryDataList.toArray()    ).position(positon.name());
                axis = categoryAxis;
                break;
            default:
                break;

        }
        if ("x".equalsIgnoreCase(axisName)) {
            gsonOption.xAxis(axis);
        } else if ("y".equalsIgnoreCase(axisName)) {
            gsonOption.yAxis(axis);
        }
        return  axisName;
    }

    private Object setMeasurmentSeries(AxisField field, List<String> measurmentData, String axisName, GsonOption gsonOption) {
        String fieldName = field.getName();
        Series dataSeries = SeriesBuilder.createBaseDataSeries(field.getType(), fieldName, measurmentData);

        if ("x".equalsIgnoreCase(axisName)) {
            dataSeries.yAxisIndex(gsonOption.xAxis().size());
        } else if ("y".equalsIgnoreCase(axisName)) {
            dataSeries.yAxisIndex(gsonOption.yAxis().size());
        }

        chartOption.getRealChartOption().legend().data(fieldName);
        chartOption.getRealChartOption().series(dataSeries);
        return  fieldName;
    }

    private void loadSeries(EntitySet dataSet) {
        Option realChartOption = chartOption.getRealChartOption();

        realChartOption.xAxis().clear();
        realChartOption.yAxis().clear();
        List<Object> collect = axisList.stream()
                .map(chartAxis ->
                        reSetAxis(chartAxis, dataSet)
                ).collect(Collectors.toList());


    }

    private String combineField(List<ChartAxis> chartAxisList) {
        ContentBuilder builder = new ContentBuilder(Util.comma);
        for (ChartAxis chartAxis : chartAxisList) {
            EDesignerDataType dataType = chartAxis.getType();
            List<AxisField> fieldList = chartAxis.getFieldList();
            switch (dataType) {
                case dimension:
                    for (AxisField axisField : fieldList) {
                        builder.append(axisField.getName());
                    }
                    break;
                case measurment:
                    for (AxisField axisField : fieldList) {
                        builder.append(MessageFormat.format("SUM({0}) as {0} ", axisField.getName()));
                    }
                    break;

                default:
                    break;

            }
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

    public JSONObject parseJson() {
        String format = GsonUtil.format(this);
        JSONObject topicJsonObject = JSONObject.fromObject(format);
        topicJsonObject.put("chartoption", chartOption.parseJson());
        return  topicJsonObject;
    }

}
