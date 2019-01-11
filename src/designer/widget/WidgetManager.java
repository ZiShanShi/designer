package designer.widget;

import designer.DesignerConstant;
import designer.DesignerUtil;
import designer.cache.CacheManager;
import designer.cache.EOptionSourceType;
import designer.cache.FieldNode;
import designer.xml.EDesignerXmlType;
import designer.xml.LayerElemetRoot;
import designer.xml.XmlReader;
import designer.xml.XmlWriter;
import foundation.config.Configer;
import foundation.data.Entity;
import foundation.persist.DataHandler;
import org.dom4j.Document;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author kimi
 * @description 主题管理类
 * @date 2018-12-17 20:27
 */


public class WidgetManager {
    private static WidgetManager manager;
    private List<String> widegtList;

    public static WidgetManager getInstance() {
        if (manager == null) {
            synchronized (WidgetManager.class) {
                if (manager == null) {
                    manager = new WidgetManager();
                }
            }
        }
        return manager;
    }

    private WidgetManager() {
        widegtList = new ArrayList<>();
    }

    public void addWidget(Widget widget) {
        widegtList.add(widget.getId());
        CacheManager.getInstance().put(widget.getId(), widget);
    }


    public Widget getWidget(String widgetId) {
        if (widegtList == null || !widegtList.contains(widgetId)) {
            return null;
        }
        return CacheManager.getInstance().get(widgetId);
    }

    public void remove(String key, Widget remove) {
        if (widegtList.contains(key)) {
            widegtList.remove(key);
            CacheManager.getInstance().remove(key);
            save2File(remove);
        }
    }


    public Widget loadWidget(String widgetId) {
        Entity topicLine;
        Widget widget = null;
        try {
            topicLine = DataHandler.getLine(DesignerConstant.TABLE_designer_panelwidget, DesignerConstant.WIDGETID, widgetId);
            String widgetName = topicLine.getString(DesignerConstant.FIELD_WIDGETNAME);
            String path = topicLine.getString(DesignerConstant.FIELD_WIDGETPATH);
            path = path.replace(DesignerConstant.ROOT, Configer.getPath_Application());
            File topicFile = DesignerUtil.checkFileLegality(path);
            widget = new Widget(widgetId, widgetName);
            XmlReader topicReader = new XmlReader(EDesignerXmlType.realWidget);
            widget.setPath(path);
            topicReader.read(topicFile, widget);
            WidgetManager.getInstance().addWidget(widget);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return widget;
    }

    private void save2File(Widget removedWidget) {
        // widget 一层  option 多层
        removedWidget = DesignerUtil.addAllCacheMap(removedWidget, removedWidget.getChartOption().getRealChartOption());
        Map<EOptionSourceType, Set<FieldNode>> fieldNodeSourceMap = removedWidget.getFieldNodeSourceMap();
        Set<FieldNode> fieldNodes = fieldNodeSourceMap.get(EOptionSourceType.Changed);
        if (fieldNodes == null || fieldNodes.size() == 0) {
            return;
        }
        String id = removedWidget.getId();
        File widgetFile = DesignerUtil.getWidgetFile(id);
        XmlWriter xmlWriter = new XmlWriter(widgetFile);
        xmlWriter.setWidget(removedWidget);

        //load 所有的root节点

        fieldNodes.stream().map(fieldNode -> loadAllRoot(xmlWriter, fieldNode)).collect(Collectors.toList());

        fieldNodes.stream()
                .filter(fieldNode -> !fieldNode.isParent())
                .map(fieldNode -> fixRealWidegtFile(xmlWriter, fieldNode))
                .collect(Collectors.toList());

        OutputFormat outformat = new OutputFormat();
        // 指定XML编码
        outformat.setEncoding("UTF-8");
        outformat.setNewlines(true);
        outformat.setIndent(true);
        outformat.setTrimText(true);
        xmlWriter.getRootEleMap().values().stream().map(layerElemet -> saveXmlFile(layerElemet, outformat)).collect(Collectors.toList());
    }

    private <R> R saveXmlFile(LayerElemetRoot layerElemet, OutputFormat outformat) {
        String path = layerElemet.getPath();
        Document doc = layerElemet.getDocument();
        File file = DesignerUtil.checkFileLegality(path);
        XMLWriter xmlWriter = null;
        try {
            xmlWriter = new XMLWriter(new FileOutputStream(file), outformat);
            xmlWriter.write(doc);
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            try {
                xmlWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    private <R> R loadAllRoot(XmlWriter xmlWriter, FieldNode fieldNode) {
        int layer = fieldNode.getLayer();
        if (layer == 1) {
            return null;
        }
        String layerFiledName = fieldNode.getLayerFiledName(layer - 1);
        String filedName = fieldNode.getLayerFiledName(layer);
        boolean exists = xmlWriter.checkExists(layerFiledName);

        if (exists) {
            return null;
        }

        xmlWriter.putChildXmlRoot(layer, filedName, fieldNode);
        return null;
    }

    private <R> R fixRealWidegtFile(XmlWriter xmlWriter, FieldNode fieldNode) {
        try {
            xmlWriter.load2Xml(fieldNode);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public void refresh(String widgetId) {
        if (!widegtList.contains(widgetId)) {
            return;
        }
        remove(widgetId, getWidget(widgetId));

        Widget widget = new Widget(widgetId);
        File widgetFile = DesignerUtil.getWidgetFile(widgetId);

        XmlReader reader = new XmlReader(EDesignerXmlType.realWidget);
        reader.read(widgetFile, widget);

        addWidget(widget);
    }


}
