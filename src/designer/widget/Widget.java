package designer.widget;

import designer.DesignerConstant;
import designer.cache.EOptionSourceType;
import designer.cache.FieldNode;
import designer.cache.ICacheSourceType;
import designer.exception.DesignerOptionException;
import designer.options.*;
import designer.options.echart.Legend;
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
    private transient Map<EAxisPositon, ChartAxis> positionAxisMap;
    private EDataType dataType;
    private String dataName;
    private List<SegmentPart> segmentList;
    private GridOption gridOption;
    private transient ChartOption chartOption;

    private transient  String path;
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
        positionAxisMap = new HashMap<>();
        xmlBeanMap = new HashMap<>();

        xmlBeanMap.put("segmentList", "filters-segment");
        xmlBeanMap.put("chartTypeList", "chartType");
        xmlBeanMap.put("axisList", "axis-one");
    }

    public Widget(String id, String name) {
        this(id);
        this.name = name;
    }

    public String fromWidget2XmlBean(String fieldName) {
        String fixedName = xmlBeanMap.get(fieldName);
        if (Util.isEmptyStr(fixedName)) {
            fixedName = fieldName;
        }
        return fixedName;
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

    public Map<EAxisPositon, ChartAxis> getPositionAxisMap() {
        return positionAxisMap;
    }

    public ChartAxis getAxisFromPosition(EAxisPositon positon) {
        if (positon == null || positionAxisMap == null) {
            return null;
        }
       return positionAxisMap.get(positon);
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
        positionAxisMap.put(chartAxis.getPositon(), chartAxis);
        axisList.add(chartAxis);
        return "axis";
    }

    public List<ChartAxis> getAxisList() {
        return axisList;
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
        return "filters";
    }
    public String putSegmentList(List<SegmentPart> segments) {
        this.segmentList.clear();
        segmentList.addAll(segments);
        return "filters";
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

    public String getPath() {
        return path;
    }

    public Widget setPath(String path) {
        this.path = path;
        return this;
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

    public Widget setAxisList(List<ChartAxis> axisList) {
        this.axisList.clear();
        this.axisList.addAll(axisList);
        return this;
    }

    public void invalidateData() throws Exception {
        String filters = combineFilter();
        //TODO 先不考虑数据权限问题
        EntitySet dataSet = null;
        String orderBy = null;
        if (gridOption != null) {
            List<String> groupList = gridOption.getGroupList();
            orderBy = StringUtils.join(groupList.toArray(), Util.comma);
        }
        //TODO 查数据前 需要把当前维度 关联的主数据字段一起查出来
        if (EDataType.db.equals(dataType)) {
            String groupBy = getGroupBy();

            String fields = combineField();
            dataSet = DataHandler.getDataSet(dataName, fields, filters, groupBy, orderBy);
        } else if (EDataType.sql.equals(dataType)) {
            NamedSQL instance = NamedSQL.getInstance(dataName);
            instance.setParam(DesignerConstant.FILTERS, filters);
            dataSet = SQLRunner.getEntitySet(instance);
        }

        clearOldData();

        loadSeries(dataSet);

        loadGrid(dataSet);
    }

    private String getGroupBy() {
        ContentBuilder builder = new ContentBuilder(Util.comma);
        List<GridField> fieldList = gridOption.getFieldList();
        fieldList.stream().filter(field -> !checkInMeasurmentAxis(field.getField()))
                .map(field -> builder.append(field.getField())).collect(Collectors.toList());


        return builder.toString();
    }

    private <R> R appendGroupBy(ChartAxis chartAxis, ContentBuilder builder) {
        chartAxis.getFieldList().stream().map(field -> builder.append(field.getName())).collect(Collectors.toList());
        return null;
    }

    private void clearOldData() {
        GsonOption chartOption = this.chartOption.getRealChartOption();
        if (chartOption == null) {
            return;
        }
        chartOption.series().clear();
        chartOption.legend().data().clear();
        chartOption.xAxis().clear();
        chartOption.yAxis().clear();
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
        //TODO 返回前面后 这个data需要删除 暂未删除
        gridOption.setData(subData);
    }

    private Object reSetAxis(ChartAxis chartAxis, EntitySet dataSet) {
        EDesignerDataType dataType = chartAxis.getType();
        EAxisPositon positon = chartAxis.getPositon();
        EDimensionAxis axisName = positon.getAxisName();
        GsonOption gsonOption = chartOption.getRealChartOption();
        Axis axis = null;
        switch (dataType) {
            case measurment:
                axis = new ValueAxis();
                if (!Util.isEmptyStr(chartAxis.getName())) {
                    axis.name(chartAxis.getName());
                }

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
                categoryAxis.data(categoryDataList.toArray()).position(positon.name());
                axis = categoryAxis;
                break;
            default:
                break;

        }

        if (chartAxis.getInverse()) {
            axis.inverse(true);
        }

        switch (axisName) {
            case x:
                gsonOption.xAxis(axis);
                break;
            case y:
                gsonOption.yAxis(axis);
            default:
                break;
        }

        return  axisName;
    }

    private Object setMeasurmentSeries(AxisField field, List<String> measurmentData, EDimensionAxis axisName, GsonOption gsonOption) {
        String fieldName = field.getName();
        Series dataSeries = SeriesBuilder.createBaseDataSeries(field.getType(), fieldName, measurmentData);
        switch (axisName) {
            case x:
                dataSeries.yAxisIndex(gsonOption.xAxis().size());
                break;
            case y:
                dataSeries.yAxisIndex(gsonOption.yAxis().size());
                break;
            default:
                break;

        }
        Legend legend = chartOption.getRealChartOption().legend();
        legend.data(fieldName);

        chartOption.getRealChartOption().series(dataSeries);
        return  fieldName;
    }

    private void loadSeries(EntitySet dataSet) {
        axisList.stream()
                .map(chartAxis ->
                        reSetAxis(chartAxis, dataSet)
                ).collect(Collectors.toList());

    }

    private String combineField() {
        ContentBuilder builder = new ContentBuilder(Util.comma);
        List<GridField> gridFieldList = gridOption.getFieldList();
        gridFieldList.stream()
                .filter(field -> !checkInMeasurmentAxis(field.getField()))
                .map(field -> builder.append(field.getField())).collect(Collectors.toList());

        gridFieldList.stream()
                .filter(field -> checkInMeasurmentAxis(field.getField()))
                .map(field -> builder.append(MessageFormat.format("SUM({0}) as {0} ", field.getField()))).collect(Collectors.toList());

        return  builder.toString();
    }

    private boolean checkInAxis(String field) {
        List<ChartAxis> containAxis = axisList.stream()
                .filter(chartAxis -> checkInAxisFieldList(chartAxis, field)).collect(Collectors.toList());
        if (containAxis != null && containAxis.size() > 0) {
            return true;
        }
        return false;
    }

    private boolean checkInMeasurmentAxis(String field) {
        List<ChartAxis> containAxis = axisList.stream()
                .filter(chartAxis -> chartAxis.getType().equals(EDesignerDataType.measurment))
                .filter(chartAxis -> checkInAxisFieldList(chartAxis, field)).collect(Collectors.toList());
        if (containAxis != null && containAxis.size() > 0) {
            return true;
        }
        return false;
    }

    private boolean checkInDimensionAxis(String field) {
        List<ChartAxis> containAxis = axisList.stream()
                .filter(chartAxis -> chartAxis.getType().equals(EDesignerDataType.dimension))
                .filter(chartAxis -> checkInAxisFieldList(chartAxis, field)).collect(Collectors.toList());
        if (containAxis != null && containAxis.size() > 0) {
            return true;
        }
        return false;
    }
    private boolean checkInAxisFieldList(ChartAxis axis, String fieldName) {
        List<AxisField> fieldList = axis.getFieldList();
        List<AxisField> containFields = fieldList.stream().filter(field -> field.getName().equalsIgnoreCase(fieldName))
                .collect(Collectors.toList());
        if (containFields != null && containFields.size() > 0) {
            return true;
        }
        return false;
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

    public Widget putFieldNode(EOptionSourceType type ,FieldNode node) {
        Set<FieldNode> fieldNodes = fieldNodeSourceMap.get(type);
        if (fieldNodes == null) {
            fieldNodes = new HashSet<>();
        }
        fieldNodes.add(node);
        fieldNodeSourceMap.put(type, fieldNodes);
        return this;
    }

    public void combineGridField(List<GridField> gridFieldList) {
        initGridField();
        List<GridField> fieldList = gridOption.getFieldList();
        gridFieldList.stream().filter(field -> fieldList.contains(field))
                .map(field -> updateField(fieldList,field)).collect(Collectors.toList());

        gridFieldList.stream().filter(field -> !fieldList.contains(field))
                .map(field -> fieldList.add(field)).collect(Collectors.toList());
    }

    private <R> R updateField(List<GridField> fieldList, GridField field) {
        int i = fieldList.indexOf(field);
        fieldList.set(i, field);
        return null;
    }

    private void initGridField() {
        axisList.stream().map(chartAxis -> checkField(chartAxis.getFieldList())).collect(Collectors.toList());
    }

    private <R> R checkField(List<AxisField> axisFieldList) {
        List<GridField> fieldList = gridOption.getFieldList();
        for (AxisField axisField : axisFieldList) {
            String name = axisField.getName();
            List<GridField> containList = fieldList.stream().filter(field -> field.getField().equalsIgnoreCase(name)).collect(Collectors.toList());
            if (containList != null && containList.size() > 0) {
                continue;
            }
            GridField gridField = new GridField(name);
            gridField.setCaption(axisField.getCaption());
            fieldList.add(gridField);
        }
        return null;
    }
}
