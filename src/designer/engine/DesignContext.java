package designer.engine;

import designer.IDesiginerConstant;
import designer.options.EChangeType;
import designer.options.EChartType;
import designer.options.EDesignerChartTheme;
import foundation.util.Util;
import foundation.variant.Segment;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author kimi
 * @description 设计器上下文
 * @date 2018-12-17 15:27
 */


public class DesignContext {

    private EChangeType changeType;
    private String topicId;
    private boolean changed;
    private boolean hasGrid;
    private EChartType chartType;
    private List<String> dimensionList;
    private List<String> measurmentList;
    private EDesignerChartTheme theme;

    private List<Segment> filterSegmentList;

    private Map<String, String> runtimeParams;
    private  DesignContext (){}

    public  DesignContext (HttpServletRequest request) throws Exception {
        runtimeParams = new HashMap<>();
        Enumeration<String> parameterNames = request.getParameterNames();
        if (parameterNames.hasMoreElements()) {
            String name = parameterNames.nextElement();
            if (IDesiginerConstant.ID.equalsIgnoreCase(name)) {
                topicId = request.getParameter(name);
            }
            else if (IDesiginerConstant.CHANGE_TYPE.equalsIgnoreCase(name)) {
                changeType = EChangeType.parse(request.getParameter(name));
            }else if (IDesiginerConstant.CHANGED.equalsIgnoreCase(name)) {
                changed = Util.stringToBoolean(request.getParameter(name));
            }
            else if (IDesiginerConstant.HAS_GRID.equalsIgnoreCase(name)) {
                hasGrid = Util.stringToBoolean(request.getParameter(name));
            }
            else if (IDesiginerConstant.CHART_TYPE.equalsIgnoreCase(name)) {
                chartType = EChartType.parse(request.getParameter(name));
            }
            else if (IDesiginerConstant.DIMENSION.equalsIgnoreCase(name)) {
                dimensionList = Util.StringToList(request.getParameter(name));
            }
            else if (IDesiginerConstant.MEASURMENT.equalsIgnoreCase(name)) {
                measurmentList = Util.StringToList(request.getParameter(name));
            }
            else if (IDesiginerConstant.FILTERS.equalsIgnoreCase(name)) {
                filterSegmentList = parse2FilterSegments(request.getParameter(name));
            } else if (IDesiginerConstant.THEME.equalsIgnoreCase(name)) {
                theme = EDesignerChartTheme.parse(request.getParameter(name));
            }


        }
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


    public String getTopicId() {
        return topicId;
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

    public EDesignerChartTheme getTheme() {
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
}
