package designer;

import java.util.HashMap;

public class DesignerConstant {
    public static final String CACHE_DEFAULT_SIZE = "cacheSize";
    public static final String CALLABLE_METHORD_SYNCHRONIZE = "synchronize";
    public static final String CALLABLE_METHORD_GETWIDGET = "getWidget";

    public static final String ID = "id";


    public static final String CHANGED = "changed";
    public static final String CHART_TYPE = "chartType";
    public static final String HAS_GRID = "hasGrid";
    public static final String DIMENSIONS = "dimensions";
    public static final String MEASURMENTS = "measurments";
    public static final String DATA_TYPE = "dataType";
    public static final String DATA_NAME = "dataName";
    public static final String CATEGORY_AXIS = "categoryAxis";
    public static final String AND = " and ";

    public static final String CHART_OPTION = "chartOption";
    public static final String GRID_OPTION = "gridOption";
    public static final String FILTERS = "filters";
    public static final String THEME = "theme";
    public static final String CHANGE_TYPE = "changeType";
    public static final String DEFAULT = "default";
    public static final String NOW_CHART_TYPE = "nowChartType";
    public static final String PATH = "path";


    public static final String TABLE_designer_panelwidget = "designer_panelwidget";
    public static final String WIDGETID = "widgetid";
    public static final String FIELD_WIDGETNAME = "widgetname";
    public static final String FIELD_WIDGETPATH = "widgetpath";



    public static final String DEFAULT_CHART_ROOT_PATH = "chartRootPath";
    public static final String ROOT= "root";
    public static final String DEFAULT_CHART_PART_PATH = "defaultChartPartPath";
    public static final String DEFAULT_Topic_PART_PATH = "defaultTopicPartPath";
    public static final String DEFAULT_Theme_PART_PATH = "defaultThemePartPath";

    public static final String DEFAULT_CHART_ENGINE = "defaultChartEngine";
    public static final String DEFAULT_CHART_SUFFIX = "defaultChartSuffix";



    public static final String keyword_type = "type";
    public static final String keyword_type_file = "file";
    public static final String keyword_type_array = "array";
    public static final String keyword_type_equal = "=";
    public static final String keyword_type_like = "%%";
    public static final String keyword_type_leftlike = "%*";
    public static final String keyword_type_rightlike = "*%";

    public static final String keyword_type_other = "**other**";

    public static final String keyword_essential = "essential";
    public static final String keyword_remark = "remark";
    public static final String keyword_parent = "parent";
    public static final String keyword_name = "name";
    public static final String keyword_datalink = "datalink";
    public static final String keyword_pathDefault = "pathDefault";
    public static final String keyword_default = "default";

    public static final String keyelement_url = "url";
    public static final String keyelement_IF = "if";
    public static final String keyelement_Show = "show";
    public static final String keyelement_object = "object";

    public static final String keyelement_position = "position";
    public static final String keyelement_field = "field";


    public static final String fix_element_Axis = "Axis";
    public static final String fix_element_xAxis = "xAxis";
    public static final String fix_element_yAxis = "yAxis";


    public static final String Grid_element_showPage = "showPage";
    public static final String Grid_element_hasTitle = "hasTitle";
    public static final String Grid_element_pageSize = "pageSize";
    public static final String Grid_element_multiSelect = "multiSelect";
    public static final String Grid_element_enableAllSelect = "enableAllSelect";
    public static final String Grid_element_field = "field";

    public static final String Grid_field_caption = "caption";
    public static final String Grid_field_width = "width";
    public static final String Grid_field_align = "align";
    public static final String Grid_field_groupby = "groupby";

    public static final String GET = "get";
    public static final String IS = "is";


    public static HashMap<String,String> fixj2Json = new HashMap<>();
    public static HashMap<String,String> fixJson2J = new HashMap<>();

    static  {
        //只有enmu需要用
        fixj2Json.put("$or$", "|");

        fixJson2J.put("|", "$or$");
    }


}
