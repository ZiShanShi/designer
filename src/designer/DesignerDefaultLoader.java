package designer;

import designer.exception.DesignerOptionException;
import designer.exception.DesignerOptionsFileException;
import designer.options.DesignerComponentFactory;
import designer.options.echart.json.GsonOption;
import designer.topic.Topic;
import foundation.config.Configer;
import foundation.config.Preloader;
import foundation.data.DataType;
import foundation.util.Util;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.text.ParseException;
import java.util.Iterator;
import java.util.Set;

/**
 * @author kimi
 * @description default文件;初始加载类
 * @date 2018-12-17 20:31
 */


public class DesignerDefaultLoader extends Preloader {
    private boolean isPathDefault = false;

    @Override
    public void load() {
        initOptionDefault();
    }

    private void initOptionDefault() {

        String rootPath = Configer.getParam(DesignerConstant.DEFAULT_CHART_ROOT_PATH);
        String suffix = Configer.getParam(DesignerConstant.DEFAULT_CHART_SUFFIX);
        rootPath = rootPath.replaceAll(DesignerConstant.ROOT,Configer.getPath_Application());
        String defaultPartPath = Configer.getParam(DesignerConstant.DEFAULT_CHART_PART_PATH);
        String topicPartPath = Configer.getParam(DesignerConstant.DEFAULT_Topic_PART_PATH);
        String themePartPath = Configer.getParam(DesignerConstant.DEFAULT_Theme_PART_PATH);
        String defaultEngine = Configer.getParam(DesignerConstant.DEFAULT_CHART_ENGINE);

        DesignerUtil.checkFileLegality(rootPath);
        String TopicRootPath = Util.filePathJion(rootPath, topicPartPath);
        DesignerUtil.checkFileLegality(TopicRootPath);

        String topicDefaultFilePath = Util.filePathJion(TopicRootPath, Util.stringJoin(defaultEngine, Util.Dot, suffix));
        File topicDefualtFile = DesignerUtil.checkFileLegality(topicDefaultFilePath);

        String themeRootPath = Util.filePathJion(rootPath, themePartPath);
        File themeRootFile = DesignerUtil.checkFileLegality(themeRootPath);

        String defaultPath = Util.filePathJion(rootPath, DesignerConstant.DEFAULT);
        DesignerUtil.checkFileLegality(defaultPath);

        String chartEngintPath = Util.filePathJion(defaultPath, defaultEngine);
        DesignerUtil.checkFileLegality(chartEngintPath);

        String chartTemplePath = Util.filePathJion(defaultPath, Util.stringJoin(defaultEngine, Util.Dot, suffix));
        File chartDefaultFile = DesignerUtil.checkFileLegality(chartTemplePath);


        GsonOption option = loadDefaultFile(chartDefaultFile, chartEngintPath);
        DesignerComponentFactory instance = DesignerComponentFactory.getInstance();

        loadThemeFile(themeRootFile);
        instance.setDefautOption(option);

        Topic defaultTopic = new Topic(DesignerConstant.DEFAULT, DesignerConstant.DEFAULT);
        DesignerUtil.loadTopicFile(topicDefualtFile, true, defaultTopic);


    }

    private void loadThemeFile(File themeRootFile) {
        File[] files = themeRootFile.listFiles();
        for (File themeFile : files) {
            //TODO add theme

        }

    }


    private GsonOption loadDefaultFile(File defaultFile, String chartEngintPath) {
        SAXReader reader = new SAXReader();
        GsonOption defaultOption = new GsonOption();

        Document doc = null;
        try {
            doc = reader.read(defaultFile);
            Element root = doc.getRootElement();
            Attribute attribute = root.attribute(DesignerConstant.keyword_pathDefault);
            String pathDefault = attribute.getValue();
            isPathDefault = Util.stringToBoolean(pathDefault);

            Iterator<Element> iterator = root.elementIterator();
            while (iterator.hasNext()) {
                Element child = iterator.next();

                loadOneTierChild(defaultOption,defaultFile, chartEngintPath, child);
            }
        } catch (DocumentException e) {
            e.printStackTrace();
        }

        return defaultOption;
    }

    private void loadOneTierChild(Object eleOption, File defaultFile, String chartEngintPath, Element child) {
        String childName = child.getName();
        try {
            if (DesignerConstant.keyelement_IF.equalsIgnoreCase(childName)) {
                Iterator<Attribute> attributeIterator = child.attributeIterator();
                boolean checkd = true;
                while (attributeIterator.hasNext()) {
                    Attribute childOneAttribute = attributeIterator.next();
                    String attributeName = childOneAttribute.getName();
                    Field attributeField = eleOption.getClass().getDeclaredField(attributeName);
                    attributeField.setAccessible(true);
                    Object eleValue = attributeField.get(eleOption);

                    Class<?> attributeFieldClaz = attributeField.getType();
                    Class<?> superclass = attributeFieldClaz.getSuperclass();
                    String eleValueString = null;
                    if (superclass != null && superclass.getSimpleName().equalsIgnoreCase(Enum.class.getSimpleName())) {
                        try {
                            Class<?>[] nameParams = {};
                            Object [] invokeParams = {};
                            Method nameMethord = attributeFieldClaz.getMethod("name",nameParams);

                            eleValueString = (String) nameMethord.invoke(eleValue, invokeParams);
                        } catch (NoSuchMethodException e) {
                            e.printStackTrace();
                        } catch (InvocationTargetException e) {
                            e.printStackTrace();
                        }

                    } else {
                        eleValueString = (String) eleValue;
                    }
                    String attributeValue = childOneAttribute.getValue();
                    if (!eleValueString.equalsIgnoreCase(attributeValue)) {
                        checkd = false;
                        return;
                    }
                }
                if (!checkd) {
                    return;
                }
            }

            Attribute parentAttribute1 = child.attribute(DesignerConstant.keyword_parent);
            if (parentAttribute1 != null) {
                String parentPathName = parentAttribute1.getValue();
               // chartEngintPath = Util.filePathJion(chartEngintPath, parentPathName);
            }

            ChartRefected componentReflected = load2Options(eleOption, child);
            if (componentReflected == null) {
                System.out.println(childName);
                return;
            }
            Attribute typeAttr = child.attribute(DesignerConstant.keyword_type);
            if (typeAttr == null) {
                if (componentReflected.isSimple()) {
                    boolean seted = trySetSimpleValue(eleOption, child, childName, componentReflected);
                    if (!seted) {
                        throw new DesignerOptionException(childName + "设值不成功");
                    }

                } else {
                    //不是基础类型 也没有type 还有type是Object类型 说明是本xml内定义的
                    Object componentBean = componentReflected.getComponentBean();
                    boolean seted = true;
                    if (componentReflected.getChartComponentClaz().equals(Object.class)) {
                        seted = trySetSimpleValue(eleOption, child, childName, componentReflected);

                    }

                    Iterator<Element> iterator = child.elementIterator();
                    if (!seted && !iterator.hasNext()) {
                        throw new DesignerOptionException(childName + "设值不成功, 并且没有子元素");
                    }

                    while (iterator.hasNext()) {
                        Element next = iterator.next();
                        loadOneTierChild(componentBean, defaultFile, chartEngintPath, next);
                    }

                }
            }
            else {
                String typeValue = typeAttr.getValue();
                if (DesignerConstant.keyword_type_link.equalsIgnoreCase(typeValue)) {
                    Element urlEle = child.element(DesignerConstant.keyelement_url);

                    Attribute parentAttribute = child.attribute(DesignerConstant.keyword_parent);
                    if (parentAttribute != null) {
                        String parentPathName = parentAttribute.getValue();
                        chartEngintPath = Util.filePathJion(chartEngintPath, parentPathName);
                    }

                    String eleFilePath = findEleFilePath(defaultFile, chartEngintPath, childName, urlEle);

                    DesignerUtil.checkFileLegality(eleFilePath);

                    File eleFile = new File(eleFilePath);

                    loadChildXml(eleFile, componentReflected, defaultFile, chartEngintPath);
                }
            }
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
            //TODO 保护性屏蔽
            // throw  new DesignerOptionsFileException(defaultFile.getAbsolutePath(), childName, "options对象无此属性");
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }

    private boolean trySetSimpleValue(Object eleOption, Element child, String childName, ChartRefected componentReflected) {
        boolean seted = true;
        String stringValue = child.getStringValue();
        stringValue = DesignerUtil.fixXmlStringValue(stringValue);
        if (Util.isEmptyStr(stringValue)) {
            return seted;
        }
        try {
            Object value = Util.StringToOther(stringValue, componentReflected.getChartComponentClaz());

            setSimpleChildValue(eleOption, childName, value);
        } catch (IllegalAccessException e) {
            seted = false;
        } catch (NoSuchFieldException e) {
            seted = false;
        } catch (ParseException e) {
            seted = false;
        }
        return seted;
    }

    private void setSimpleChildValue(Object defaultOption, String childName, Object value) throws IllegalAccessException, NoSuchFieldException {
        Field declaredField = getReflectField(defaultOption, childName);
        Class componentClaz = declaredField.getType();
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
                value = valueOfMethord.invoke(defaultOption, valueString);

            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(declaredField, declaredField.getModifiers() & ~Modifier.FINAL);
        declaredField.setAccessible(true);

        declaredField.set(defaultOption, value);



    }

    private String fixJson2J(String valueString) {
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

    private void loadChildXml(File eleFile, ChartRefected componentReflected, File defaultFile, String chartEngintPath) throws DocumentException {
        SAXReader reader = new SAXReader();
        Document doc = reader.read(eleFile);
        Element rootElement = doc.getRootElement();
        Object componentBean = componentReflected.getComponentBean();
        loadDetailChild(rootElement,componentBean, defaultFile, chartEngintPath);
    }

    private void loadDetailChild(Element root, Object componentBean, File defaultFile, String chartEngintPath) {
        Iterator<Element> iterator = root.elementIterator();
        while (iterator.hasNext()) {
            Element child = iterator.next();
            loadOneTierChild(componentBean, defaultFile, chartEngintPath, child);
        }
    }


    private ChartRefected load2Options(Object defaultOption, Element child) {
        String childName = child.getName();
        Attribute essentialAttr = child.attribute(DesignerConstant.keyword_essential);
        Attribute defaultAttr = child.attribute(DesignerConstant.keyword_default);
        boolean isEessential = false; //TODO 默认是不必备
        boolean isDefault = false; //TODO 默认是不默认有

        if (essentialAttr != null) {
            String essentialValue = essentialAttr.getValue();
            isEessential = Util.stringToBoolean(essentialValue);
        }
        if (defaultAttr != null) {
            String defaultValue = defaultAttr.getValue();
            isDefault = Util.stringToBoolean(defaultValue);
        }

        /*if (!(isEessential || isDefault)) {
            //TODO 既不是 必备的 也不是默认的 不加defaultOption去
            return null;
        }*/
        ChartRefected chartRefected = null;

        try {
            Field declaredField = getReflectField(defaultOption, childName);

            Class<? extends Object> fieldClass = declaredField.getType();

            chartRefected = new ChartRefected(fieldClass);
            DataType dataType = DataType.valueOfString(fieldClass.getSimpleName());

            Class<?> superclass = fieldClass.getSuperclass();
            DataType superDataType;
            if (superclass == null) {
                superDataType = DataType.Unknown;
            } else {
                superDataType = DataType.valueOfString(superclass.getSimpleName());
            }


            Object componentObject;
            if (!DataType.Unknown.equals(dataType) || DataType.Enum.equals(superDataType)) {
                chartRefected.setSimple(true);
                Field modifiersField = Field.class.getDeclaredField("modifiers");
                modifiersField.setAccessible(true);
                modifiersField.setInt(declaredField, declaredField.getModifiers() & ~Modifier.FINAL);
                componentObject = Util.getDefaultValue(dataType);

            }else {
                chartRefected.setSimple(false);
                componentObject = fieldClass.newInstance();

            }
            chartRefected.setComponentBean(componentObject);

            declaredField.setAccessible(true);
            declaredField.set(defaultOption, componentObject);
        } catch (NoSuchFieldException e) {
            //TODO 保护性屏蔽
//            e.printStackTrace();
//            throw  new DesignerOptionException("filed:" + childName);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            throw  new DesignerOptionException("无法set值 filed:" + childName);
        } catch (InstantiationException e) {
            e.printStackTrace();
            throw  new DesignerOptionException("无法创建对象--filed:" + childName);
        }
        return chartRefected;
    }

    private Field getReflectField(Object defaultOption, String childName) throws NoSuchFieldException {
        childName = fixEleNameName(childName);
        Class<? extends Object> optionClass = defaultOption.getClass();
        Field declaredField = null;
        try {
            declaredField = optionClass.getDeclaredField(childName);
        } catch (NoSuchFieldException e) {
            //本类中找不到此字段 从父类中找
            while (optionClass != null) {
                optionClass = optionClass.getSuperclass();
                if (optionClass == null) {
                    throw  new NoSuchFieldException();
                }
                declaredField = optionClass.getDeclaredField(childName);
                if (declaredField != null) {
                    break;
                }
            }
        }
        return declaredField;
    }

    private String fixEleNameName(String childName) {
        if (childName.toLowerCase().endsWith(DesignerConstant.fix_element_axis)) {
            return DesignerConstant.fix_element_axis;
        } else {
            return  childName;
        }
    }

    private String findEleFilePath(File defaultFile, String chartEngintPath, String childName, Element urlEle) {
        String eleFilePath;
        if (isPathDefault) {
            String eleFileName;
            if (urlEle != null) {
                eleFileName = urlEle.getStringValue();
            }else {
                eleFileName = DesignerConstant.DEFAULT;
            }
            String suffix = Configer.getParam(DesignerConstant.DEFAULT_CHART_SUFFIX);
            String fileCompleteName= Util.filePathJion(childName, Util.stringJoin(eleFileName, Util.Dot, suffix));
            eleFilePath = Util.filePathJion(chartEngintPath, fileCompleteName);
        }else{
            if (urlEle != null) {
                eleFilePath = urlEle.getStringValue();
            }
            else {
                throw new DesignerOptionsFileException(defaultFile.getAbsolutePath(), childName, "无数据");
            }
        }
        return eleFilePath;
    }

}
