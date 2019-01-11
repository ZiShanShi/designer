package designer;

import designer.cache.CheckCacheObject;
import designer.cache.EOptionSourceType;
import designer.cache.FieldNode;
import designer.cache.ICacheSourceType;
import designer.exception.DesignerBaseException;
import designer.exception.DesignerFileException;
import designer.options.AxisField;
import designer.options.GridField;
import designer.options.GridOption;
import designer.options.echart.Option;
import designer.options.echart.json.GsonOption;
import designer.widget.ChartAxis;
import designer.widget.Widget;
import designer.widget.theme.Theme;
import designer.xml.EDesignerXmlType;
import designer.xml.XmlReader;
import foundation.config.Configer;
import foundation.data.DataType;
import foundation.data.Entity;
import foundation.persist.DataHandler;
import foundation.util.Util;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.*;
import java.text.MessageFormat;
import java.util.*;

/**
 * @author kimi
 * @description 设计器工具类
 * @date 2018-12-17 14:20
 */


public class DesignerUtil {
    private static List<String> noCatchedList = new ArrayList<>();

    private static List<String> noJsonCatchedList = new ArrayList<>();
    static {
        noCatchedList.add("xAxis");
        noCatchedList.add("yAxis");
        noCatchedList.add("series");
        noCatchedList.add("fieldNodeSourceMap");
        noCatchedList.add("nodeSourceTypeMap");

        noJsonCatchedList.add("timeline");
        noJsonCatchedList.add("visualMap");
        noJsonCatchedList.add("dataZoom");
    }

    public static File getWidgetFile(String widgetId) {
        Entity topicLine = null;
        try {
            topicLine = DataHandler.getLine(DesignerConstant.TABLE_designer_panelwidget, DesignerConstant.WIDGETID, widgetId);

        } catch (Exception e) {
            e.printStackTrace();
        }
        if (topicLine == null) {
            return null;
        }
        String filepath = topicLine.getString(DesignerConstant.FIELD_WIDGETPATH);
        File file = checkFileLegality(filepath);
        return file;
    }


    public static String fixXmlStringValue(String rawValue) {
        rawValue = rawValue.replaceAll(Util.String_Escape_newLine,Util.String_Space);
        rawValue = rawValue.replaceAll(Util.String_Escape_newSpace,Util.String_Space);
        rawValue = rawValue.trim();
        if (Util.isEmptyStr(rawValue)) {
            return Util.String_Empty;
        }else {
            return rawValue;
        }
    }
    public static File checkFileLegality(String filePath) {

        if (Util.isEmptyStr(filePath)) {
            throw new DesignerFileException("path is empty");
        }
        if (filePath.indexOf(DesignerConstant.ROOT) != -1) {
            filePath = filePath.replace(DesignerConstant.ROOT, Configer.getPath_Application());
        }
        File file = new File(filePath);
        if (!file.exists()) {
            throw new DesignerFileException(filePath);
        }
        return file;
    }

    public static File checkFileLegality(String filePath,String suffix) {
        if (Util.isEmptyStr(filePath)) {
            throw new DesignerFileException("path is empty");
        }
        if (Util.isEmptyStr(suffix)) {
            throw new DesignerFileException("suffix is empty");
        }

        if (filePath.contains(DesignerConstant.ROOT)) {
            filePath = filePath.replace(DesignerConstant.ROOT, Configer.getPath_Application());
        }
        if (!filePath.contains(Util.Dot)) {
            filePath = Util.stringJoin(filePath, Util.Dot, suffix);
        }
        File file = new File(filePath);
        if (!file.exists()) {
            throw new DesignerFileException(filePath);
        }
        return file;
    }
    public static String checkFileSuffix(String filePath) {
        if (Util.isEmptyStr(filePath)) {
            throw new DesignerFileException("path is empty");
        }
        String suffix = Configer.getParam(DesignerConstant.DEFAULT_CHART_SUFFIX);
        filePath = filePath.replace(DesignerConstant.ROOT,Configer.getPath_Application());
        if (!filePath.endsWith(suffix)) {
            filePath = Util.stringJoin(filePath, Util.Dot, suffix);
        }

        return filePath;
    }


    public static GsonOption combineOption(String path) {
        path = checkFileSuffix(path);
        File file = checkFileLegality(path);
        XmlReader reader = new XmlReader(EDesignerXmlType.realChart);
        GsonOption realOption = new GsonOption();
        reader.read(file, realOption);
        return realOption;
    }

    public static void combine(Widget widget) {
        combine(widget, DesignerComponentFactory.getInstance().getBaseWidget());
    }

    public static void combine(Option option) {
        combine(option, DesignerComponentFactory.getInstance().getDefautOption());
    }

    public static void combine(Widget widget, Widget baseWidget) {
        //组合成完整的widget
        try {
            combineObj(widget, baseWidget, null, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void combine(Option option, Option baseOption) {
        // 组合成完整的option
        try {
            combineObj(option, baseOption, null, false);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static JSONObject combineTheme(String optionStr, String themeName) {
        DesignerComponentFactory instance = DesignerComponentFactory.getInstance();
        Theme findTheme = instance.getThemeByName(themeName);
        JSONObject themeJson = findTheme.getThemeJson();
        JSONObject optionObject = JSONObject.fromObject(optionStr);
        JSONObject resultJsonObject = loadJSONObject(themeJson, optionObject);
        return resultJsonObject;
    }

    private static JSONObject loadJSONObject(JSONObject themeObject, JSONObject optionObject) {
        //Theme..
        Set ontKeySet = themeObject.keySet();

        for (Object key : ontKeySet) {
            Object oneThemeValue = themeObject.get(key);
            Object oneOptionValue = optionObject.get(key);
            checkObjectType(oneThemeValue, oneOptionValue, optionObject, key);
        }
        return optionObject;
    }

    private static void checkObjectType(Object oneThemeValue, Object oneOptionValue, JSONObject optionObject, Object key) {
        if (oneOptionValue instanceof JSONArray) {
            int oneOptionValueSize = ((JSONArray) oneOptionValue).size();
            if (oneThemeValue == null) {
                return;
            }
            for (int i = 0; i < oneOptionValueSize; i++) {
                Object subThemeValue = ((JSONArray) oneThemeValue).get(i);
                Object subOptionValue = ((JSONArray) oneOptionValue).get(i);
                checkObjectType(subThemeValue, subOptionValue, optionObject, key);
            }
        } else if (oneOptionValue instanceof JSONObject) {
            if (((JSONObject) oneThemeValue).isEmpty()) {
                if (!((JSONObject) oneOptionValue).isEmpty()) {
                    ((JSONObject) oneOptionValue).putAll((JSONObject) oneThemeValue);
                }

            } else {
                ((JSONObject) oneOptionValue).putAll((JSONObject) oneThemeValue);
            }
        } else {
            if (oneOptionValue == null) {
                if (noJsonCatchedList.contains(key)) {
                    return;
                }
                optionObject.put(key, oneThemeValue);
            }
        }
    }

    public static  void combineObj(Object targetObj, Object sourseObj, CheckCacheObject checkCacheObject, boolean doChange) throws IllegalAccessException, NoSuchFieldException, InstantiationException {
        Class<?> targetClass = targetObj.getClass();
        Class<?> sourseClass = sourseObj.getClass();
        Boolean mayCache = false;
        if (checkCacheObject == null) {
            if (targetClass != null && ICacheSourceType.class.isAssignableFrom(targetClass)) {
                mayCache = true;
            }
            checkCacheObject = new CheckCacheObject(mayCache, targetObj, sourseObj);
        }

        String targretClassName = targetClass.getName();
        String sourseClassName = sourseClass.getName();
        if (!targretClassName.equalsIgnoreCase(sourseClassName)) {
            throw  new DesignerBaseException(MessageFormat.format("combineObj 输入类型不一样 target: {0}; sourse:{1}",targretClassName,sourseClassName));
        }
        List<Field> targetFieldList = getFieldList(targetClass);
        List<Field> sourseFieldList = getFieldList(sourseClass);
        for (int i = 0; i < targetFieldList.size(); i++) {
            Field targetField = targetFieldList.get(i);
            Field sourseField = sourseFieldList.get(i);

            if (noCatchedList.contains(targetField.getName())) {
                checkCacheObject = new CheckCacheObject(true, targetObj, sourseObj);
                continue;
            }

            targetField.setAccessible(true);
            sourseField.setAccessible(true);
            Object targetValue = targetField.get(targetObj);
            Object sourseValue = sourseField.get(sourseObj);
            Class<?> fieldClass = targetField.getType();

            boolean isSample = checkFieldSimple(fieldClass);
            if (isSample) {
                DataType dataType = DataType.valueOfString(fieldClass.getSimpleName());

                if (targetValue != null || sourseValue == null) {
                    continue;
                }

                if (DataType.List.equals(dataType)) {
                    Type genericType = targetField.getGenericType();
                    if (genericType != null && genericType instanceof ParameterizedType) {
                        ParameterizedType pt = (ParameterizedType) genericType;
                        //得到泛型里的class类型对象
                        Class<?> genericClazz = (Class<?>) pt.getActualTypeArguments()[0];
                        setListTypeValue(genericClazz, targetValue, sourseValue, targetField,checkCacheObject);
                    }
                }

                setSimpleValue(targetObj, targetField, sourseValue, checkCacheObject, doChange);
            } else {
                if (noCatchedList.contains(targetField.getType().getSimpleName())) {
                    continue;
                }
                if (sourseValue == null) {
                    continue;
                }
                if (targetValue == null) {
                    setSimpleValue(targetObj, targetField, sourseValue, checkCacheObject, doChange);
                } else {
                    combineObj(targetValue , sourseValue, checkCacheObject, false);
                }

            }
        }
    }

    private static void setListTypeValue(Class<?> targetFieldClaz, Object targetValue, Object sourseValue, Field targetField, CheckCacheObject checkCacheObject) throws IllegalAccessException, NoSuchFieldException, InstantiationException {
        boolean isGenericSample = checkFieldSimple(targetFieldClaz);
        DataType genericClassType = DataType.valueOfString(targetFieldClaz.getSimpleName());
        List targetList = (List) targetValue;
        List soutseList = (List) sourseValue;

        handleCacheTypes(targetField, checkCacheObject);

        for (int i = 0; i < soutseList.size(); i++) {
            Object oneSourseValue = soutseList.get(i);
            Object oneTargetValue = null;
            if (targetList.size() <= i) {
                oneTargetValue = targetFieldClaz.newInstance();
            } else {
                oneTargetValue = targetList.get(i);
            }

            if (isGenericSample) {
                if (DataType.List.equals(genericClassType)) {
                    ParameterizedType parameterizedType=(ParameterizedType)targetFieldClaz.getGenericSuperclass();
                    Type genericType = parameterizedType.getActualTypeArguments()[0];
                    if (genericType != null && genericType instanceof ParameterizedType) {
                        ParameterizedType pt = (ParameterizedType) genericType;
                        //得到泛型里泛型的class类型对象
                        Class<?> genericClazz = (Class<?>) pt.getActualTypeArguments()[0];
                        setListTypeValue(genericClazz, targetValue, sourseValue, targetField, checkCacheObject);
                    }

                }
                if (targetList.contains(oneSourseValue)) {
                    continue;
                }
                targetList.add(oneSourseValue);
            } else {

                combineObj(oneTargetValue , oneSourseValue, checkCacheObject, false);
            }
        }
    }

    private static void handleCacheTypes(Field targetField, CheckCacheObject checkCacheObject) {
        if (checkCacheObject != null && checkCacheObject.getMayCache()) {
            ICacheSourceType targetObj = (ICacheSourceType) checkCacheObject.getCombineTargetObj();
            ICacheSourceType sourceObj = (ICacheSourceType) checkCacheObject.getCombineSourceObj();
            FieldNode node = new FieldNode(targetField.getType(),targetField.getName());
            EOptionSourceType targetSourceType = targetObj.getTypeFromNode(node);
            EOptionSourceType sourceSourceType = sourceObj.getTypeFromNode(node);
            if (targetSourceType != null) {
                Set<FieldNode> targetTargetTypeNodeSet = targetObj.getFieldNodeSet(targetSourceType);
                if (targetTargetTypeNodeSet != null) {
                    targetTargetTypeNodeSet.remove(node);
                }
            }
            if (sourceSourceType != null) {
                Set<FieldNode> targetSoutceTypeNodeSet = targetObj.getFieldNodeSet(sourceSourceType);
                if (targetSoutceTypeNodeSet != null) {
                    targetSoutceTypeNodeSet.add(node);
                }
            }


        }
    }

    public static boolean checkFieldSimple(Class<?> fieldClass) {
        Boolean isSample = null;

        DataType dataType = DataType.valueOfString(fieldClass.getSimpleName());

        Class<?> superclass = fieldClass.getSuperclass();
        DataType superDataType;
        if (superclass == null) {
            superDataType = DataType.Unknown;
        } else {
            superDataType = DataType.valueOfString(superclass.getSimpleName());
        }
        if (!DataType.Unknown.equals(dataType) || DataType.Enum.equals(superDataType)) {
            // 本地已知/父类为enmu 说明是简单类型
            isSample = true;
        } else {
            isSample = false;
        }
        return  isSample;
    }

    public static void setSimpleValue(Object targetObj, Field field, Object value, CheckCacheObject checkCacheObject, boolean doChange) throws IllegalAccessException, NoSuchFieldException {
        Class componentClaz = field.getType();
        Class<?> superclass = componentClaz.getSuperclass();

        if (doChange) {
            handleCacheTypes(field, checkCacheObject);
        }

        DataType superDataType;
        if (superclass == null) {
            superDataType = DataType.Unknown;
        } else {
            superDataType = DataType.valueOfString(superclass.getSimpleName());
        }
        if (DataType.Enum.equals(superDataType)) {
            try {
                Method valueOfMethord = componentClaz.getDeclaredMethod("valueOf", String.class);
                String valueString = value.toString();
                valueString = fixJson2J(valueString);
                value = valueOfMethord.invoke(targetObj, valueString);

            } catch (NoSuchMethodException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);
        field.setAccessible(true);

        field.set(targetObj, value);
    }

    public static Widget addAllCacheMap(Widget widget, GsonOption realChartOption) {
        Map<EOptionSourceType, Set<FieldNode>> topicCacheNodeMap = widget.getFieldNodeSourceMap();
        Map<EOptionSourceType, Set<FieldNode>> optionCacheNodeMap = realChartOption.getFieldNodeSourceMap();

        Set<EOptionSourceType> optionTypes = optionCacheNodeMap.keySet();
        for (EOptionSourceType eOptionSourceType : optionTypes) {
            Set<FieldNode> optionNodeSet = optionCacheNodeMap.get(eOptionSourceType);
            Set<FieldNode> topicNodeSet = topicCacheNodeMap.get(eOptionSourceType);
            if (topicNodeSet == null) {
                topicNodeSet = new HashSet<>();
            }
            topicNodeSet.addAll(optionNodeSet);
        }

        return widget;
    }

    public static GridOption fixGridField(Widget widget, GridOption gridOption) {
        List<ChartAxis> axisList = widget.getAxisList();
        for (ChartAxis chartAxis : axisList) {
            List<AxisField> fieldList = chartAxis.getFieldList();
            for (AxisField axisField : fieldList) {
                String caption = axisField.getCaption();
                String name = axisField.getName();
                GridField gridField = new GridField(name);
                gridField.setCaption(caption);
                gridOption.putOneField(gridField);
            }
        }
        return gridOption;
    }
    private static String fixJson2J(String valueString) {
        Set<String> keys = DesignerConstant.fixJson2J.keySet();
        for (String key : keys) {
            int i = valueString.indexOf(key);
            if (i != -1) {
                String replaced = DesignerConstant.fixJson2J.get(key);
                valueString = valueString.replace(key, replaced);
            }
        }

        return  valueString;
    }

    public static List<Field> getFieldList(Class<?> clazz){
        if(null == clazz){
            return null;
        }
        List<Field> fieldList = new LinkedList<Field>();
        Field[] fields = clazz.getDeclaredFields();
        for(Field field : fields){
            fieldList.add(field);
        }
        /** 处理父类字段**/
        Class<?> superClass = clazz.getSuperclass();
        if(superClass.equals(Object.class)){
            return fieldList;
        }

        fieldList.addAll(getFieldList(superClass));
        return fieldList;
    }

    public static Field getField(Class<?> clazz, String name){
        if(null == clazz){
            return null;
        }
        Field realfield = null;
        Field[] fields = clazz.getDeclaredFields();
        for(Field field : fields){
            if (field.getName().toLowerCase().equalsIgnoreCase(name.toLowerCase())) {
                realfield = field;
            }
        }
        /** 处理父类字段**/
        Class<?> superClass = clazz.getSuperclass();
        if(superClass.equals(Object.class)){
            return realfield;
        }
        Field field = getField(superClass, name);
        if (field != null) {
            realfield = field;
        }
        return realfield;
    }

    public static void loadOneTheme(File themeFile) {
        Theme theme = new Theme();
        String fileName = themeFile.getName();
        theme.setName(fileName.substring(0, fileName.lastIndexOf(Util.Dot)));
        String themeJson;
        try {
            themeJson = FileUtils.readFileToString(themeFile, "UTF-8");
        } catch (IOException e) {
            throw  new DesignerFileException(themeFile.getPath());
        }
        JSONObject themeObject = JSONObject.fromObject(themeJson);
        theme.setThemeJson(themeObject);
        DesignerComponentFactory.getInstance().putTheme(theme);
    }
}
