package designer.xml;

import designer.DesignerConstant;
import designer.DesignerUtil;
import designer.cache.FieldNode;
import designer.exception.DesignerOptionsFileException;
import designer.widget.Widget;
import foundation.config.Configer;
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
    private File xmlFile;
    private Element root;
    private Map<String, LayerElemetRoot> rootEleMap;

    public XmlWriter() {
        rootEleMap = new HashMap<>();
    }

    public XmlWriter(File xmlFile) {
        this();
        this.xmlFile = xmlFile;
        SAXReader reader = new SAXReader();
        try {
            Document document = reader.read(xmlFile);
            root = document.getRootElement();
        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }

    public XmlWriter load2Xml(FieldNode node) {
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
        String thisName = node.getLayerFiledName(layer);
        Element parentRoot = parentElemetRoot.getRoot();

        Element thisEle = parentRoot.element(thisName);
        if (thisEle == null) {
            //create
            thisEle = createElement(thisEle, node, layer);
        }
        String value = getWidgetData(node);
        thisEle.setText(value);
        return this;
    }

    private Element createElement(Element thisEle, FieldNode node, int layer) {
        int maxLayer = node.getLayer();
        Element element = thisEle;
        while (layer < maxLayer) {
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

    private String getWidgetData(FieldNode node) {
        List<String> hierarchyList = node.getHierarchyList();

        if (hierarchyList.size() > 1) {
            //option 级别

        } else {
            String fieldName = hierarchyList.get(0);

            try {
                Field declaredField = Widget.class.getDeclaredField(fieldName);
                DataType dataType = DataType.valueOfString(declaredField.getType().getSimpleName());
                String getfieldMethordName;
                if (dataType.equals(DataType.Boolean)) {
                    getfieldMethordName = Util.stringJoin(DesignerConstant.IS, fieldName.substring(0, 1).toUpperCase(), fieldName.substring(1));

                } else {
                    getfieldMethordName = Util.stringJoin(DesignerConstant.GET, fieldName.substring(0, 1).toUpperCase(), fieldName.substring(1));
                }

                Method getMethod = Widget.class.getDeclaredMethod(getfieldMethordName);
                Object value = getMethod.invoke(widget, new Object[0]);

                return String.valueOf(value);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private XmlWriter load2WidgetLayer(FieldNode node) {
        return this;
    }

    public XmlWriter putChildXmlRoot(int layer, String name, Element root) {
        LayerElemetRoot layerElemetRoot = new LayerElemetRoot(layer, root);
        if (rootEleMap == null) {
            rootEleMap = new HashMap<>();
        }
        rootEleMap.put(name, layerElemetRoot);
        return this;
    }

    public XmlWriter put2RootMap(int layer, String name, File file) {
        SAXReader reader = new SAXReader();
        try {
            Document document = reader.read(file);
            Element rootElement = document.getRootElement();

            return putChildXmlRoot(layer, name, rootElement);
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        return this;
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
