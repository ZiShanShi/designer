package designer;

import designer.exception.DesignerBaseException;
import designer.exception.DesignerFileException;
import designer.options.ChartOption;
import designer.options.DesignerComponentFactory;
import designer.options.echart.Option;
import designer.topic.Topic;
import foundation.data.DataType;
import foundation.util.Util;
import org.dom4j.Element;

import java.io.File;
import java.lang.reflect.*;
import java.text.MessageFormat;
import java.util.List;
import java.util.Set;

/**
 * @author kimi
 * @description 设计器工具类
 * @date 2018-12-17 14:20
 */


public class DesignerUtil {

    public static String getOptionFilePath(String id) {
        return id;
    }

    public static String fixXmlStringValue(String rawValue) {
        rawValue = rawValue.replaceAll(Util.String_Escape_newLine,Util.String_Space);
        rawValue = rawValue.replaceAll(Util.String_Escape_newSpace,Util.String_Space);
        rawValue = rawValue.trim();
        if (Util.isEmptyStr(rawValue)) {
            return null;
        }else {
            return rawValue;
        }
    }
    public static File checkFileLegality(String filePath) {
        if (Util.isEmptyStr(filePath)) {
            throw new DesignerFileException("path is empty");
        }
        File file = new File(filePath);
        if (!file.exists()) {
            throw new DesignerFileException(filePath);
        }
        return file;
    }

    public static Option combineOption(Element path) {
        return null;
    }

    public static void combine(Topic topic) {
        //TODO 组合成完整的topic
        combine(topic, DesignerComponentFactory.getInstance().getBaseTopic());
    }
    public static void combine(ChartOption option) {
        //TODO 组合成完整的option
        combine(option, DesignerComponentFactory.getInstance().getDefautOption());
    }

    public static void combine(Topic topic, Topic baseTopic) {
        //TODO 组合成完整的topic
        try {
            combineObj(topic, baseTopic);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    public static void combine(ChartOption option, Option baseOption) {
        //TODO 组合成完整的option
        try {
            combineObj(option, baseOption);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }

    }
    public static  void combineObj (Object targetObj, Object sourseObj) throws IllegalAccessException, NoSuchFieldException {
        Class<?> targetClass = targetObj.getClass();
        Class<?> sourseClass = sourseObj.getClass();

        String targretClassName = targetClass.getName();
        String sourseClassName = sourseClass.getName();
        if (!targretClassName.equalsIgnoreCase(sourseClassName)) {
            throw  new DesignerBaseException(MessageFormat.format("combineObj 输入类型不一样 target: {0}; sourse:{1}",targretClassName,sourseClassName));
        }


        Field[] targetFieldArray = targetClass.getDeclaredFields();
        Field[] sourseFieldArray = sourseClass.getDeclaredFields();

        for (int i = 0; i < targetFieldArray.length; i++) {
            Field targetField = targetFieldArray[i];
            Field sourseField = sourseFieldArray[i];
            targetField.setAccessible(true);
            sourseField.setAccessible(true);
            Object targetValue = targetField.get(targetObj);
            Object sourseValue = sourseField.get(sourseObj);
            Class<?> fieldClass = targetField.getType();

            boolean isSample = checkFieldSimple(fieldClass);
            if (isSample) {
                DataType dataType = DataType.valueOfString(fieldClass.getSimpleName());
                if (DataType.List.equals(dataType)) {
                    Type genericType = targetField.getGenericType();
                    if (genericType != null && genericType instanceof ParameterizedType) {
                        ParameterizedType pt = (ParameterizedType) genericType;
                        //得到泛型里的class类型对象
                        Class<?> genericClazz = (Class<?>) pt.getActualTypeArguments()[0];
                        setListTypeValue(genericClazz, targetValue, sourseValue);
                    }
                }
                if (targetValue != null || sourseValue == null) {
                    continue;
                }
                DesignerUtil.setSimpleValue(targetObj, targetField, sourseValue);
            } else {
                if (sourseValue == null) {
                    continue;
                }
                combineObj(targetValue , sourseValue);
            }
        }
    }

    private static void setListTypeValue(Class<?> targetFieldClaz, Object targetValue, Object sourseValue) throws IllegalAccessException, NoSuchFieldException {
        boolean isGenericSample = checkFieldSimple(targetFieldClaz);
        DataType genericClassType = DataType.valueOfString(targetFieldClaz.getSimpleName());
        List targetList = (List) targetValue;
        List soutseList = (List) sourseValue;
        for (Object oneSourseValue : soutseList) {
            if (isGenericSample) {
                if (DataType.List.equals(genericClassType)) {
                    ParameterizedType parameterizedType=(ParameterizedType)targetFieldClaz.getGenericSuperclass();
                    Type genericType = parameterizedType.getActualTypeArguments()[0];
                    if (genericType != null && genericType instanceof ParameterizedType) {
                        ParameterizedType pt = (ParameterizedType) genericType;
                        //得到泛型里泛型的class类型对象
                        Class<?> genericClazz = (Class<?>) pt.getActualTypeArguments()[0];
                        setListTypeValue(genericClazz, targetValue, sourseValue);
                    }

                }
                if (targetList.contains(oneSourseValue)) {
                    continue;
                }
                targetList.add(oneSourseValue);
            } else {
                combineObj(targetValue , sourseValue);
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

    public static void setSimpleValue(Object targetObj, Field field, Object value) throws IllegalAccessException, NoSuchFieldException {
        Class componentClaz = field.getType();
        Class<?> superclass = componentClaz.getSuperclass();
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

            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);
        field.setAccessible(true);

        field.set(targetObj, value);
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





}
