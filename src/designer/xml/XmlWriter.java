package designer.xml;

import designer.DesignerComponentFactory;
import designer.DesignerConstant;
import designer.DesignerUtil;
import designer.cache.FieldNode;
import designer.exception.DesignerOptionsFileException;
import designer.options.AxisField;
import designer.options.ChartOption;
import designer.options.GridField;
import designer.options.GridOption;
import designer.options.echart.json.GsonOption;
import designer.widget.*;
import designer.widget.theme.Theme;
import foundation.config.Configer;
import foundation.util.Util;
import net.sf.json.JSONSerializer;
import net.sf.json.xml.XMLSerializer;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author kimi
 * @description 写入xml
 * @date 2018-12-29 14:18
 */


public class XmlWriter {
    private Widget widget;
    private Element root;
    private Map<String, LayerElemetRoot> rootEleMap;

    public XmlWriter() {
        rootEleMap = new HashMap<>();
    }

    public XmlWriter(File xmlFile) {
        this();

        SAXReader reader = new SAXReader();
        try {
            Document document = reader.read(xmlFile);
            root = document.getRootElement();
            LayerElemetRoot layerElemetRoot = new LayerElemetRoot(1, document, root, xmlFile.getAbsolutePath());
            rootEleMap.put("root", layerElemetRoot);
        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }

    public XmlWriter load2Xml(FieldNode node) throws Exception {
        if (root == null || node == null) {
            throw new DesignerOptionsFileException("XmlWriter 初始化失败");
        }
        if (node.isParent()) {
            return this;
        }

        int layer = node.getLayer();

        if (layer == 1) {
            // widget 层单独处理
            return load2WidgetLayer(node);
        }
        LayerElemetRoot parentElemetRoot = getRecentParentRoot(node, layer);
        //此时的layer 不一定是最后一个节点
        int index = 2;
        Element rootEle = parentElemetRoot.getRoot();
        Element thisEle = findNearEle(rootEle, node, index);

        if (thisEle == null) {
            //create
            thisEle = createElement(rootEle, node, 1);
        }

        String value = getWidgetData(node);
        thisEle.setText(value);
        return this;
    }

    private Element findNearEle(Element rootEle, FieldNode node, int index) {
        int layer = node.getLayer();
        Element preElement = null;
        while (index <= layer) {
            String layerFiledName = node.getLayerFiledName(index);
            Element element = rootEle.element(layerFiledName);
            if (element == null) {
                return preElement;
            } else {
                index++;
                Element ele =  findNearEle(element, node, index);
                if (ele == null) {
                    preElement = element;
                } else {
                    preElement = ele;
                }
            }
        }
        return preElement;
    }

    private Element createElement(Element thisEle, FieldNode node, int layer) {
        int maxLayer = node.getLayer();
        Element element = thisEle;
        while (layer <= maxLayer) {
            String layerFiledName = node.getLayerFiledName(layer);
            element = element.addElement(layerFiledName);

            layer++;
        }
        return element;
    }

    private LayerElemetRoot getRecentParentRoot(FieldNode node, int layer) {
        while (layer > 0) {
            String layerFiledName = node.getLayerFiledName(layer);
            LayerElemetRoot parentElemetRoot =rootEleMap.get(layerFiledName);
            if (parentElemetRoot != null) {
                return parentElemetRoot;
            }
            layer--;
            return getRecentParentRoot(node, layer);
        }
        return null;
    }

    private String getWidgetData(FieldNode node) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        List<String> hierarchyList = node.getHierarchyList();
        ChartOption chartOption = widget.getChartOption();
        String result = null;
        if (hierarchyList.size() == 1) {
            //option 级别
            XMLSerializer xmlSerializer = new XMLSerializer();
            result = xmlSerializer.write(JSONSerializer.toJSON(chartOption));
            //writerChartOptionLayerData(hierarchyList);
        } else if (hierarchyList.size() > 1) {
            GsonOption realChartOption = chartOption.getRealChartOption();
            int startIndex = 1;
            Object realValue = getRealValue(startIndex, hierarchyList, realChartOption);

            result = String.valueOf(realValue);
            //writerWidgetLayerData(hierarchyList.get(0));
        }
        return result;
    }

    private Object getRealValue(int startIndex, List<String> hierarchyList, Object realChartOption) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        String preFieldName = hierarchyList.get(startIndex);
        String fieldName = Util.stringJoin(preFieldName.substring(0, 1).toLowerCase(), preFieldName.substring(1));
        Method getMethod = realChartOption.getClass().getMethod(fieldName);
        Object invoke = getMethod.invoke(realChartOption, new Object[0]);
        if (startIndex < (hierarchyList.size() - 1)) {
            startIndex++;
            invoke = getRealValue(startIndex, hierarchyList, invoke);

        }
        return invoke;
    }


    private XmlWriter load2WidgetLayer(FieldNode node) {
        //theme 在本层处理
        String fixedName = widget.fromWidget2XmlBean(node.getFieldName());
        EWidgetField eWidgetField = EWidgetField.valueOf(fixedName);
        Widget baseWidget = DesignerComponentFactory.getInstance().getBaseWidget();
        Element oneLayerElement = null;
        if (DesignerConstant.THEME.equalsIgnoreCase(fixedName)) {
            oneLayerElement = root.element(DesignerConstant.CHART_OPTION).element(DesignerConstant.THEME);
        } else {
            oneLayerElement = root.element(fixedName);
        }

        if (oneLayerElement == null) {
            oneLayerElement = root.addElement(fixedName);
        }
        boolean isEqual;
        switch (eWidgetField) {
            case theme:
                Theme theme = widget.getChartOption().getTheme();
                Theme baseTheme = baseWidget.getChartOption().getTheme();
                isEqual = false;
                if (theme == null) {
                    break;
                } else if (theme != null && baseTheme != null){
                    if (theme.getName().equalsIgnoreCase(baseTheme.getName())) {
                        isEqual = true;
                    } else {
                        isEqual = false;
                    }
                }
                if (isEqual) {
                    break;
                }
                Element themeElement = oneLayerElement;
                if (themeElement == null) {
                    themeElement = oneLayerElement.addElement(DesignerConstant.THEME);
                }
                themeElement.setText(theme.getName());
                break;
            case hasgrid:
                boolean hasGrid = widget.isHasGrid();
                boolean baseHasGrid = baseWidget.isHasGrid();
                if (hasGrid == baseHasGrid) {
                    return this;
                }
                oneLayerElement.setText(String.valueOf(hasGrid));
                break;
            case datatype:
                EDataType dataType = widget.getDataType();
                EDataType baseDataType = baseWidget.getDataType();
                if (dataType.equals(baseDataType)) {
                    return this;
                }
                oneLayerElement.setText(dataType.name());
                break;
            case dataname:
                String dataName = widget.getDataName();
                oneLayerElement.setText(dataName);
                break;
            case chartType:
                List<ChartType> chartTypeList = widget.getChartTypeList();
                List<ChartType> baseChartTypeList = baseWidget.getChartTypeList();
                isEqual = false;
                if (chartTypeList.size() == baseChartTypeList.size()) {
                    for (int i = 0; i < baseChartTypeList.size(); i++) {
                        ChartType baseChartType = baseChartTypeList.get(i);
                        ChartType chartType = chartTypeList.get(i);
                        if (!baseChartType.equals(chartType)) {
                            isEqual = false;
                        } else {
                            isEqual = true;
                        }
                    }
                }

                if (isEqual) {
                    return this;
                }

                List<Element> chartTypeElements = oneLayerElement.elements(DesignerConstant.CHART_TYPE);
                //1
                for (Element chartTypeElement : chartTypeElements) {
                    oneLayerElement.remove(chartTypeElement);
                }
                for (ChartType chartType : chartTypeList) {
                    Element chartTypeElement = oneLayerElement.addElement(chartType.getType().name());
                    setAttrIFNotNull(chartTypeElement, DesignerConstant.keyelement_minDimension, chartType.getMinDimensionNum());
                    setAttrIFNotNull(chartTypeElement, DesignerConstant.keyelement_minDimension, chartType.getMinDimensionNum().toString());
                    setAttrIFNotNull(chartTypeElement, DesignerConstant.keyelement_minMensurment, chartType.getMinMensurmentNum().toString());
                    setAttrIFNotNull(chartTypeElement, DesignerConstant.keyelement_maxDimension, chartType.getMaxDimensionNum().toString());
                    setAttrIFNotNull(chartTypeElement, DesignerConstant.keyelement_maxMensurment, chartType.getMaxMensurmentNum().toString());

                    chartTypeElement.setText(String.valueOf(chartType.isOpen()));
                }

                break;
            case filters:
                List<SegmentPart> segmentList = widget.getSegmentList();
                List<SegmentPart> baseSegmentList = baseWidget.getSegmentList();

                isEqual = false;
                if (segmentList.size() == baseSegmentList.size()) {
                    for (int i = 0; i < baseSegmentList.size(); i++) {
                        SegmentPart baseSegmentPart = baseSegmentList.get(i);
                        SegmentPart segmentPart = segmentList.get(i);
                        if (baseSegmentPart.equals(segmentPart)) {
                            isEqual = true;
                        } else {
                            isEqual = false;
                        }

                    }

                }

                if (isEqual) {
                    return this;
                }

                List<Element> segmentElements = oneLayerElement.elements(DesignerConstant.keyword_segment);
                // 1
                for (Element segmentElement : segmentElements) {
                    oneLayerElement.remove(segmentElement);
                }

                for (SegmentPart segmentPart : segmentList) {
                    Element segmentElement = oneLayerElement.addElement(DesignerConstant.keyword_segment);
                    setAttrIFNotNull(segmentElement, DesignerConstant.keyword_name, segmentPart.getName());
                    setAttrIFNotNull(segmentElement, DesignerConstant.keyword_type, segmentPart.getValueType().name());
                    setAttrIFNotNull(segmentElement, DesignerConstant.keyword_link, segmentPart.getLink().toString());

                    segmentElement.setText(segmentPart.getValue());
                }
                break;

            case gridOption:
                GridOption gridOption = widget.getGridOption();
                GridOption baseWidgetGridOption = baseWidget.getGridOption();
                //hastitle
                if (!gridOption.getHasTitle().equals(baseWidgetGridOption.getHasTitle())) {
                    Element hasTitleEle = oneLayerElement.element(DesignerConstant.Grid_element_hasTitle);
                    if (hasTitleEle == null) {
                        hasTitleEle = oneLayerElement.addElement(DesignerConstant.Grid_element_hasTitle);
                    }
                    hasTitleEle.setText(String.valueOf(gridOption.getHasTitle()));
                }
                //showpage
                if (!gridOption.getShowPage().equals(baseWidgetGridOption.getShowPage())) {
                    Element showPageEle = oneLayerElement.element(DesignerConstant.Grid_element_showPage);
                    if (showPageEle == null) {
                        showPageEle = oneLayerElement.addElement(DesignerConstant.Grid_element_showPage);
                    }
                    showPageEle.setText(String.valueOf(gridOption.getShowPage()));
                }
                //pagesize
                if (!(gridOption.getPage().getPageSize() == baseWidgetGridOption.getPage().getPageSize())) {
                    Element pageSizeEle = oneLayerElement.element(DesignerConstant.Grid_element_pageSize);
                    if (pageSizeEle == null) {
                        pageSizeEle = oneLayerElement.addElement(DesignerConstant.Grid_element_pageSize);
                    }
                    pageSizeEle.setText(String.valueOf(gridOption.getPage().getPageSize()));
                }

                //field
                List<Element> fieldElements = oneLayerElement.elements(DesignerConstant.keyelement_field);
                for (Element fieldElement : fieldElements) {
                    oneLayerElement.remove(fieldElement);
                }

                List<GridField> gridFieldList = gridOption.getFieldList();
                for (GridField gridField : gridFieldList) {
                    Element newFieldEle = oneLayerElement.addElement(DesignerConstant.keyelement_field);
                    newFieldEle.setText(gridField.getField());
                    setAttrIFNotNull(newFieldEle, DesignerConstant.keyword_capiton, gridField.getCaption());
                    setAttrIFNotNull(newFieldEle, DesignerConstant.Grid_field_width, gridField.getWidth());
                    setAttrIFNotNull(newFieldEle, DesignerConstant.Grid_field_align, gridField.getAlign());
                    setAttrIFNotNull(newFieldEle, DesignerConstant.Grid_field_groupby, String.valueOf(gridField.getGroupBy()));
                }

                break;
            case axis:
                List<ChartAxis> axisList = widget.getAxisList();
                List<Element> elements = oneLayerElement.elements(DesignerConstant.One);
                //删除原先节点
                for (Element oneAxisEle : elements) {
                  oneLayerElement.remove(oneAxisEle);
                }
                for (ChartAxis chartAxis : axisList) {
                    Element axisOneElement = oneLayerElement.addElement(DesignerConstant.One);

                    setAttrIFNotNull(axisOneElement, DesignerConstant.keyword_type, chartAxis.getType().name());
                    setAttrIFNotNull(axisOneElement, DesignerConstant.keyword_name, chartAxis.getName());
                    setAttrIFNotNull(axisOneElement, DesignerConstant.Grid, chartAxis.getPositon().getAxisName().name());
                    setAttrIFNotNull(axisOneElement, DesignerConstant.keyelement_position, chartAxis.getPositon().name());

                    List<AxisField> fieldList = chartAxis.getFieldList();
                    if (fieldList.size() == 0) {
                        continue;
                    }
                    for (AxisField axisField : fieldList) {
                        Element axisOneFieldElement = axisOneElement.addElement(DesignerConstant.keyelement_field);
                        setAttrIFNotNull(axisOneFieldElement, DesignerConstant.keyword_type, axisField.getType().name());
                        if (!Util.isEmptyStr(axisField.getCaption())) {
                            setAttrIFNotNull(axisOneFieldElement, DesignerConstant.keyword_capiton, axisField.getCaption());
                        }
                    }

                }
                break;


            default:
                break;

        }

        return this;
    }

    private void setAttrIFNotNull(Element chartTypeElement, String keyelement, Object value) {
        if (chartTypeElement == null || Util.isEmptyStr(keyelement) || value == null) {
            return;
        }
        chartTypeElement.addAttribute(keyelement,String.valueOf(value));
    }

    private String getElementAttr(Element oneAxisEle, String keyword_type) {
        Attribute attribute = oneAxisEle.attribute(keyword_type);
        if (attribute != null) {
         return attribute.getValue();
        }
        return null;
    }


    public XmlWriter putChildXmlRoot(int layer, String name, Document document, String absolutePath) {
        Element rootElement = document.getRootElement();
        LayerElemetRoot layerElemetRoot = new LayerElemetRoot(layer, document, rootElement, absolutePath);
        if (rootEleMap == null) {
            rootEleMap = new HashMap<>();
        }
        rootEleMap.put(name, layerElemetRoot);
        return this;
    }

    public XmlWriter put2RootMap(int layer, String name, File file) {
        SAXReader reader = new SAXReader();
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(file);
            Document document = reader.read(fileInputStream);

            String absolutePath = file.getAbsolutePath();
            return putChildXmlRoot(layer, name, document, absolutePath);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if (fileInputStream != null) {
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return this;
    }

    public Map<String, LayerElemetRoot> getRootEleMap() {
        return rootEleMap;
    }

    public XmlWriter putChildXmlRoot(int layer, String name, FieldNode node) {
        if (root == null) {
            throw new DesignerOptionsFileException("root is null");
        }
        if (layer == 1) {
            Element element = root.element(name);
            if (element == null) {
                //以前没有这种配置 写那边需要注意
            } else {
                Element pathEle = element.element(DesignerConstant.PATH);
                if (pathEle != null) {
                    //说明是有link的
                    String elePath = pathEle.getStringValue();
                    File eleFile = DesignerUtil.checkFileLegality(elePath, Configer.getParam(DesignerConstant.DEFAULT_CHART_SUFFIX));
                    put2RootMap(layer, name, eleFile);
                }
            }
        } else {
            String preName = node.getLayerFiledName(layer - 1);
            LayerElemetRoot layerElemetRoot = rootEleMap.get(preName);
            if (layerElemetRoot == null) {
                //上一层找不到
                putChildXmlRoot(layer - 1, preName, node);
            }
            //现在应该上一层已经加载进去了
            layerElemetRoot = rootEleMap.get(preName);
            if (layerElemetRoot == null) {
               return this;
            }
            Element root = layerElemetRoot.getRoot();
            Element childNameEle = root.element(name);
            Attribute typeAttr = childNameEle.attribute(DesignerConstant.keyword_type);
            if (typeAttr != null) {
                if (typeAttr.getValue().equalsIgnoreCase(DesignerConstant.keyword_type_file)) {
                    String childPath = childNameEle.getStringValue();
                    File childFile = DesignerUtil.checkFileLegality(childPath);
                    put2RootMap(layer, name, childFile);
                }
            } else {
                Element pathEle = childNameEle.element(DesignerConstant.PATH);
                if (pathEle != null) {
                    String childPath = pathEle.getStringValue();
                    File childFile = DesignerUtil.checkFileLegality(childPath);
                    put2RootMap(layer, name, childFile);
                }
            }

        }

        return this;
    }

    public boolean checkExists(String name) {
        if (rootEleMap == null) {
            rootEleMap = new HashMap<>();
            return false;
        }
        LayerElemetRoot layerElemetRoot = rootEleMap.get(name);
        if (layerElemetRoot != null) {
            return true;
        } else {
            return false;
        }
    }

    public Widget getWidget() {
        return widget;
    }

    public XmlWriter setWidget(Widget widget) {
        this.widget = widget;
        return this;
    }

    public Element getRoot() {
        return root;
    }
}
