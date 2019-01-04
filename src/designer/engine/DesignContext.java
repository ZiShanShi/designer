package designer.engine;

import designer.DesignerComponentFactory;
import designer.DesignerConstant;
import designer.DesignerUtil;
import designer.EChangeType;
import designer.cache.EOptionSourceType;
import designer.cache.FieldNode;
import designer.exception.DesignerOptionException;
import designer.options.EChartType;
import designer.options.echart.json.GsonOption;
import designer.widget.ChartAxis;
import designer.widget.Widget;
import designer.widget.WidgetManager;
import designer.widget.theme.Theme;
import designer.xml.EDesignerXmlType;
import designer.xml.XmlReader;
import foundation.config.Configer;
import foundation.data.Entity;
import foundation.persist.DataHandler;
import foundation.util.Util;
import foundation.variant.Segment;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.MessageFormat;
import java.util.*;

/**
 * @author kimi
 * @description 设计器上下文
 * @date 2018-12-17 15:27
 */


public class DesignContext {

    private EChangeType changeType;
    private String widgetId;
    private boolean changed; // 区别是全亮还是增量;
    private boolean hasGrid;
    private EChartType chartType;
    private List<String> dimensionList;
    private List<String> measurmentList;
    private String theme;
    private List<ChartAxis> axisList;
    private List<Segment> filterSegmentList;

    private Map<String, String> runtimeParams;
    private Widget widget;

    private  DesignContext (){}

    public  DesignContext (HttpServletRequest request)  {
        runtimeParams = new HashMap<>();
        Enumeration<String> parameterNames = request.getParameterNames();
        while (parameterNames.hasMoreElements()) {
            String name = parameterNames.nextElement();
            if (DesignerConstant.ID.equalsIgnoreCase(name)) {
                widgetId = request.getParameter(name);
                widget = WidgetManager.getInstance().getWidget(getWidgetId());
                if (widget == null) {
                    widget = loadWidget(widget, widgetId);
                }
            }
            else if (DesignerConstant.CHANGE_TYPE.equalsIgnoreCase(name)) {
                changeType = EChangeType.parse(request.getParameter(name));
            }else if (DesignerConstant.CHANGED.equalsIgnoreCase(name)) {
                changed = Util.stringToBoolean(request.getParameter(name));
            }
            else if (DesignerConstant.HAS_GRID.equalsIgnoreCase(name)) {
                hasGrid = Util.stringToBoolean(request.getParameter(name));
            }
            else if (DesignerConstant.CHART_TYPE.equalsIgnoreCase(name)) {
                chartType = EChartType.parse(request.getParameter(name));
            }
            else if (DesignerConstant.DIMENSIONS.equalsIgnoreCase(name)) {
                dimensionList = Util.StringToList(request.getParameter(name));
            }
            else if (DesignerConstant.MEASURMENTS.equalsIgnoreCase(name)) {
                measurmentList = Util.StringToList(request.getParameter(name));
            }
            else if (DesignerConstant.FILTERS.equalsIgnoreCase(name)) {
                filterSegmentList = parse2FilterSegments(request.getParameter(name));
            } else if (DesignerConstant.THEME.equalsIgnoreCase(name)) {
                theme = request.getParameter(name);
            } else if (DesignerConstant.fix_element_Axis.equalsIgnoreCase(name)) {
                axisList = new ArrayList<>();
            }


        }
    }

    private Widget loadWidget(Widget widget, String widgetId) {
        Entity topicLine;
        try {
            topicLine = DataHandler.getLine(DesignerConstant.TABLE_designer_panelwidget, DesignerConstant.WIDGETID, widgetId);
            String widgetName = topicLine.getString(DesignerConstant.FIELD_WIDGETNAME);
            String path = topicLine.getString(DesignerConstant.FIELD_WIDGETPATH);
            path = path.replace(DesignerConstant.ROOT, Configer.getPath_Application());
            File topicFile = DesignerUtil.checkFileLegality(path);
            widget = new Widget(widgetId, widgetName);
            XmlReader topicReader = new XmlReader(EDesignerXmlType.realTopic);
            widget.setPath(path);
            topicReader.read(topicFile, widget);
            WidgetManager.getInstance().addWidget(widget);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return widget;
    }

    public void putRuntimeParams(String key, String value) {
        runtimeParams.put(key, value);
    }

    public  String getParams(String key) {
        return runtimeParams.get(key);
    }

    private List<Segment> parse2FilterSegments(String parameter) {
        //TODO 过滤条件
        return  null;
    }


    public String getWidgetId() {
        return widgetId;
    }

    public boolean isChanged() {
        return changed;
    }

    public boolean isHasGrid() {
        return hasGrid;
    }

    public EChartType getChartType() {
        return chartType;
    }

    public List<String> getDimensionList() {
        return dimensionList;
    }

    public List<String> getMeasurmentList() {
        return measurmentList;
    }

    public String getTheme() {
        return theme;
    }

    public List<Segment> getFilterSegmentList() {
        return filterSegmentList;
    }

    public Map<String, String> getRuntimeParams() {
        return runtimeParams;
    }

    public EChangeType getChangeType() {
        return changeType;
    }

    public void exec() {
        switch (changeType) {
            case theme:
                String nowTheme = getTheme();
                Theme theme = DesignerComponentFactory.getInstance().getThemeByName(nowTheme);
                widget.getChartOption().setTheme(theme);
                widget.putFieldNode(EOptionSourceType.Changed, new FieldNode(Theme.class, DesignerConstant.THEME));
                break;
            case unknown:
                String type = "title-show";
                String value = "true";
                Widget widget = WidgetManager.getInstance().getWidget(getWidgetId());
                if (widget != null) {
                    GsonOption realChartOption = widget.getChartOption().getRealChartOption();

                    try {
                        loadData(type, value,realChartOption);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;

            default:
                break;

        }
    }

    private void loadData(String type, String value, GsonOption realChartOption) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, NoSuchFieldException {
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
        realChartOption.putFieldNode(EOptionSourceType.Changed, node);

        Method method = invoke.getClass().getMethod(s, declaredField.getType());
        Object o = Util.StringToOther(value, declaredField.getType());
        method.invoke(invoke, o);
    }
}
