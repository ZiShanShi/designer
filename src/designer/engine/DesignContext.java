package designer.engine;

import designer.DesignerConstant;
import designer.EChangeType;
import designer.options.EChartType;
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
    private String theme;

    private List<Segment> filterSegmentList;

    private Map<String, String> runtimeParams;
    private  DesignContext (){}

    public  DesignContext (HttpServletRequest request) throws Exception {
        runtimeParams = new HashMap<>();
        Enumeration<String> parameterNames = request.getParameterNames();
        if (parameterNames.hasMoreElements()) {
            String name = parameterNames.nextElement();
            if (DesignerConstant.ID.equalsIgnoreCase(name)) {
                topicId = request.getParameter(name);
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
}
