package designer.xml;

import designer.ChartRefected;
import designer.DesignerConstant;
import designer.DesignerUtil;
import designer.exception.DesignerFileException;
import designer.exception.DesignerOptionException;
import designer.exception.DesignerOptionsFileException;
import designer.options.*;
import designer.options.echart.Option;
import designer.options.echart.axis.Axis;
import designer.options.echart.axis.CategoryAxis;
import designer.options.echart.axis.ValueAxis;
import designer.options.echart.json.GsonOption;
import designer.options.echart.series.Series;
import designer.topic.*;
import foundation.config.Configer;
import foundation.data.DataType;
import foundation.util.Util;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.lang.reflect.*;
import java.text.MessageFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * @author kimi
 * @description 设计器xml文件读取类
 * @date 2018-12-21 15:33
 */


public class XmlReader {
    private boolean isPathDefault = false;
    private EDesignerXmlType readerType;
    private String rootPath;
    private String defaultRootPath;
    private String chartEnginePath;

    public  XmlReader(EDesignerXmlType type) {
        this.readerType = type;
        init();
    }

    private void init() {
        rootPath = Configer.getParam(DesignerConstant.DEFAULT_CHART_ROOT_PATH);

        rootPath = rootPath.replaceAll(DesignerConstant.ROOT,Configer.getPath_Application());

        defaultRootPath = Util.filePathJion(rootPath, DesignerConstant.DEFAULT);
        DesignerUtil.checkFileLegality(defaultRootPath);
        DesignerUtil.checkFileLegality(rootPath);
        chartEnginePath = Util.filePathJion(defaultRootPath, Configer.getParam(DesignerConstant.DEFAULT_CHART_ENGINE));
        DesignerUtil.checkFileLegality(chartEnginePath);
    }


    public <T> void read(File file, T obj){

        switch (readerType) {
            case defaultChart:
                loadDefaultFile(file, (GsonOption) obj);
                break;
            case theme:
                break;
            case realChart:
                loadDefaultFile(file, (GsonOption) obj);
//                DesignerUtil.combine((GsonOption) obj);
                break;
            case realTopic:
                loadTopicFile(file, false, (Topic) obj);
                break;
            case defaultTopic:
                loadTopicFile(file, true, (Topic) obj);
                break;
            default:
                break;
        }

    }

    public void loadDefaultFile(File defaultFile, GsonOption defaultOption) {
        SAXReader reader = new SAXReader();

        Document doc = null;
        try {
            doc = reader.read(defaultFile);
            Element root = doc.getRootElement();
            Attribute attribute = root.attribute(DesignerConstant.keyword_pathDefault);
            Attribute type = root.attribute(DesignerConstant.keyword_type);
            if (type != null) {
                System.out.println("haha");
            }
            String pathDefault = attribute.getValue();
            isPathDefault = Util.stringToBoolean(pathDefault);

            Iterator<Element> iterator = root.elementIterator();
            while (iterator.hasNext()) {
                Element child = iterator.next();

                loadOneTierChild(defaultOption, defaultFile, child);
            }
        } catch (DocumentException e) {
            e.printStackTrace();
        }

    }

    private void loadOneTierChild(Object eleOption, File defaultFile, Element child) {
        // eleOption 可能为list 不是实际 数据类型;
        String childName = child.getName();
        try {
            if (childName.equalsIgnoreCase("xaxis")) {
                System.out.println("1");
            }
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
                // chartEnginePath = Util.filePathJion(chartEnginePath, parentPathName);
            }



            loadWhenParamsIsType(eleOption, defaultFile, child);

        } catch (NoSuchFieldException e) {
            e.printStackTrace();
            //TODO 保护性屏蔽
            // throw  new DesignerOptionsFileException(defaultFile.getAbsolutePath(), childName, "options对象无此属性");
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void loadWhenParamsIsType(Object eleOption, File defaultFile, Element child) throws DocumentException, IllegalAccessException, NoSuchFieldException, ClassNotFoundException {
        Attribute typeAttr = child.attribute(DesignerConstant.keyword_type);
        String childName = child.getName();

        ChartRefected componentReflected = load2Options(eleOption, child, defaultFile);

        if (componentReflected == null) {
            System.out.println(childName);
            return;
        }
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
                    loadOneTierChild(componentBean, defaultFile, next);
                }

            }
        }
        else {
            String typeValue = typeAttr.getValue();
            if (DesignerConstant.keyword_type_link.equalsIgnoreCase(typeValue)) {
                Element urlEle = child.element(DesignerConstant.keyelement_url);

                Attribute parentAttribute = child.attribute(DesignerConstant.keyword_parent);
                String parentPathName = null;
                if (parentAttribute != null) {
                    parentPathName = parentAttribute.getValue();
                }

                String eleFilePath = findEleFilePath(defaultFile, parentPathName, childName, urlEle);

                DesignerUtil.checkFileLegality(eleFilePath);

                File eleFile = new File(eleFilePath);

                loadChildXml(eleFile, componentReflected, defaultFile);
            } else if (DesignerConstant.keyword_type_array.equalsIgnoreCase(typeValue)){
                //default 中 数组数据中一个数据的模板  暂定两种 map  和obj　(series 特殊)
                List<Element> objectElementList = child.elements(DesignerConstant.keyelement_object);
                if (childName.equalsIgnoreCase(Series.class.getSimpleName())) {
                    //series
                    setSeriesDefault(eleOption, defaultFile, childName, objectElementList);
                }

            } else if (DesignerConstant.keyelement_object.equalsIgnoreCase(typeValue)) {
                // type = object
            }
        }
    }

    private void setSeriesDefault(Object eleOption, File defaultFile, String childName, List<Element> objectElementList) throws IllegalAccessException, NoSuchFieldException {
        ArrayList<Series> serieObjects = new ArrayList<>();

        for (Element objectElement : objectElementList) {
            if (objectElement == null) {
                return;
            }
            Attribute objectTypeAttr = objectElement.attribute(DesignerConstant.keyword_type);
            if (objectTypeAttr == null) {
                throw  new DesignerOptionsFileException(MessageFormat.format("series Object {0}无 type:", childName));
            }
            String realClazType = objectTypeAttr.getValue();
            String packageName = Series.class.getPackage().getName();
            realClazType = realClazType.substring(0, 1).toUpperCase() + realClazType.substring(1);
            String realClazName = Util.stringJoin(packageName,Util.Dot,realClazType);
            try {
                Class<? extends Series> realSeries = (Class<? extends Series>) Class.forName(realClazName);
                Series realseries = realSeries.newInstance();

                Iterator<Element> iterator = objectElement.elementIterator();
                if (!iterator.hasNext()) {
                    throw new DesignerOptionException(childName + "设值不成功, 并且没有子元素");
                }

                while (iterator.hasNext()) {
                    Element next = iterator.next();
                    loadOneTierChild(realseries, defaultFile, next);
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            }
        }
        setSimpleChildValue(eleOption, childName, serieObjects);
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
        DesignerUtil.setSimpleValue(defaultOption, declaredField, value);
    }


    private void loadChildXml(File eleFile, ChartRefected componentReflected, File defaultFile) throws DocumentException {
        SAXReader reader = new SAXReader();
        Document doc = reader.read(eleFile);
        Element rootElement = doc.getRootElement();
        Object componentBean = componentReflected.getComponentBean();
        loadDetailChild(rootElement,componentBean, defaultFile);
    }

    private void loadDetailChild(Element root, Object componentBean, File defaultFile) {
        Iterator<Element> iterator = root.elementIterator();
        while (iterator.hasNext()) {
            Element child = iterator.next();
            loadOneTierChild(componentBean, defaultFile, child);
        }
    }


    private ChartRefected load2Options(Object defaultOption, Element child, File defaultFile) throws ClassNotFoundException {
        String childName = child.getName();
        checkEssentialAndDefault(child);
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
            //TODO list 需要特殊处理 暂未
            if (DataType.List.equals(dataType) || DataType.ArrayList.equals(dataType) || DataType.LinkedList.equals(dataType)) {
                List<Object> objectList = null;
                if (DataType.ArrayList.equals(dataType)) {
                    objectList = new ArrayList<>();
                } else if (DataType.LinkedList.equals(dataType)) {
                    objectList = new LinkedList<>();
                }
                Type genericType = declaredField.getGenericType();
                if (genericType != null && genericType instanceof ParameterizedType) {
                    ParameterizedType pt = (ParameterizedType) genericType;
                    //得到泛型里的class类型对象
                    Class<?> genericClazz = (Class<?>) pt.getActualTypeArguments()[0];
                    Object genericObject = null;
                    if (genericClazz.getName().equalsIgnoreCase(Axis.class.getName())) {
                        if (childName.equalsIgnoreCase(DesignerConstant.fix_element_xaxis)) {
                            genericObject = new CategoryAxis();
                        } else if (childName.equalsIgnoreCase(DesignerConstant.fix_element_yaxis)){
                            genericObject = new ValueAxis();
                        }

                    }else if (genericClazz.getName().equalsIgnoreCase(Series.class.getName())){
                            //series
                            List<Element> objectElementList = child.elements(DesignerConstant.keyelement_object);
                            setSeriesDefault(defaultOption, defaultFile, childName, objectElementList);
                    }
                    else {
                        genericObject = genericClazz.newInstance();
                    }

                    //loadWhenParamsIsType(defaultOption, defaultFile, child);

                    Iterator<Element> iterator = child.elementIterator();
                    while (iterator.hasNext()) {
                        Element next = iterator.next();
                        loadOneTierChild(genericObject, defaultFile, next);
                        objectList.add(genericObject);
                    }

                }
                setSimpleChildValue(defaultOption, childName, objectList);
            }

            if (!(DataType.Unknown.equals(dataType)) || DataType.Enum.equals(superDataType)) {
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

    private void checkEssentialAndDefault(Element child) {
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

    private String findEleFilePath(File defaultFile, String parentPathName, String childName, Element urlEle) {
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
            if (Util.isEmptyStr(parentPathName)) {
                eleFilePath = Util.filePathJion(chartEnginePath, fileCompleteName);
            } else {
                eleFilePath = Util.filePathJion(chartEnginePath, parentPathName + Util.java_slash +fileCompleteName);
            }

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


    // topic reader
    public void loadTopicFile(File topicFile, boolean isDefault, Topic topic) {
        SAXReader reader = new SAXReader();
        try {
            Document read = reader.read(topicFile);
            Element root = read.getRootElement();
            loadTopicElement(root, topic);
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        if (isDefault) {
            DesignerComponentFactory.getInstance().setBaseTopic(topic);
        } else {
            DesignerUtil.combine(topic);
            topic.invalidate();
        }


    }

    public void loadTopicFile(File topicFile, Topic topic) {
        loadTopicFile(topicFile, false, topic);
    }

    private  void loadTopicElement(Element root, Topic topic) {
        Iterator<Element> iterator = root.elementIterator();
        while (iterator.hasNext()) {
            Element childElement = iterator.next();

            load2Topic(childElement, topic);
        }
    }

    private void load2Topic(Element childElement, Topic topic) {
        String name = DesignerUtil.fixXmlStringValue(childElement.getName());
        String elementStringValue = DesignerUtil.fixXmlStringValue(childElement.getStringValue());
        if (DesignerConstant.HAS_GRID.equalsIgnoreCase(name)) {
            topic.setHasGrid(Util.stringToBoolean(elementStringValue));
        } else if (DesignerConstant.CHART_TYPE.equalsIgnoreCase(name)) {
            List<ChartType> chartTypeList = new ArrayList<>();

            Iterator<Element> chartTypeIterator = childElement.elementIterator();
            while (chartTypeIterator.hasNext()) {
                Element oneChartTypeEle = chartTypeIterator.next();
                String oneChartTypeName = oneChartTypeEle.getName();
                EChartType oneChartType = EChartType.valueOf(oneChartTypeName);

                Attribute minDimensionAttr = oneChartTypeEle.attribute("minDimension");
                int minDimension = Integer.parseInt(minDimensionAttr.getValue());

                Attribute minMensurmentAttr = oneChartTypeEle.attribute("minMensurment");
                int minMensurment = Integer.parseInt(minMensurmentAttr.getValue());

                ChartType chartType = new ChartType(oneChartType);
                chartType.setMinDimensionNum(minDimension).setMinMensurmentNum(minMensurment);

                chartTypeList.add(chartType);
            }
            topic.setChartTypeList(chartTypeList);
        } else if (DesignerConstant.DIMENSIONS.equalsIgnoreCase(name)) {
            try {
                List<String> dimensions = Util.StringToList(elementStringValue);//默认；
                topic.setDimensionList(dimensions);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }else if (DesignerConstant.MEASURMENTS.equalsIgnoreCase(name)) {
            List<Element> mensurmentList = childElement.elements("one");

            for (Element mesurmentEle : mensurmentList) {
                Attribute typeAttr = mesurmentEle.attribute("type");
                EChartType mensurmentType;
                if (typeAttr == null) {
                    mensurmentType = EChartType.bar;
                } else {
                    mensurmentType = EChartType.valueOf(typeAttr.getValue());
                }
                Mensurment mensurment = new Mensurment(mensurmentType);
                String mensurmentName = DesignerUtil.fixXmlStringValue(mesurmentEle.getStringValue());
                mensurment.setName(mensurmentName);
                topic.putMensurment(mensurment);
            }
        }else if (DesignerConstant.DATA_TYPE.equalsIgnoreCase(name)) {
            EDataType eDataType = EDataType.valueOf(elementStringValue);
            topic.setDataType(eDataType);

        }else if (DesignerConstant.DATA_NAME.equalsIgnoreCase(name)) {
            topic.setDataName(elementStringValue.trim());

        }else if (DesignerConstant.FILTERS.equalsIgnoreCase(name)) {
            //TODO 未定
            List<Element> segmentList = childElement.elements("segment");
            for (Element segment : segmentList) {
                Attribute nameAttr = segment.attribute("name");
                String fieldName;
                if (nameAttr == null) {
                    throw new DesignerFileException("节点: " + name);
                } else {
                    fieldName = nameAttr.getValue();
                }
                Attribute linkAttr = segment.attribute("link");
                String link;
                if (linkAttr == null) {
                    link = DesignerConstant.keyword_type_equal;
                } else {
                    link = linkAttr.getValue();
                }
                Attribute typeAttr = segment.attribute("type");
                ESqlValueType type;
                if (typeAttr == null) {
                    type = ESqlValueType.value;
                } else {
                    type = ESqlValueType.valueOf(typeAttr.getValue());
                }

                SegmentPart segmentPart = new SegmentPart();
                segmentPart.setLink(link).setName(fieldName).setValueType(type).setValue(elementStringValue);
                topic.putSegment(segmentPart);
            }

        }else if (DesignerConstant.CHART_OPTION.equalsIgnoreCase(name)) {
            ChartOption chartOption;
            Element path = childElement.element("path");
            if (path != null) {
                String filePath = path.getStringValue();
                if (Util.isEmptyStr(filePath)) {
                    chartOption = new ChartOption();
                } else {
                    Option realOption = DesignerUtil.combineOption(path);
                    chartOption = new ChartOption(realOption);
                }

            } else {
                chartOption = new ChartOption();
            }
            chartOption.setTopicId(topic.getId());
            Element element = childElement.element(DesignerConstant.THEME);
            if (element != null) {
                String themeName = element.getStringValue();
                Theme realTheme = DesignerComponentFactory.getInstance().getThemeByName(themeName);
                chartOption.setTheme(realTheme);
            }
            topic.setChartOption(chartOption);
        }else if (DesignerConstant.GRID_OPTION.equalsIgnoreCase(name)) {
            //TODO 暂无
        }else if (DesignerConstant.CATEGORY_AXIS.equalsIgnoreCase(name)) {
            EDimensonAxis dimensonAxis = EDimensonAxis.valueOf(elementStringValue);
            topic.setDimensonAxis(dimensonAxis);
        }else if (DesignerConstant.NOW_CHART_TYPE.equalsIgnoreCase(name)) {
            EChartType type = EChartType.valueOf(elementStringValue);
            topic.setDefaultType(type);
        }
    }

}
