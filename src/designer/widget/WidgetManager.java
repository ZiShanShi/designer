package designer.widget;

import designer.DesignerUtil;
import designer.cache.CacheManager;
import designer.cache.EOptionSourceType;
import designer.cache.FieldNode;
import designer.xml.XmlWriter;

import java.io.File;
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

    public void remove(String key, Widget removedWidget) {
        widegtList.remove(key);

        save2File(removedWidget);
    }

    private void save2File(Widget removedWidget) {
        // widget 一层  option 多层


        Map<EOptionSourceType, Set<FieldNode>> fieldNodeSourceMap = removedWidget.getFieldNodeSourceMap();
        Set<FieldNode> fieldNodes = fieldNodeSourceMap.get(EOptionSourceType.Real);
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
    }

    private <R> R loadAllRoot(XmlWriter xmlWriter, FieldNode fieldNode) {
        int layer = fieldNode.getLayer();
        if (layer == 1) {
            return null;
        }
        String layerFiledName = fieldNode.getLayerFiledName(layer - 1);
        boolean exists = xmlWriter.checkExists(layerFiledName);

        if (exists) {
            return null;
        }

        xmlWriter.putChildXmlRoot(layer, layerFiledName, fieldNode);
        return null;
    }

    private <R> R fixRealWidegtFile(XmlWriter xmlWriter, FieldNode fieldNode) {
        xmlWriter.load2Xml(fieldNode);
        return null;
    }


}
