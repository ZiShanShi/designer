package designer.widget.theme;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @author kimi
 * @description theme
 * @date 2018-12-24 15:19
 */


public class CommonTheme {
    private String color;
    /**
     * color : ["#d87c7c","#919e8b","#d7ab82","#6e7074","#61a0a8","#efa18d","#787464","#cc7e63","#724e58","#4b565b"]
     * backgroundColor : rgba(254,248,239,1)
     * textStyle : {}
     * title : {"textStyle":{"color":"#333333"},"subtextStyle":{"color":"#aaa"}}
     * line : {"itemStyle":{"normal":{"borderWidth":1}},"lineStyle":{"normal":{"width":2}},"symbolSize":4,"symbol":"emptyCircle","smooth":false}
     * radar : {"itemStyle":{"normal":{"borderWidth":1}},"lineStyle":{"normal":{"width":2}},"symbolSize":4,"symbol":"emptyCircle","smooth":false}
     * bar : {"itemStyle":{"normal":{"barBorderWidth":0,"barBorderColor":"#ccc"},"emphasis":{"barBorderWidth":0,"barBorderColor":"#ccc"}}}
     * pie : {"itemStyle":{"normal":{"borderWidth":0,"borderColor":"#ccc"},"emphasis":{"borderWidth":0,"borderColor":"#ccc"}}}
     * scatter : {"itemStyle":{"normal":{"borderWidth":0,"borderColor":"#ccc"},"emphasis":{"borderWidth":0,"borderColor":"#ccc"}}}
     * boxplot : {"itemStyle":{"normal":{"borderWidth":0,"borderColor":"#ccc"},"emphasis":{"borderWidth":0,"borderColor":"#ccc"}}}
     * parallel : {"itemStyle":{"normal":{"borderWidth":0,"borderColor":"#ccc"},"emphasis":{"borderWidth":0,"borderColor":"#ccc"}}}
     * sankey : {"itemStyle":{"normal":{"borderWidth":0,"borderColor":"#ccc"},"emphasis":{"borderWidth":0,"borderColor":"#ccc"}}}
     * funnel : {"itemStyle":{"normal":{"borderWidth":0,"borderColor":"#ccc"},"emphasis":{"borderWidth":0,"borderColor":"#ccc"}}}
     * gauge : {"itemStyle":{"normal":{"borderWidth":0,"borderColor":"#ccc"},"emphasis":{"borderWidth":0,"borderColor":"#ccc"}}}
     * candlestick : {"itemStyle":{"normal":{"color":"#c23531","color0":"#314656","borderColor":"#c23531","borderColor0":"#314656","borderWidth":1}}}
     * graph : {"itemStyle":{"normal":{"borderWidth":0,"borderColor":"#ccc"}},"lineStyle":{"normal":{"width":1,"color":"#aaa"}},"symbolSize":4,"symbol":"emptyCircle","smooth":false,"color":["#d87c7c","#919e8b","#d7ab82","#6e7074","#61a0a8","#efa18d","#787464","#cc7e63","#724e58","#4b565b"],"label":{"normal":{"textStyle":{"color":"#eee"}}}}
     * map : {"itemStyle":{"normal":{"areaColor":"#eeeeee","borderColor":"#444444","borderWidth":0.5},"emphasis":{"areaColor":"rgba(255,215,0,0.8)","borderColor":"#444444","borderWidth":1}},"label":{"normal":{"textStyle":{"color":"#000000"}},"emphasis":{"textStyle":{"color":"rgb(100,0,0)"}}}}
     * geo : {"itemStyle":{"normal":{"areaColor":"#eeeeee","borderColor":"#444444","borderWidth":0.5},"emphasis":{"areaColor":"rgba(255,215,0,0.8)","borderColor":"#444444","borderWidth":1}},"label":{"normal":{"textStyle":{"color":"#000000"}},"emphasis":{"textStyle":{"color":"rgb(100,0,0)"}}}}
     * categoryAxis : {"axisLine":{"show":true,"lineStyle":{"color":"#333"}},"axisTick":{"show":true,"lineStyle":{"color":"#333"}},"axisLabel":{"show":true,"textStyle":{"color":"#333"}},"splitLine":{"show":false,"lineStyle":{"color":["#ccc"]}},"splitArea":{"show":false,"areaStyle":{"color":["rgba(250,250,250,0.3)","rgba(200,200,200,0.3)"]}}}
     * valueAxis : {"axisLine":{"show":true,"lineStyle":{"color":"#333"}},"axisTick":{"show":true,"lineStyle":{"color":"#333"}},"axisLabel":{"show":true,"textStyle":{"color":"#333"}},"splitLine":{"show":true,"lineStyle":{"color":["#ccc"]}},"splitArea":{"show":false,"areaStyle":{"color":["rgba(250,250,250,0.3)","rgba(200,200,200,0.3)"]}}}
     * logAxis : {"axisLine":{"show":true,"lineStyle":{"color":"#333"}},"axisTick":{"show":true,"lineStyle":{"color":"#333"}},"axisLabel":{"show":true,"textStyle":{"color":"#333"}},"splitLine":{"show":true,"lineStyle":{"color":["#ccc"]}},"splitArea":{"show":false,"areaStyle":{"color":["rgba(250,250,250,0.3)","rgba(200,200,200,0.3)"]}}}
     * timeAxis : {"axisLine":{"show":true,"lineStyle":{"color":"#333"}},"axisTick":{"show":true,"lineStyle":{"color":"#333"}},"axisLabel":{"show":true,"textStyle":{"color":"#333"}},"splitLine":{"show":true,"lineStyle":{"color":["#ccc"]}},"splitArea":{"show":false,"areaStyle":{"color":["rgba(250,250,250,0.3)","rgba(200,200,200,0.3)"]}}}
     * toolbox : {"iconStyle":{"normal":{"borderColor":"#999999"},"emphasis":{"borderColor":"#666666"}}}
     * legend : {"textStyle":{"color":"#333333"}}
     * tooltip : {"axisPointer":{"lineStyle":{"color":"#cccccc","width":1},"crossStyle":{"color":"#cccccc","width":1}}}
     * timeline : {"lineStyle":{"color":"#293c55","width":1},"itemStyle":{"normal":{"color":"#293c55","borderWidth":1},"emphasis":{"color":"#a9334c"}},"controlStyle":{"normal":{"color":"#293c55","borderColor":"#293c55","borderWidth":0.5},"emphasis":{"color":"#293c55","borderColor":"#293c55","borderWidth":0.5}},"checkpointStyle":{"color":"#e43c59","borderColor":"rgba(194,53,49,0.5)"},"label":{"normal":{"textStyle":{"color":"#293c55"}},"emphasis":{"textStyle":{"color":"#293c55"}}}}
     * visualMap : {"color":["#bf444c","#d88273","#f6efa6"]}
     * dataZoom : {"backgroundColor":"rgba(47,69,84,0)","dataBackgroundColor":"rgba(47,69,84,0.3)","fillerColor":"rgba(167,183,204,0.4)","handleColor":"#a7b7cc","handleSize":"100%","textStyle":{"color":"#333333"}}
     * markPoint : {"label":{"normal":{"textStyle":{"color":"#eee"}},"emphasis":{"textStyle":{"color":"#eee"}}}}
     */

    private String backgroundColor;
    private TextStyleBean textStyle;
    private TitleBean title;
    private LineBean line;
    private RadarBean radar;
    private BarBean bar;
    private PieBean pie;
    private ScatterBean scatter;
    private BoxplotBean boxplot;
    private ParallelBean parallel;
    private SankeyBean sankey;
    private FunnelBean funnel;
    private GaugeBean gauge;
    private CandlestickBean candlestick;
    private GraphBean graph;
    private MapBean map;
    private GeoBean geo;
    private CategoryAxisBean categoryAxis;
    private ValueAxisBean valueAxis;
    private LogAxisBean logAxis;
    private TimeAxisBean timeAxis;
    private ToolboxBean toolbox;
    private LegendBean legend;
    private TooltipBean tooltip;
    private TimelineBean timeline;
    private VisualMapBean visualMap;
    private DataZoomBean dataZoom;
    private MarkPointBean markPoint;
    @SerializedName("color")
    private List<String> colorX;

    public String getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(String backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public TextStyleBean getTextStyle() {
        return textStyle;
    }

    public void setTextStyle(TextStyleBean textStyle) {
        this.textStyle = textStyle;
    }

    public TitleBean getTitle() {
        return title;
    }

    public void setTitle(TitleBean title) {
        this.title = title;
    }

    public LineBean getLine() {
        return line;
    }

    public void setLine(LineBean line) {
        this.line = line;
    }

    public RadarBean getRadar() {
        return radar;
    }

    public void setRadar(RadarBean radar) {
        this.radar = radar;
    }

    public BarBean getBar() {
        return bar;
    }

    public void setBar(BarBean bar) {
        this.bar = bar;
    }

    public PieBean getPie() {
        return pie;
    }

    public void setPie(PieBean pie) {
        this.pie = pie;
    }

    public ScatterBean getScatter() {
        return scatter;
    }

    public void setScatter(ScatterBean scatter) {
        this.scatter = scatter;
    }

    public BoxplotBean getBoxplot() {
        return boxplot;
    }

    public void setBoxplot(BoxplotBean boxplot) {
        this.boxplot = boxplot;
    }

    public ParallelBean getParallel() {
        return parallel;
    }

    public void setParallel(ParallelBean parallel) {
        this.parallel = parallel;
    }

    public SankeyBean getSankey() {
        return sankey;
    }

    public void setSankey(SankeyBean sankey) {
        this.sankey = sankey;
    }

    public FunnelBean getFunnel() {
        return funnel;
    }

    public void setFunnel(FunnelBean funnel) {
        this.funnel = funnel;
    }

    public GaugeBean getGauge() {
        return gauge;
    }

    public void setGauge(GaugeBean gauge) {
        this.gauge = gauge;
    }

    public CandlestickBean getCandlestick() {
        return candlestick;
    }

    public void setCandlestick(CandlestickBean candlestick) {
        this.candlestick = candlestick;
    }

    public GraphBean getGraph() {
        return graph;
    }

    public void setGraph(GraphBean graph) {
        this.graph = graph;
    }

    public MapBean getMap() {
        return map;
    }

    public void setMap(MapBean map) {
        this.map = map;
    }

    public GeoBean getGeo() {
        return geo;
    }

    public void setGeo(GeoBean geo) {
        this.geo = geo;
    }

    public CategoryAxisBean getCategoryAxis() {
        return categoryAxis;
    }

    public void setCategoryAxis(CategoryAxisBean categoryAxis) {
        this.categoryAxis = categoryAxis;
    }

    public ValueAxisBean getValueAxis() {
        return valueAxis;
    }

    public void setValueAxis(ValueAxisBean valueAxis) {
        this.valueAxis = valueAxis;
    }

    public LogAxisBean getLogAxis() {
        return logAxis;
    }

    public void setLogAxis(LogAxisBean logAxis) {
        this.logAxis = logAxis;
    }

    public TimeAxisBean getTimeAxis() {
        return timeAxis;
    }

    public void setTimeAxis(TimeAxisBean timeAxis) {
        this.timeAxis = timeAxis;
    }

    public ToolboxBean getToolbox() {
        return toolbox;
    }

    public void setToolbox(ToolboxBean toolbox) {
        this.toolbox = toolbox;
    }

    public LegendBean getLegend() {
        return legend;
    }

    public void setLegend(LegendBean legend) {
        this.legend = legend;
    }

    public TooltipBean getTooltip() {
        return tooltip;
    }

    public void setTooltip(TooltipBean tooltip) {
        this.tooltip = tooltip;
    }

    public TimelineBean getTimeline() {
        return timeline;
    }

    public void setTimeline(TimelineBean timeline) {
        this.timeline = timeline;
    }

    public VisualMapBean getVisualMap() {
        return visualMap;
    }

    public void setVisualMap(VisualMapBean visualMap) {
        this.visualMap = visualMap;
    }

    public DataZoomBean getDataZoom() {
        return dataZoom;
    }

    public void setDataZoom(DataZoomBean dataZoom) {
        this.dataZoom = dataZoom;
    }

    public MarkPointBean getMarkPoint() {
        return markPoint;
    }

    public void setMarkPoint(MarkPointBean markPoint) {
        this.markPoint = markPoint;
    }

    public List<String> getColorX() {
        return colorX;
    }

    public void setColorX(List<String> colorX) {
        this.colorX = colorX;
    }

    public static class TextStyleBean {
    }

    public static class TitleBean {
        /**
         * textStyle : {"color":"#333333"}
         * subtextStyle : {"color":"#aaa"}
         */

        private CommonTheme textStyle;
        private CommonTheme subtextStyle;

        public CommonTheme getTextStyle() {
            return textStyle;
        }

        public void setTextStyle(CommonTheme textStyle) {
            this.textStyle = textStyle;
        }

        public CommonTheme getSubtextStyle() {
            return subtextStyle;
        }

        public void setSubtextStyle(CommonTheme subtextStyle) {
            this.subtextStyle = subtextStyle;
        }
    }

    public static class LineBean {
        /**
         * itemStyle : {"normal":{"borderWidth":1}}
         * lineStyle : {"normal":{"width":2}}
         * symbolSize : 4
         * symbol : emptyCircle
         * smooth : false
         */

        private ItemStyleBean itemStyle;
        private LineStyleBean lineStyle;
        private int symbolSize;
        private String symbol;
        private boolean smooth;

        public ItemStyleBean getItemStyle() {
            return itemStyle;
        }

        public void setItemStyle(ItemStyleBean itemStyle) {
            this.itemStyle = itemStyle;
        }

        public LineStyleBean getLineStyle() {
            return lineStyle;
        }

        public void setLineStyle(LineStyleBean lineStyle) {
            this.lineStyle = lineStyle;
        }

        public int getSymbolSize() {
            return symbolSize;
        }

        public void setSymbolSize(int symbolSize) {
            this.symbolSize = symbolSize;
        }

        public String getSymbol() {
            return symbol;
        }

        public void setSymbol(String symbol) {
            this.symbol = symbol;
        }

        public boolean isSmooth() {
            return smooth;
        }

        public void setSmooth(boolean smooth) {
            this.smooth = smooth;
        }

        public  class ItemStyleBean {
            /**
             * normal : {"borderWidth":1}
             */

            private NormalTheme normal;

            public NormalTheme getNormal() {
                return normal;
            }

            public void setNormal(NormalTheme normal) {
                this.normal = normal;
            }

        }

        public static class LineStyleBean {
            /**
             * normal : {"width":2}
             */

            private NormalTheme normal;

            public NormalTheme getNormal() {
                return normal;
            }

            public void setNormal(NormalTheme normal) {
                this.normal = normal;
            }


        }
    }

    public static class RadarBean {
        /**
         * itemStyle : {"normal":{"borderWidth":1}}
         * lineStyle : {"normal":{"width":2}}
         * symbolSize : 4
         * symbol : emptyCircle
         * smooth : false
         */

        private ItemStyleTheme itemStyle;
        private ItemStyleTheme lineStyle;
        private int symbolSize;
        private String symbol;
        private boolean smooth;

        public ItemStyleTheme getItemStyle() {
            return itemStyle;
        }

        public void setItemStyle(ItemStyleTheme itemStyle) {
            this.itemStyle = itemStyle;
        }

        public ItemStyleTheme getLineStyle() {
            return lineStyle;
        }

        public void setLineStyle(ItemStyleTheme lineStyle) {
            this.lineStyle = lineStyle;
        }

        public int getSymbolSize() {
            return symbolSize;
        }

        public void setSymbolSize(int symbolSize) {
            this.symbolSize = symbolSize;
        }

        public String getSymbol() {
            return symbol;
        }

        public void setSymbol(String symbol) {
            this.symbol = symbol;
        }

        public boolean isSmooth() {
            return smooth;
        }

        public void setSmooth(boolean smooth) {
            this.smooth = smooth;
        }

    }

    public static class BarBean {
        /**
         * itemStyle : {"normal":{"barBorderWidth":0,"barBorderColor":"#ccc"},"emphasis":{"barBorderWidth":0,"barBorderColor":"#ccc"}}
         */

        private ItemStyleTheme itemStyle;

        public ItemStyleTheme getItemStyle() {
            return itemStyle;
        }

        public void setItemStyle(ItemStyleTheme itemStyle) {
            this.itemStyle = itemStyle;
        }

    }

    public static class PieBean {
        /**
         * itemStyle : {"normal":{"borderWidth":0,"borderColor":"#ccc"},"emphasis":{"borderWidth":0,"borderColor":"#ccc"}}
         */

        private ItemStyleTheme itemStyle;

        public ItemStyleTheme getItemStyle() {
            return itemStyle;
        }

        public void setItemStyle(ItemStyleTheme itemStyle) {
            this.itemStyle = itemStyle;
        }

    }

    public static class ScatterBean {
        /**
         * itemStyle : {"normal":{"borderWidth":0,"borderColor":"#ccc"},"emphasis":{"borderWidth":0,"borderColor":"#ccc"}}
         */

        private ItemStyleTheme itemStyle;

        public ItemStyleTheme getItemStyle() {
            return itemStyle;
        }

        public void setItemStyle(ItemStyleTheme itemStyle) {
            this.itemStyle = itemStyle;
        }

    }

    public static class BoxplotBean {
        /**
         * itemStyle : {"normal":{"borderWidth":0,"borderColor":"#ccc"},"emphasis":{"borderWidth":0,"borderColor":"#ccc"}}
         */

        private ItemStyleTheme itemStyle;

        public ItemStyleTheme getItemStyle() {
            return itemStyle;
        }

        public void setItemStyle(ItemStyleTheme itemStyle) {
            this.itemStyle = itemStyle;
        }
    }

    public static class ParallelBean {
        /**
         * itemStyle : {"normal":{"borderWidth":0,"borderColor":"#ccc"},"emphasis":{"borderWidth":0,"borderColor":"#ccc"}}
         */

        private ItemStyleTheme itemStyle;

        public ItemStyleTheme getItemStyle() {
            return itemStyle;
        }

        public void setItemStyle(ItemStyleTheme itemStyle) {
            this.itemStyle = itemStyle;
        }

    }

    public static class SankeyBean {
        /**
         * itemStyle : {"normal":{"borderWidth":0,"borderColor":"#ccc"},"emphasis":{"borderWidth":0,"borderColor":"#ccc"}}
         */

        private ItemStyleTheme itemStyle;

        public ItemStyleTheme getItemStyle() {
            return itemStyle;
        }

        public void setItemStyle(ItemStyleTheme itemStyle) {
            this.itemStyle = itemStyle;
        }

    }

    public static class FunnelBean {
        /**
         * itemStyle : {"normal":{"borderWidth":0,"borderColor":"#ccc"},"emphasis":{"borderWidth":0,"borderColor":"#ccc"}}
         */

        private ItemStyleTheme itemStyle;

        public ItemStyleTheme getItemStyle() {
            return itemStyle;
        }

        public void setItemStyle(ItemStyleTheme itemStyle) {
            this.itemStyle = itemStyle;
        }


    }

    public static class GaugeBean {
        /**
         * itemStyle : {"normal":{"borderWidth":0,"borderColor":"#ccc"},"emphasis":{"borderWidth":0,"borderColor":"#ccc"}}
         */

        private ItemStyleTheme itemStyle;

        public ItemStyleTheme getItemStyle() {
            return itemStyle;
        }

        public void setItemStyle(ItemStyleTheme itemStyle) {
            this.itemStyle = itemStyle;
        }


    }

    public static class CandlestickBean {
        /**
         * itemStyle : {"normal":{"color":"#c23531","color0":"#314656","borderColor":"#c23531","borderColor0":"#314656","borderWidth":1}}
         */

        private ItemStyleTheme itemStyle;

        public ItemStyleTheme getItemStyle() {
            return itemStyle;
        }

        public void setItemStyle(ItemStyleTheme itemStyle) {
            this.itemStyle = itemStyle;
        }

    }

    public static class GraphBean {
        /**
         * itemStyle : {"normal":{"borderWidth":0,"borderColor":"#ccc"}}
         * lineStyle : {"normal":{"width":1,"color":"#aaa"}}
         * symbolSize : 4
         * symbol : emptyCircle
         * smooth : false
         * color : ["#d87c7c","#919e8b","#d7ab82","#6e7074","#61a0a8","#efa18d","#787464","#cc7e63","#724e58","#4b565b"]
         * label : {"normal":{"textStyle":{"color":"#eee"}}}
         */

        private ItemStyleTheme itemStyle;
        private LineStyleTheme lineStyle;
        private int symbolSize;
        private String symbol;
        private boolean smooth;
        private LabelTheme label;
        @SerializedName("color")
        private List<String> colorX;

        public ItemStyleTheme getItemStyle() {
            return itemStyle;
        }

        public void setItemStyle(ItemStyleTheme itemStyle) {
            this.itemStyle = itemStyle;
        }

        public LineStyleTheme getLineStyle() {
            return lineStyle;
        }

        public void setLineStyle(LineStyleTheme lineStyle) {
            this.lineStyle = lineStyle;
        }

        public int getSymbolSize() {
            return symbolSize;
        }

        public void setSymbolSize(int symbolSize) {
            this.symbolSize = symbolSize;
        }

        public String getSymbol() {
            return symbol;
        }

        public void setSymbol(String symbol) {
            this.symbol = symbol;
        }

        public boolean isSmooth() {
            return smooth;
        }

        public void setSmooth(boolean smooth) {
            this.smooth = smooth;
        }

        public LabelTheme getLabel() {
            return label;
        }

        public void setLabel(LabelTheme label) {
            this.label = label;
        }

        public List<String> getColorX() {
            return colorX;
        }

        public void setColorX(List<String> colorX) {
            this.colorX = colorX;
        }

    }

    public static class MapBean {
        /**
         * itemStyle : {"normal":{"areaColor":"#eeeeee","borderColor":"#444444","borderWidth":0.5},"emphasis":{"areaColor":"rgba(255,215,0,0.8)","borderColor":"#444444","borderWidth":1}}
         * label : {"normal":{"textStyle":{"color":"#000000"}},"emphasis":{"textStyle":{"color":"rgb(100,0,0)"}}}
         */

        private ItemStyleTheme itemStyle;
        private LabelTheme label;

        public ItemStyleTheme getItemStyle() {
            return itemStyle;
        }

        public void setItemStyle(ItemStyleTheme itemStyle) {
            this.itemStyle = itemStyle;
        }

        public LabelTheme getLabel() {
            return label;
        }

        public void setLabel(LabelTheme label) {
            this.label = label;
        }


    }

    public static class GeoBean {
        /**
         * itemStyle : {"normal":{"areaColor":"#eeeeee","borderColor":"#444444","borderWidth":0.5},"emphasis":{"areaColor":"rgba(255,215,0,0.8)","borderColor":"#444444","borderWidth":1}}
         * label : {"normal":{"textStyle":{"color":"#000000"}},"emphasis":{"textStyle":{"color":"rgb(100,0,0)"}}}
         */

        private ItemStyleTheme itemStyle;
        private LabelTheme label;

        public ItemStyleTheme getItemStyle() {
            return itemStyle;
        }

        public void setItemStyle(ItemStyleTheme itemStyle) {
            this.itemStyle = itemStyle;
        }

        public LabelTheme getLabel() {
            return label;
        }

        public void setLabel(LabelTheme label) {
            this.label = label;
        }

    }

    public static class CategoryAxisBean {
        /**
         * axisLine : {"show":true,"lineStyle":{"color":"#333"}}
         * axisTick : {"show":true,"lineStyle":{"color":"#333"}}
         * axisLabel : {"show":true,"textStyle":{"color":"#333"}}
         * splitLine : {"show":false,"lineStyle":{"color":["#ccc"]}}
         * splitArea : {"show":false,"areaStyle":{"color":["rgba(250,250,250,0.3)","rgba(200,200,200,0.3)"]}}
         */

        private AxisLineBean axisLine;
        private AxisTickBean axisTick;
        private AxisLabelBean axisLabel;
        private SplitLineBean splitLine;
        private SplitAreaBean splitArea;

        public AxisLineBean getAxisLine() {
            return axisLine;
        }

        public void setAxisLine(AxisLineBean axisLine) {
            this.axisLine = axisLine;
        }

        public AxisTickBean getAxisTick() {
            return axisTick;
        }

        public void setAxisTick(AxisTickBean axisTick) {
            this.axisTick = axisTick;
        }

        public AxisLabelBean getAxisLabel() {
            return axisLabel;
        }

        public void setAxisLabel(AxisLabelBean axisLabel) {
            this.axisLabel = axisLabel;
        }

        public SplitLineBean getSplitLine() {
            return splitLine;
        }

        public void setSplitLine(SplitLineBean splitLine) {
            this.splitLine = splitLine;
        }

        public SplitAreaBean getSplitArea() {
            return splitArea;
        }

        public void setSplitArea(SplitAreaBean splitArea) {
            this.splitArea = splitArea;
        }

    }

    public static class ValueAxisBean {
        /**
         * axisLine : {"show":true,"lineStyle":{"color":"#333"}}
         * axisTick : {"show":true,"lineStyle":{"color":"#333"}}
         * axisLabel : {"show":true,"textStyle":{"color":"#333"}}
         * splitLine : {"show":true,"lineStyle":{"color":["#ccc"]}}
         * splitArea : {"show":false,"areaStyle":{"color":["rgba(250,250,250,0.3)","rgba(200,200,200,0.3)"]}}
         */

        private AxisLineBean axisLine;
        private AxisTickBean axisTick;
        private AxisLabelBean axisLabel;
        private SplitLineBean splitLine;
        private SplitAreaBean splitArea;

        public AxisLineBean getAxisLine() {
            return axisLine;
        }

        public void setAxisLine(AxisLineBean axisLine) {
            this.axisLine = axisLine;
        }

        public AxisTickBean getAxisTick() {
            return axisTick;
        }

        public void setAxisTick(AxisTickBean axisTick) {
            this.axisTick = axisTick;
        }

        public AxisLabelBean getAxisLabel() {
            return axisLabel;
        }

        public void setAxisLabel(AxisLabelBean axisLabel) {
            this.axisLabel = axisLabel;
        }

        public SplitLineBean getSplitLine() {
            return splitLine;
        }

        public void setSplitLine(SplitLineBean splitLine) {
            this.splitLine = splitLine;
        }

        public SplitAreaBean getSplitArea() {
            return splitArea;
        }

        public void setSplitArea(SplitAreaBean splitArea) {
            this.splitArea = splitArea;
        }


    }

    public static class LogAxisBean {
        /**
         * axisLine : {"show":true,"lineStyle":{"color":"#333"}}
         * axisTick : {"show":true,"lineStyle":{"color":"#333"}}
         * axisLabel : {"show":true,"textStyle":{"color":"#333"}}
         * splitLine : {"show":true,"lineStyle":{"color":["#ccc"]}}
         * splitArea : {"show":false,"areaStyle":{"color":["rgba(250,250,250,0.3)","rgba(200,200,200,0.3)"]}}
         */

        private AxisLineBean axisLine;
        private AxisTickBean axisTick;
        private AxisLabelBean axisLabel;
        private SplitLineBean splitLine;
        private SplitAreaBean splitArea;

        public AxisLineBean getAxisLine() {
            return axisLine;
        }

        public void setAxisLine(AxisLineBean axisLine) {
            this.axisLine = axisLine;
        }

        public AxisTickBean getAxisTick() {
            return axisTick;
        }

        public void setAxisTick(AxisTickBean axisTick) {
            this.axisTick = axisTick;
        }

        public AxisLabelBean getAxisLabel() {
            return axisLabel;
        }

        public void setAxisLabel(AxisLabelBean axisLabel) {
            this.axisLabel = axisLabel;
        }

        public SplitLineBean getSplitLine() {
            return splitLine;
        }

        public void setSplitLine(SplitLineBean splitLine) {
            this.splitLine = splitLine;
        }

        public SplitAreaBean getSplitArea() {
            return splitArea;
        }

        public void setSplitArea(SplitAreaBean splitArea) {
            this.splitArea = splitArea;
        }


    }

    public static class TimeAxisBean {
        /**
         * axisLine : {"show":true,"lineStyle":{"color":"#333"}}
         * axisTick : {"show":true,"lineStyle":{"color":"#333"}}
         * axisLabel : {"show":true,"textStyle":{"color":"#333"}}
         * splitLine : {"show":true,"lineStyle":{"color":["#ccc"]}}
         * splitArea : {"show":false,"areaStyle":{"color":["rgba(250,250,250,0.3)","rgba(200,200,200,0.3)"]}}
         */

        private AxisLineBean axisLine;
        private AxisTickBean axisTick;
        private AxisLabelBean axisLabel;
        private SplitLineBean splitLine;
        private SplitAreaBean splitArea;

        public AxisLineBean getAxisLine() {
            return axisLine;
        }

        public void setAxisLine(AxisLineBean axisLine) {
            this.axisLine = axisLine;
        }

        public AxisTickBean getAxisTick() {
            return axisTick;
        }

        public void setAxisTick(AxisTickBean axisTick) {
            this.axisTick = axisTick;
        }

        public AxisLabelBean getAxisLabel() {
            return axisLabel;
        }

        public void setAxisLabel(AxisLabelBean axisLabel) {
            this.axisLabel = axisLabel;
        }

        public SplitLineBean getSplitLine() {
            return splitLine;
        }

        public void setSplitLine(SplitLineBean splitLine) {
            this.splitLine = splitLine;
        }

        public SplitAreaBean getSplitArea() {
            return splitArea;
        }

        public void setSplitArea(SplitAreaBean splitArea) {
            this.splitArea = splitArea;
        }


    }

    public static class ToolboxBean {
        /**
         * iconStyle : {"normal":{"borderColor":"#999999"},"emphasis":{"borderColor":"#666666"}}
         */

        private IconStyleBean iconStyle;

        public IconStyleBean getIconStyle() {
            return iconStyle;
        }

        public void setIconStyle(IconStyleBean iconStyle) {
            this.iconStyle = iconStyle;
        }

        public static class IconStyleBean {
            /**
             * normal : {"borderColor":"#999999"}
             * emphasis : {"borderColor":"#666666"}
             */

            private NormalTheme normal;
            private NormalTheme emphasis;

            public NormalTheme getNormal() {
                return normal;
            }

            public void setNormal(NormalTheme normal) {
                this.normal = normal;
            }

            public NormalTheme getEmphasis() {
                return emphasis;
            }

            public void setEmphasis(NormalTheme emphasis) {
                this.emphasis = emphasis;
            }
        }
    }

    public static class LegendBean {
        /**
         * textStyle : {"color":"#333333"}
         */

        private TextStyleTheme textStyle;

        public TextStyleTheme getTextStyle() {
            return textStyle;
        }

        public void setTextStyle(TextStyleTheme textStyle) {
            this.textStyle = textStyle;
        }
    }

    public static class TooltipBean {
        /**
         * axisPointer : {"lineStyle":{"color":"#cccccc","width":1},"crossStyle":{"color":"#cccccc","width":1}}
         */

        private AxisPointerBean axisPointer;

        public AxisPointerBean getAxisPointer() {
            return axisPointer;
        }

        public void setAxisPointer(AxisPointerBean axisPointer) {
            this.axisPointer = axisPointer;
        }

        public static class AxisPointerBean {
            /**
             * lineStyle : {"color":"#cccccc","width":1}
             * crossStyle : {"color":"#cccccc","width":1}
             */

            private NormalTheme lineStyle;
            private NormalTheme crossStyle;

            public NormalTheme getLineStyle() {
                return lineStyle;
            }

            public void setLineStyle(NormalTheme lineStyle) {
                this.lineStyle = lineStyle;
            }

            public NormalTheme getCrossStyle() {
                return crossStyle;
            }

            public void setCrossStyle(NormalTheme crossStyle) {
                this.crossStyle = crossStyle;
            }
        }
    }

    public static class TimelineBean {
        /**
         * lineStyle : {"color":"#293c55","width":1}
         * itemStyle : {"normal":{"color":"#293c55","borderWidth":1},"emphasis":{"color":"#a9334c"}}
         * controlStyle : {"normal":{"color":"#293c55","borderColor":"#293c55","borderWidth":0.5},"emphasis":{"color":"#293c55","borderColor":"#293c55","borderWidth":0.5}}
         * checkpointStyle : {"color":"#e43c59","borderColor":"rgba(194,53,49,0.5)"}
         * label : {"normal":{"textStyle":{"color":"#293c55"}},"emphasis":{"textStyle":{"color":"#293c55"}}}
         */

        private NormalTheme lineStyle;
        private ItemStyleTheme itemStyle;
        private ControlStyleBean controlStyle;
        private NormalTheme checkpointStyle;
        private LabelTheme label;

        public NormalTheme getLineStyle() {
            return lineStyle;
        }

        public void setLineStyle(NormalTheme lineStyle) {
            this.lineStyle = lineStyle;
        }

        public ItemStyleTheme getItemStyle() {
            return itemStyle;
        }

        public void setItemStyle(ItemStyleTheme itemStyle) {
            this.itemStyle = itemStyle;
        }

        public ControlStyleBean getControlStyle() {
            return controlStyle;
        }

        public void setControlStyle(ControlStyleBean controlStyle) {
            this.controlStyle = controlStyle;
        }

        public NormalTheme getCheckpointStyle() {
            return checkpointStyle;
        }

        public void setCheckpointStyle(NormalTheme checkpointStyle) {
            this.checkpointStyle = checkpointStyle;
        }

        public LabelTheme getLabel() {
            return label;
        }

        public void setLabel(LabelTheme label) {
            this.label = label;
        }


        public static class ControlStyleBean {
            /**
             * normal : {"color":"#293c55","borderColor":"#293c55","borderWidth":0.5}
             * emphasis : {"color":"#293c55","borderColor":"#293c55","borderWidth":0.5}
             */

            private NormalTheme normal;
            private NormalTheme emphasis;

            public NormalTheme getNormal() {
                return normal;
            }

            public void setNormal(NormalTheme normal) {
                this.normal = normal;
            }

            public NormalTheme getEmphasis() {
                return emphasis;
            }

            public void setEmphasis(NormalTheme emphasis) {
                this.emphasis = emphasis;
            }

        }

    }

    public static class VisualMapBean {
        @SerializedName("color")
        private List<String> colorX;

        public List<String> getColorX() {
            return colorX;
        }

        public void setColorX(List<String> colorX) {
            this.colorX = colorX;
        }
    }

    public static class DataZoomBean {
        /**
         * backgroundColor : rgba(47,69,84,0)
         * dataBackgroundColor : rgba(47,69,84,0.3)
         * fillerColor : rgba(167,183,204,0.4)
         * handleColor : #a7b7cc
         * handleSize : 100%
         * textStyle : {"color":"#333333"}
         */

        private String backgroundColor;
        private String dataBackgroundColor;
        private String fillerColor;
        private String handleColor;
        private String handleSize;
        private TextStyleTheme textStyle;

        public String getBackgroundColor() {
            return backgroundColor;
        }

        public void setBackgroundColor(String backgroundColor) {
            this.backgroundColor = backgroundColor;
        }

        public String getDataBackgroundColor() {
            return dataBackgroundColor;
        }

        public void setDataBackgroundColor(String dataBackgroundColor) {
            this.dataBackgroundColor = dataBackgroundColor;
        }

        public String getFillerColor() {
            return fillerColor;
        }

        public void setFillerColor(String fillerColor) {
            this.fillerColor = fillerColor;
        }

        public String getHandleColor() {
            return handleColor;
        }

        public void setHandleColor(String handleColor) {
            this.handleColor = handleColor;
        }

        public String getHandleSize() {
            return handleSize;
        }

        public void setHandleSize(String handleSize) {
            this.handleSize = handleSize;
        }

        public TextStyleTheme getTextStyle() {
            return textStyle;
        }

        public void setTextStyle(TextStyleTheme textStyle) {
            this.textStyle = textStyle;
        }
    }

    public static class MarkPointBean {
        /**
         * label : {"normal":{"textStyle":{"color":"#eee"}},"emphasis":{"textStyle":{"color":"#eee"}}}
         */

        private LabelTheme label;

        public LabelTheme getLabel() {
            return label;
        }

        public void setLabel(LabelTheme label) {
            this.label = label;
        }

    }

    public  class AxisLineBean {
        /**
         * show : true
         * lineStyle : {"color":"#333"}
         */

        private boolean show;
        private LineStyleTheme lineStyle;

        public boolean isShow() {
            return show;
        }

        public void setShow(boolean show) {
            this.show = show;
        }

        public LineStyleTheme getLineStyle() {
            return lineStyle;
        }

        public void setLineStyle(LineStyleTheme lineStyle) {
            this.lineStyle = lineStyle;
        }
    }

    public  class AxisTickBean {
        /**
         * show : true
         * lineStyle : {"color":"#333"}
         */

        private boolean show;
        private LineStyleTheme lineStyle;

        public boolean isShow() {
            return show;
        }

        public void setShow(boolean show) {
            this.show = show;
        }

        public LineStyleTheme getLineStyle() {
            return lineStyle;
        }

        public void setLineStyle(LineStyleTheme lineStyle) {
            this.lineStyle = lineStyle;
        }
    }

    public class AxisLabelBean {
        /**
         * show : true
         * textStyle : {"color":"#333"}
         */

        private boolean show;
        private LineStyleTheme textStyle;

        public boolean isShow() {
            return show;
        }

        public void setShow(boolean show) {
            this.show = show;
        }

        public LineStyleTheme getTextStyle() {
            return textStyle;
        }

        public void setTextStyle(LineStyleTheme textStyle) {
            this.textStyle = textStyle;
        }
    }

    public class SplitLineBean {
        /**
         * show : false
         * lineStyle : {"color":["#ccc"]}
         */

        private boolean show;
        private LineStyleTheme lineStyle;

        public boolean isShow() {
            return show;
        }

        public void setShow(boolean show) {
            this.show = show;
        }

        public LineStyleTheme getLineStyle() {
            return lineStyle;
        }

        public void setLineStyle(LineStyleTheme lineStyle) {
            this.lineStyle = lineStyle;
        }

    }

    public  class SplitAreaBean {
        /**
         * show : false
         * areaStyle : {"color":["rgba(250,250,250,0.3)","rgba(200,200,200,0.3)"]}
         */

        private boolean show;
        private AreaStyleBean areaStyle;

        public boolean isShow() {
            return show;
        }

        public void setShow(boolean show) {
            this.show = show;
        }

        public AreaStyleBean getAreaStyle() {
            return areaStyle;
        }

        public void setAreaStyle(AreaStyleBean areaStyle) {
            this.areaStyle = areaStyle;
        }

        public  class AreaStyleBean {
            @SerializedName("color")
            private List<String> colorX;

            public List<String> getColorX() {
                return colorX;
            }

            public void setColorX(List<String> colorX) {
                this.colorX = colorX;
            }
        }
    }
}
