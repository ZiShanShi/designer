package designer.xml;

import designer.ChartRefected;
import designer.DesignerComponentFactory;
import designer.DesignerConstant;
import designer.DesignerUtil;
import designer.cache.EOptionSourceType;
import designer.cache.FieldNode;
import designer.exception.DesignerFileException;
import designer.exception.DesignerOptionException;
import designer.exception.DesignerOptionsFileException;
import designer.options.*;
import designer.options.echart.axis.Axis;
import designer.options.echart.axis.CategoryAxis;
import designer.options.echart.axis.ValueAxis;
import designer.options.echart.code.Align;
import designer.options.echart.json.GsonOption;
import designer.options.echart.series.Series;
import designer.widget.*;
import designer.widget.theme.Theme;
import foundation.config.Configer;
import foundation.data.DataType;
import foundation.data.Page;
import foundation.util.Util;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.lang.reflect.*;
import java.text.MessageFormat;
import java.util.*;

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
    private List<String> noInjectElementList;
    private Set<FieldNode> fieldNodeList;

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

        fieldNodeList = Collections.synchronizedSet(new HashSet());

        noInjectElementList = new ArrayList<>();
        noInjectElementList.add("formatter");
        noInjectElementList.add("rich");
    }

    public void putSourcrFieldClazName(FieldNode fieldNode) {
        if (fieldNodeList == null) {
            fieldNodeList = new HashSet<>();
        }

        fieldNodeList.add(fieldNode);

    }

    public Set<FieldNode> getFieldNodeSet() {
        return fieldNodeList;
    }

    private EOptionSourceType checkOptionSourceType() {
        if (readerType == null) {
            return null;
        }
        EOptionSourceType type = null;
        switch (readerType) {
            case defaultChart:
            case defaultWidget:
                type = EOptionSourceType.Default;
                break;
            case realWidget:
            case realChart:
                type = EOptionSourceType.Changed;
                break;


            default:
                break;

        }
        return type;
    }

    public <T> void read(File file, T obj){
        switch (readerType) {
            case defaultChart:
            case realChart:
                loadDefaultFile(file, (GsonOption) obj);
                ((GsonOption) obj).putFieldNodeSourceSet(checkOptionSourceType(),getFieldNodeSet());
                break;
            case realWidget:
                loadWidgetFile(file, false, (Widget) obj);
                break;
            case defaultWidget:
                loadWidgetFile(file, true, (Widget) obj);
                ((Widget) obj).putFieldNodeSourceSet(checkOptionSourceType(),getFieldNodeSet());
                break;
            default:
                break;
        }

    }


    private void loadDefaultFile(File defaultFile, GsonOption defaultOption) {
        SAXReader reader = new SAXReader();

        Document doc = null;
        try {
            doc = reader.read(defaultFile);
            Element root = doc.getRootElement();
            Attribute pathDefaultAttr = root.attribute(DesignerConstant.keyword_pathDefault);
            if (pathDefaultAttr != null) {
                String pathDefault = pathDefaultAttr.getValue();
                isPathDefault = Util.stringToBoolean(pathDefault);
            }

            Iterator<Element> iterator = root.elementIterator();
            while (iterator.hasNext()) {
                Element child = iterator.next();

                loadOneTierChild(defaultOption, defaultFile, child, null);
            }
        } catch (DocumentException e) {
            e.printStackTrace();
        }

    }

    private void loadOneTierChild(Object eleOption, ChartRefected componentReflected, File defaultFile, Element child, FieldNode node) throws NoSuchFieldException, IllegalAccessException {
        // eleOption 可能为list 不是实际 数据类型;
        if (componentReflected != null && componentReflected.isList()) {
            //list 不适用 eleOption
            Class<?> genericClaz = componentReflected.getChartComponentClaz();
            List listBean = (List) componentReflected.getComponentBean();
            Attribute typeAttr = child.attribute(DesignerConstant.keyword_type);
            if (typeAttr == null) {
                System.out.println(MessageFormat.format("{0} is not have type class name is {1}",child.getName(), componentReflected.getChartComponentClaz()));
                return;
                //throw new DesignerFileException("series type is null");
            }
            String typeValue = typeAttr.getValue();
            String childName = child.getName();

            //default 中 数组数据中一个数据的模板  暂定两种 map  和obj　(series 特殊)
            List objectElementList = child.elements(DesignerConstant.keyelement_object);
            if (genericClaz.equals(Series.class)) {
                //series
                Object listParentObj = componentReflected.getListParentObj();
                setSeriesDefault(listParentObj, defaultFile, childName, objectElementList, node);

            } else if (genericClaz.equals(Axis.class)) {
                //axis 默认 x category  y value
                Axis oneAxis = null;
                if (childName.equalsIgnoreCase(DesignerConstant.fix_element_xAxis)) {
                    oneAxis = new CategoryAxis();
                } else if (childName.equalsIgnoreCase(DesignerConstant.fix_element_yAxis)){
                    oneAxis = new ValueAxis();
                }
                listBean.add(oneAxis);
                Object listParentObj = componentReflected.getListParentObj();
                setSimpleChildValue(listParentObj, childName, listBean);
            }
        } else {
            loadOneTierChild(eleOption, defaultFile, child, node);
        }
    }

    private void loadOneTierChild(Object eleOption, File defaultFile, Element child, FieldNode preNode) {
        String childName = child.getName();

        try {
            if (noInjectElementList.contains(childName)) {
                return;
            }
            boolean show = loadTypeIsShow(eleOption, child);

            boolean typeIsIfLoaded = loadTypeIsIf(eleOption, child);

            if (!typeIsIfLoaded || !show) {
                return;
            }

            Attribute parentAttribute1 = child.attribute(DesignerConstant.keyword_parent);
            if (parentAttribute1 != null) {
                String parentPathName = parentAttribute1.getValue();
                // chartEnginePath = Util.filePathJion(chartEnginePath, parentPathName);
            }

            loadWhenParamsIsType(eleOption, defaultFile, child, preNode);

        } catch (NoSuchFieldException | DocumentException | ClassNotFoundException | IllegalAccessException e) {
            e.printStackTrace();
            //TODO 保护性屏蔽
            // throw  new DesignerOptionsFileException(defaultFile.getAbsolutePath(), childName, "options对象无此属性");
        }
    }

    private boolean loadTypeIsShow(Object eleOption, Element child) {
        Attribute showAttr = child.attribute(DesignerConstant.keyelement_Show);
        if (showAttr == null) {
            return  true;
        }
        return  Util.stringToBoolean(showAttr.getValue(), true);
    }

    private boolean loadTypeIsIf(Object eleOption, Element child) throws NoSuchFieldException, IllegalAccessException {
        String childName = child.getName();
        boolean checkd = true;
        if (DesignerConstant.keyelement_IF.equalsIgnoreCase(childName)) {
            Iterator<Attribute> attributeIterator = child.attributeIterator();
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
                    } catch (NoSuchMethodException | InvocationTargetException e) {
                        e.printStackTrace();
                    }

                } else {
                    eleValueString = (String) eleValue;
                }
                String attributeValue = childOneAttribute.getValue();
                assert eleValueString != null;
                if (!eleValueString.equalsIgnoreCase(attributeValue)) {
                    checkd = false;
                }
            }

        }
        return checkd;
    }

    private void loadWhenParamsIsType(Object eleOption, File defaultFile, Element child, FieldNode preNode) throws DocumentException, IllegalAccessException, NoSuchFieldException, ClassNotFoundException {
        Attribute typeAttr = child.attribute(DesignerConstant.keyword_type);
        String childName = child.getName();

        ChartRefected componentReflected = load2Options(eleOption, child);

        if (componentReflected == null) {
            System.out.println(childName);
            return;
        }

        FieldNode fieldNode = new FieldNode(componentReflected.getChartComponentClaz(), childName);
        if (preNode != null) {
            fieldNode.setPreName(preNode);
            preNode.setParent(true);
        }
        putSourcrFieldClazName(fieldNode);

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

                Iterator iterator = child.elementIterator();
                if (!seted && !iterator.hasNext()) {
                    throw new DesignerOptionException(childName + "设值不成功, 并且没有子元素");
                }

                while (iterator.hasNext()) {
                    Element next = (Element) iterator.next();
                    loadOneTierChild(componentBean, defaultFile, next, fieldNode);
                }

            }
        }
        else {
            String typeValue = typeAttr.getValue();
            if (DesignerConstant.keyword_type_file.equalsIgnoreCase(typeValue)) {
                Element urlEle = child.element(DesignerConstant.keyelement_url);

                Attribute parentAttribute = child.attribute(DesignerConstant.keyword_parent);
                String parentPathName = null;
                if (parentAttribute != null) {
                    parentPathName = parentAttribute.getValue();
                }

                String eleFilePath = findEleFilePath(defaultFile, parentPathName, childName, urlEle);

                DesignerUtil.checkFileLegality(eleFilePath);

                File eleFile = new File(eleFilePath);

                loadChildXml(eleFile, componentReflected, defaultFile, fieldNode);
            } else if (DesignerConstant.keyword_type_array.equalsIgnoreCase(typeValue)){
                //default 中 数组数据中一个数据的模板  暂定两种 map  和obj　(series 特殊)
                List<Element> objectElementList = child.elements(DesignerConstant.keyelement_object);
                if (childName.equalsIgnoreCase(Series.class.getSimpleName())) {
                    //series
                    setSeriesDefault(eleOption, defaultFile, childName, objectElementList, fieldNode);
                }

            } else if (DesignerConstant.keyelement_object.equalsIgnoreCase(typeValue)) {
                // type = object
            }
        }
    }

    private void setSeriesDefault(Object eleOption, File defaultFile, String childName, List<Element> objectElementList, FieldNode preNode) throws IllegalAccessException, NoSuchFieldException {
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
            Class<? extends Series> realSeries = null;
            try {
                realSeries = (Class<? extends Series>) Class.forName(realClazName);
            } catch (ClassNotFoundException e) {
                try {
                    realSeries = (Class<? extends Series>) Class.forName(realClazName + "Series");
                } catch (ClassNotFoundException e1) {
                    e1.printStackTrace();
                }
            }try {
                Series realseries = realSeries.newInstance();

                Iterator iterator = objectElement.elementIterator();
                if (!iterator.hasNext()) {
                    throw new DesignerOptionException(childName + "设值不成功, 并且没有子元素");
                }

                while (iterator.hasNext()) {
                    Element next = (Element) iterator.next();
                    loadOneTierChild(realseries, defaultFile, next, preNode);
                }
                serieObjects.add(realseries);
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
            return true;
        }
        try {
            Object value = Util.StringToOther(stringValue, componentReflected.getChartComponentClaz());

            setSimpleChildValue(eleOption, childName, value);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            seted = false;
        }
        return seted;
    }

    private void setSimpleChildValue(Object defaultOption, String childName, Object value) throws IllegalAccessException, NoSuchFieldException {
        Field declaredField = getReflectField(defaultOption, childName);
        DesignerUtil.setSimpleValue(defaultOption, declaredField, value, null, false);

    }


    private void loadChildXml(File eleFile, ChartRefected componentReflected, File defaultFile, FieldNode preNode) throws DocumentException, NoSuchFieldException, IllegalAccessException {
        SAXReader reader = new SAXReader();
        Document doc = reader.read(eleFile);
        Element rootElement = doc.getRootElement();
        Object componentBean = componentReflected.getComponentBean();
        loadDetailChild(rootElement,componentReflected, componentBean, defaultFile, preNode);
    }

    private void loadDetailChild(Element root, ChartRefected componentReflected, Object componentBean, File defaultFile, FieldNode preNode) throws NoSuchFieldException, IllegalAccessException {
        Iterator<Element> iterator = root.elementIterator();
        while (iterator.hasNext()) {
            Element child = iterator.next();
            loadOneTierChild(componentBean, componentReflected, defaultFile, child, preNode);
        }
    }


    private ChartRefected load2Options(Object defaultOption, Element child) throws ClassNotFoundException {
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
            //TODO list 需要特殊处理 暂未处理完美
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

                    chartRefected.setComponentBean(objectList);
                    chartRefected.setChartComponentClaz(genericClazz);
                    chartRefected.setSimple(true);
                    chartRefected.setList(true);
                    chartRefected.setListParentObj(defaultOption);
                    return chartRefected;
                }
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
        if (childName.toLowerCase().endsWith(DesignerConstant.fix_element_Axis)) {
            return DesignerConstant.fix_element_Axis;
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


    // widget reader
    private void loadWidgetFile(File topicFile, boolean isDefault, Widget widget) {
        SAXReader reader = new SAXReader();
        try {
            Document read = reader.read(topicFile);
            Element root = read.getRootElement();
            loadWidgetElement(root, widget, isDefault);
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        if (isDefault) {
            DesignerComponentFactory.getInstance().setBaseWidget(widget);
        } else {
            //widget.putFieldNodeSourceSet(checkOptionSourceType(),getFieldNodeSet());
            DesignerUtil.combine(widget);
            widget = DesignerUtil.addAllCacheMap(widget, widget.getChartOption().getRealChartOption());
            widget.invalidate();
        }


    }


    public void loadWidgetFile(File topicFile, Widget widget) {
        loadWidgetFile(topicFile, false, widget);
    }

    private void loadWidgetElement(Element root, Widget widget, boolean isDefault) {
        Iterator iterator = root.elementIterator();
        while (iterator.hasNext()) {
            Element childElement = (Element) iterator.next();
            load2Widegt(childElement, widget, isDefault);
        }
    }



    private void load2Widegt(Element childElement, Widget widget, boolean isDefault) {
        // writer node is not null
        String name = DesignerUtil.fixXmlStringValue(childElement.getName());
        String elementStringValue = DesignerUtil.fixXmlStringValue(childElement.getStringValue());
        if (DesignerConstant.HAS_GRID.equalsIgnoreCase(name)) {
            String fieldName = widget.setHasGrid(Util.stringToBoolean(elementStringValue));
            FieldNode rootNode = new FieldNode(boolean.class,fieldName);
            putSourcrFieldClazName(rootNode);

        } else if (DesignerConstant.CHART_TYPE.equalsIgnoreCase(name)) {
            List<ChartType> chartTypeList = new ArrayList<>();

            Iterator<Element> chartTypeIterator = childElement.elementIterator();
            while (chartTypeIterator.hasNext()) {
                Element oneChartTypeEle = chartTypeIterator.next();
                String oneChartTypeName = oneChartTypeEle.getName();
                EChartType oneChartType = EChartType.valueOf(oneChartTypeName);

                ChartType chartType = new ChartType(oneChartType);
                Attribute minDimensionAttr = oneChartTypeEle.attribute(DesignerConstant.keyelement_minDimension);
                if (minDimensionAttr != null) {
                    int minDimension = Integer.parseInt(minDimensionAttr.getValue());
                    chartType.setMinDimensionNum(minDimension);
                }

                Attribute minMensurmentAttr = oneChartTypeEle.attribute(DesignerConstant.keyelement_minMensurment);
                if (minMensurmentAttr != null) {
                    int minMensurment = Integer.parseInt(minMensurmentAttr.getValue());
                    chartType.setMinMensurmentNum(minMensurment);
                }

                Attribute maxDimensionAttr = oneChartTypeEle.attribute(DesignerConstant.keyelement_maxDimension);
                if (maxDimensionAttr != null) {
                    int maxDimension = Integer.parseInt(maxDimensionAttr.getValue());
                    chartType.setMaxDimensionNum(maxDimension);
                }

                Attribute maxMensurmentAttr = oneChartTypeEle.attribute(DesignerConstant.keyelement_maxMensurment);
                if (maxMensurmentAttr != null) {
                    int maxMensurment = Integer.parseInt(maxMensurmentAttr.getValue());
                    chartType.setMaxMensurmentNum(maxMensurment);
                }

                String stringValue = oneChartTypeEle.getStringValue();
                Boolean isOpen = Util.stringToBoolean(stringValue);

                chartType.setOpen(isOpen);

                chartTypeList.add(chartType);
            }
            String fieldName = widget.setChartTypeList(chartTypeList);
            FieldNode rootNode = new FieldNode(List.class,fieldName);
            putSourcrFieldClazName(rootNode);

        } else if (DesignerConstant.fix_element_Axis.equalsIgnoreCase(name)) {
            List<Element> axisEleList = childElement.elements(DesignerConstant.One);
            String fieldName = loadAxises(widget, axisEleList);
            if (Util.isEmptyStr(fieldName)) {
                fieldName = "axis";
            }
            FieldNode rootNode = new FieldNode(Axis.class, fieldName);
            putSourcrFieldClazName(rootNode);

        }else if (DesignerConstant.DATA_TYPE.equalsIgnoreCase(name)) {
            EDataType eDataType = EDataType.valueOf(elementStringValue);
            String fieldName = widget.setDataType(eDataType);

            FieldNode rootNode = new FieldNode(String.class,fieldName);
            putSourcrFieldClazName(rootNode);

        }else if (DesignerConstant.DATA_NAME.equalsIgnoreCase(name)) {
            String fieldName = widget.setDataName(elementStringValue.trim());

            FieldNode rootNode = new FieldNode(String.class,fieldName);
            putSourcrFieldClazName(rootNode);
        }else if (DesignerConstant.FILTERS.equalsIgnoreCase(name)) {

            List<Element> segmentList = childElement.elements(DesignerConstant.keyword_segment);
            for (Element segment : segmentList) {
                Attribute nameAttr = segment.attribute(DesignerConstant.keyword_name);
                String fieldName;
                if (nameAttr == null) {
                    throw new DesignerFileException("节点: " + name);
                } else {
                    fieldName = nameAttr.getValue();
                }
                Attribute linkAttr = segment.attribute(DesignerConstant.keyword_link);
                String link;
                if (linkAttr == null) {
                    link = DesignerConstant.keyword_type_equal;
                } else {
                    link = linkAttr.getValue();
                }
                Attribute typeAttr = segment.attribute(DesignerConstant.keyword_type);
                ESqlValueType type;
                if (typeAttr == null) {
                    type = ESqlValueType.value;
                } else {
                    type = ESqlValueType.valueOf(typeAttr.getValue());
                }

                SegmentPart segmentPart = new SegmentPart();
                segmentPart.setLink(link).setName(fieldName).setValueType(type).setValue(elementStringValue);
                String segmentName = widget.putSegment(segmentPart);

                FieldNode rootNode = new FieldNode(List.class,segmentName);
                putSourcrFieldClazName(rootNode);
            }

        }else if (DesignerConstant.CHART_OPTION.equalsIgnoreCase(name)) {
            ChartOption chartOption;
            Element path = childElement.element(DesignerConstant.PATH);
            if (path != null) {
                String filePath = path.getStringValue();
                if (Util.isEmptyStr(filePath)) {
                    chartOption = new ChartOption();
                } else {
                    GsonOption realOption = DesignerUtil.combineOption(filePath);
                    chartOption = new ChartOption(realOption);
                }
                chartOption.setLink(true);
                chartOption.setPath(filePath);

            } else {
                chartOption = new ChartOption();
                chartOption.setLink(false);

                Iterator<Element> iterator = childElement.elementIterator();
                while (iterator.hasNext()) {
                    Element child = iterator.next();
                    loadOneTierChild(chartOption, null, child, null);
                }
            }
            chartOption.setTopicId(widget.getId());
            Element element = childElement.element(DesignerConstant.THEME);
            if (element != null) {
                String themeName = element.getStringValue();
                Theme realTheme = DesignerComponentFactory.getInstance().getThemeByName(themeName);
                chartOption.setTheme(realTheme);
            }
            String fieldName = widget.setChartOption(chartOption);

            FieldNode rootNode = new FieldNode(ChartOption.class, fieldName);
            putSourcrFieldClazName(rootNode);

            chartOption.getRealChartOption().setPreFieldNode(rootNode);
        }else if (DesignerConstant.GRID_OPTION.equalsIgnoreCase(name)) {
            GridOption gridOption = new GridOption();
            //showpage
            Element shhowPageEle = childElement.element(DesignerConstant.Grid_element_showPage);
            if (shhowPageEle != null) {
                String showPageValue = shhowPageEle.getStringValue();
                if (!Util.isEmptyStr(showPageValue)) {
                    gridOption.setShowPage(Util.stringToBoolean(showPageValue));
                }
            }
            //showpage
            Element hasTitleEle = childElement.element(DesignerConstant.Grid_element_hasTitle);
            if (hasTitleEle != null) {
                String hasTitleValue = hasTitleEle.getStringValue();
                if (!Util.isEmptyStr(hasTitleValue)) {
                    gridOption.setHasTitle(Util.stringToBoolean(hasTitleValue));
                }
            }

            //page
            Element pageSizeEle = childElement.element(DesignerConstant.Grid_element_pageSize);
            if (pageSizeEle != null) {
                String pageSizeValue = pageSizeEle.getStringValue();
                if (Util.isEmptyStr(pageSizeValue)) {
                    throw new DesignerOptionsFileException("gridoption-pagesize为空");
                }
                Page page = new Page();
                page.setPageSize(Util.stringToInt(pageSizeValue, DesignerConstant.PAGE_DEFAULT_SIZE));
                gridOption.setPage(page);
            }


            //AllSelect
            Element AllSelectEle = childElement.element(DesignerConstant.Grid_element_enableAllSelect);
            if (AllSelectEle != null) {
                String AllSelectValue = AllSelectEle.getStringValue();
                if (!Util.isEmptyStr(AllSelectValue)) {
                    gridOption.setEnableAllSelect(Util.stringToBoolean(AllSelectValue));
                }
            }
            //multiSelect
            Element multiSelectEle = childElement.element(DesignerConstant.Grid_element_multiSelect);
            if (multiSelectEle != null) {
                String multiSelectValue = multiSelectEle.getStringValue();
                if (!Util.isEmptyStr(multiSelectValue)) {
                    gridOption.setMultiSelect(Util.stringToBoolean(multiSelectValue));
                }
            }

            //field
            List<Element> fieldEleList = childElement.elements(DesignerConstant.Grid_element_field);
            if (!isDefault &&(fieldEleList == null || fieldEleList.size() == 0)) {
               gridOption = DesignerUtil.fixGridField(widget, gridOption);
            }

            for (Element fieldEle : fieldEleList) {
                String fieldName = fieldEle.getStringValue();
                if (Util.isEmptyStr(fieldName)) {
                    throw new DesignerOptionsFileException("gridoption field未配置值");
                }
                GridField gridField = new GridField(fieldName);
                Attribute captionAttr = fieldEle.attribute(DesignerConstant.Grid_field_caption);
                if (captionAttr != null) {
                    String captionValue = captionAttr.getValue();
                    gridField.setCaption(captionValue);
                }
                Attribute widthAttr = fieldEle.attribute(DesignerConstant.Grid_field_width);
                if (widthAttr != null) {
                    String widthValue = widthAttr.getValue();
                    gridField.setWidth(widthValue);
                }
                Attribute alignAttr = fieldEle.attribute(DesignerConstant.Grid_field_align);
                if (alignAttr != null) {
                    String alignValue = alignAttr.getValue();
                    gridField.setAlign(Align.valueOf(alignValue));
                }
                Attribute groupByAttr = fieldEle.attribute(DesignerConstant.Grid_field_groupby);
                if (groupByAttr != null) {
                    String groupByValue = groupByAttr.getValue();
                    Boolean mayGroupBy = Util.stringToBoolean(groupByValue);
                    gridField.setGroupBy(mayGroupBy);
                    if (mayGroupBy) {
                        gridOption.putGroupByField(fieldName);
                    }
                }
                gridOption.putOneField(gridField);
            }
            widget.setGridOption(gridOption);

            FieldNode rootNode = new FieldNode(GridOption.class,"gridOption");
            putSourcrFieldClazName(rootNode);
        }
        else if (DesignerConstant.NOW_CHART_TYPE.equalsIgnoreCase(name)) {
            EChartType type = EChartType.valueOf(elementStringValue);
            String setDefaultType = widget.setDefaultType(type);

            FieldNode rootNode = new FieldNode(EChartType.class,setDefaultType);
            putSourcrFieldClazName(rootNode);
        }
    }


    private String loadAxises(Widget widget, List<Element> axisEleList) {
        String resultName = null;
        for (Element axisEle : axisEleList) {
            Attribute typettr = axisEle.attribute(DesignerConstant.keyword_type);
            if (typettr == null) {
                throw new DesignerOptionsFileException("widget axis type is null");
            }
            EDesignerDataType eDesignerDataType = EDesignerDataType.valueOf(typettr.getValue());


            Attribute gridAttr = axisEle.attribute(DesignerConstant.Grid);
            if (gridAttr == null) {
                throw new DesignerOptionsFileException("widget gridAttr type is null");
            }

            EDimensionAxis dimensonAxis = EDimensionAxis.valueOf(gridAttr.getValue());

            ChartAxis chartAxis = new ChartAxis(eDesignerDataType, dimensonAxis);

            Attribute positioAttr = axisEle.attribute(DesignerConstant.keyelement_position);

            if (positioAttr == null) {
                throw new DesignerOptionsFileException("widget axis type is null");
            }

            EAxisPositon eAxisPositon = EAxisPositon.valueOf(positioAttr.getValue());
            chartAxis.setPositon(eAxisPositon);
            Attribute nameAttr = axisEle.attribute(DesignerConstant.keyword_name);

            if (nameAttr != null) {
                chartAxis.setName(nameAttr.getValue());
            }

            Attribute inverseAttr = axisEle.attribute(DesignerConstant.keyelement_inverse);

            if (inverseAttr != null) {
                chartAxis.setInverse(Util.stringToBoolean(inverseAttr.getValue(), false));
            }

            List<Element> fieldEleList = axisEle.elements(DesignerConstant.keyelement_field);

            for (Element fieldEle : fieldEleList) {
                String fieldName = fieldEle.getStringValue();
                AxisField axisField = new AxisField(fieldName);
                Attribute fieldTypeAttr = fieldEle.attribute(DesignerConstant.keyword_type);
                if (fieldTypeAttr != null) {
                    EChartType type = EChartType.valueOf(fieldTypeAttr.getValue());
                    axisField.setType(type);
                } else {
                    axisField.setType(EChartType.bar);
                }
                Attribute captionAttr = fieldEle.attribute(DesignerConstant.keyword_capiton);
                if (captionAttr != null) {
                    axisField.setCaption(captionAttr.getValue());
                }
                chartAxis.putField(axisField);
            }
            resultName = widget.putAxises(chartAxis);
        }
        return resultName;

    }




}
