package designer.engine;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import designer.*;
import designer.cache.EOptionSourceType;
import designer.cache.FieldNode;
import designer.exception.DesignerOptionException;
import designer.exception.DesignerOptionsFileException;
import designer.options.EChartType;
import designer.options.GridField;
import designer.options.GridOption;
import designer.options.echart.axis.Axis;
import designer.options.echart.json.GsonOption;
import designer.options.echart.series.Series;
import designer.widget.*;
import designer.widget.theme.Theme;
import foundation.util.Util;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.MessageFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author kimi
 * @description 设计器上下文
 * @date 2018-12-17 15:27
 */


public class DesignContext {

    private HttpServletRequest request;
    private EChangeType changeType;
    private String widgetId;
    private boolean changed; // TODO 区别是全亮还是增量; 暂时都是全亮
    private boolean hasGrid;
    private EChartType chartType;
    private String theme;
    private List<ChartAxis> axisList;
    private List<SegmentPart> filterSegmentList;
    private List<GridField> gridFieldList;

    private Map<String, String> runtimeParams;
    private Widget widget;
    private ChangeNodeSegment changeNodeSegment;

    private  DesignContext (){}

    public  DesignContext (HttpServletRequest request) {
        this. request = request;
        runtimeParams = new HashMap<>();
        axisList = new ArrayList<>();
        gridFieldList = new ArrayList<>();

        Enumeration<String> parameterNames = request.getParameterNames();
        while (parameterNames.hasMoreElements()) {
            String name = parameterNames.nextElement();
            if (DesignerConstant.ID.equalsIgnoreCase(name)) {
                widgetId = request.getParameter(name);
                widget = WidgetManager.getInstance().getWidget(getWidgetId());
                if (widget == null) {
                    widget = WidgetManager.getInstance().loadWidget(widgetId);
                }
            }
            else if (DesignerConstant.CHANGE_TYPE.equalsIgnoreCase(name)) {
                changeType = EChangeType.parse(request.getParameter(name));
                changeNodeSegment = new ChangeNodeSegment(changeType);
            }else if (DesignerConstant.CHANGED.equalsIgnoreCase(name)) {
                changed = Util.stringToBoolean(request.getParameter(name));
            }
            else if (DesignerConstant.HAS_GRID.equalsIgnoreCase(name)) {
                hasGrid = Util.stringToBoolean(request.getParameter(name));
            }
            else if (DesignerConstant.CHART_TYPE.equalsIgnoreCase(name)) {
                chartType = EChartType.parse(request.getParameter(name));
            }
            else if (DesignerConstant.fix_element_Axis.equalsIgnoreCase(name)) {
                //多方向
                String axisArray = request.getParameter(name);
                axisList = parse2ChartAxis(axisArray);
            } else if (DesignerConstant.FILTERS.equalsIgnoreCase(name)) {
                filterSegmentList = parse2FilterSegments(request.getParameter(name));
            } else if (DesignerConstant.THEME.equalsIgnoreCase(name)) {
                theme = request.getParameter(name);
            } else if (DesignerConstant.Grid_element_fields.equalsIgnoreCase(name)) {
                gridFieldList = parse2FieldList(request.getParameter(name));
            }

        }
    }

    private List<GridField> parse2FieldList(String fieldNames) {
        Gson gson = new GsonBuilder().create();
        List<GridField> gridFields = gson.fromJson(fieldNames, new TypeToken<List<GridField>>() {
        }.getType());
        return gridFields;
    }

    private List<ChartAxis> parse2ChartAxis(String axisArray) {
        Gson gson = new GsonBuilder().registerTypeAdapter(ChartAxis.class, new ChartAxisDeserializer()).create();
        List<ChartAxis> axisList = gson.fromJson(axisArray, new TypeToken<List<ChartAxis>>() {
        }.getType());
        return axisList;

    }


    public void putRuntimeParams(String key, String value) {
        runtimeParams.put(key, value);
    }

    public  String getParams(String key) {
        return runtimeParams.get(key);
    }

    private List<SegmentPart> parse2FilterSegments(String filter) {
        Gson gson = new GsonBuilder().registerTypeAdapter(SegmentPart.class, new SegmentPartDeserializer()).create();
            List<SegmentPart> fieldList = gson.fromJson(filter, new TypeToken<List<SegmentPart>>() {
            }.getType());
            return fieldList;
    }


    public String getWidgetId() {
        return widgetId;
    }


    public Widget exec() {
        GsonOption realChartOption = widget.getChartOption().getRealChartOption();
        List<ChartAxis> axisList = widget.getAxisList();
        switch (changeType) {
            case all:
                widget = WidgetManager.getInstance().loadWidget(widgetId);
                break;
            case hasgrid:
                GridOption gridOption = widget.getGridOption();
                if (gridOption != null) {
                    gridOption.setShow(hasGrid);
                } else {
                    throw new DesignerOptionsFileException("fridoption 无默认设置");
                }
                widget.setHasGrid(hasGrid);
                break;
            case filters:
                widget.setSegmentList(filterSegmentList);
                try {
                    widget.invalidateData();
                } catch (Exception e) {
                    e.printStackTrace();

                }
                break;
            case axis:
                try {
                    widget.setAxisList(this.axisList);
                    widget.invalidateData();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                break;
            case inverseX:
                ArrayList<Axis> axes = realChartOption.xAxis();
                for (Axis axis : axes) {
                    axis.inverse(true);
                }

                widget.getAxisList().stream().filter(chartAxis -> chartAxis.getAxis().equals(EDimensionAxis.x))
                        .map(chartAxis -> chartAxis.setInverse(true)).collect(Collectors.toList());

                break;
            case inverseY:
                ArrayList<Axis> yaxes = realChartOption.yAxis();
                for (Axis axis : yaxes) {
                    axis.inverse(true);
                }
                widget.getAxisList().stream().filter(chartAxis -> chartAxis.getAxis().equals(EDimensionAxis.y))
                        .map(chartAxis -> chartAxis.setInverse(true)).collect(Collectors.toList());

                break;

            case inversePositon:
                String positionStr = request.getParameter(DesignerConstant.keyelement_position);
                EAxisPositon eAxisPositon = EAxisPositon.valueOf(positionStr);
                EDimensionAxis axisName = eAxisPositon.getAxisName();
                //opsition
                ArrayList<Axis> axislist = null;
                if (axisName.equals(EDimensionAxis.y)) {
                    //说明修改的是y轴
                    axislist = realChartOption.getyAxis();
                } else if (axisName.equals(EDimensionAxis.x)) {
                    axislist = realChartOption.getxAxis();
                }
                //axislist
                widget.getAxisList().stream().filter(chartAxis -> chartAxis.getAxis().equals(axisName))
                        .map(chartAxis -> chartAxis.setInverse(true)).collect(Collectors.toList());

                for (Axis axis : axislist) {
                    eAxisPositon = changeAxisPosition(eAxisPositon);
                    axis.position(eAxisPositon.name());
                }

                break;
            case inverse://    x,y 反转
                //option 互换
                ArrayList<Axis> xAxis = realChartOption.xAxis();
                ArrayList<Axis> yAxis = realChartOption.yAxis();

                realChartOption.xAxis(yAxis);
                realChartOption.yAxis(xAxis);

                //series index
                ArrayList<Series> seriesList = realChartOption.series();
                for (Series series : seriesList) {
                    Integer xAxisIndex = series.xAxisIndex();
                    Integer yAxisIndex = series.yAxisIndex();
                    if (xAxisIndex != null || xAxisIndex == 0) {
                        series.xAxisIndex(null);
                        series.yAxisIndex(xAxisIndex);
                    }
                    if (yAxisIndex != null || yAxisIndex == 0) {
                        series.yAxisIndex(null);
                        series.xAxisIndex(yAxisIndex);
                    }
                }

                // axislist
                for (ChartAxis chartAxis : axisList) {

                    EDimensionAxis axis = chartAxis.getAxis();
                    if (axis.equals(EDimensionAxis.x)) {
                        chartAxis.setAxis(EDimensionAxis.y);
                    } else if (axis.equals(EDimensionAxis.y)) {
                        chartAxis.setAxis(EDimensionAxis.x);
                    }

                    EAxisPositon positon = chartAxis.getPositon();
                    if (positon.equals(EAxisPositon.bottom)) {
                        chartAxis.setPositon(EAxisPositon.left);
                    } else if (positon.equals(EAxisPositon.left)) {
                        chartAxis.setPositon(EAxisPositon.bottom);
                    } else if (positon.equals(EAxisPositon.top)) {
                        chartAxis.setPositon(EAxisPositon.right);
                    } else if (positon.equals(EAxisPositon.right)) {
                        chartAxis.setPositon(EAxisPositon.top);
                    }
                }
                break;
            case theme:
                String nowTheme = theme;
                Theme theme = DesignerComponentFactory.getInstance().getThemeByName(nowTheme);
                widget.getChartOption().setTheme(theme);
                                break;
            case chartOption:
                String value = "true";
                String type = "title-show";
                changeNodeSegment.setValue(value);
                changeNodeSegment.setFieldNode(new FieldNode(type));

                try {
                    loadData(changeNodeSegment, realChartOption);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case gridField:
                gridFieldList.stream().map(field -> field.setDefault()).collect(Collectors.toList());
                widget.combineGridField(gridFieldList);
                try {
                    widget.invalidateData();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            default:
                break;
        }

        widget.putFieldNode(EOptionSourceType.Changed,changeNodeSegment.getFieldNode());
        return widget;
    }

    private EAxisPositon changeAxisPosition(EAxisPositon eAxisPositon) {
        EAxisPositon positon = null;
        switch (eAxisPositon) {
            case left:
                positon = EAxisPositon.right;
                break;
            case right:
                positon = EAxisPositon.left;
                break;
            case top:
                positon = EAxisPositon.bottom;
                break;
            case bottom:
                positon = EAxisPositon.top;
            default:
                break;
        }
        return positon;
    }

    private void loadData(ChangeNodeSegment changeNodeSegment, GsonOption realChartOption) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, NoSuchFieldException {
        String type = changeNodeSegment.getFieldNode().toString();
        String value = changeNodeSegment.getValue();
        List<String> strings = Util.StringToList(type, Util.Separator);
        int i = 0;
        Object invoke = null;
        while (i < (strings.size() - 1)) {
            String s = strings.get(i);
            Method method = realChartOption.getClass().getMethod(s);
            invoke = method.invoke(realChartOption, new Object[0]);

            i++;
        }
        String s = strings.get(strings.size() - 1);
        Field declaredField = DesignerUtil.getField(invoke.getClass(), s);
        FieldNode node = new FieldNode(declaredField.getType(),declaredField.getName());
        EOptionSourceType typeFromNode = realChartOption.getTypeFromNode(node);
        if (typeFromNode == null) {
            throw new DesignerOptionException(MessageFormat.format("load未将此节点加入node{1}", node.toString()));
        }
        realChartOption.putFieldNode(EOptionSourceType.Changed, changeNodeSegment.getFieldNode());

        Method method = invoke.getClass().getMethod(s, declaredField.getType());
        Object o = Util.StringToOther(value, declaredField.getType());
        method.invoke(invoke, o);
    }
}
