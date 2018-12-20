package designer.topic;

import designer.DesignerConstant;
import designer.options.ChartOption;
import designer.options.EChartType;
import designer.options.GridOption;
import foundation.data.EntitySet;
import foundation.persist.sql.NamedSQL;
import foundation.persist.sql.SQLRunner;
import foundation.util.ContentBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * @author kimi
 * @description topic bean
 * @date 2018-12-19 10:54
 */


public class Topic {
    private String id;
    private String name;

    private boolean hasGrid;
    private List<ChartType> chartTypeList;
    private EChartType defaultType;
    private List<String> dimensionList;
    private List<String> mensurmentList;
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

    public List<String> getMensurmentList() {
        return mensurmentList;
    }

    public Topic setMensurmentList(List<String> mensurmentList) {
        this.mensurmentList = mensurmentList;
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

    public void invalidate() throws Exception {
        String filters = combineFilter();
        //TODO 先不管聚合数据 仅仅普通   聚合数据需要考虑数据权限问题

        if (EDataType.db.equals(dataType)) {

        } else if (EDataType.sql.equals(dataType)) {
            NamedSQL instance = NamedSQL.getInstance(dataName);
            instance.setParam(DesignerConstant.FILTERS, filters);
            EntitySet entitySet = SQLRunner.getEntitySet(instance);
        }
    }

    private String combineFilter() {
        ContentBuilder builder = new ContentBuilder(DesignerConstant.AND);
        for (SegmentPart segmentPart : segmentList) {
            builder.append(segmentPart.toString());
        }
        return builder.toString();
    }
}
