package designer;

import designer.engine.DesignContext;
import designer.widget.Widget;
import designer.widget.WidgetManager;
import designer.xml.EDesignerXmlType;
import designer.xml.XmlReader;
import foundation.callable.Callable;
import foundation.config.Configer;
import foundation.data.Entity;
import foundation.persist.DataHandler;
import net.sf.json.JSONObject;

import java.io.File;

/**
 * @author kimi
 * @description 设计器入口
 * @date 2018-12-17 15:20
 */


public class DesignerConsole extends Callable {

    @Override
    protected void doReceive(String[] paths) throws Exception {
        String operator = paths[1];
        operator = operator.toLowerCase();

        if (DesignerConstant.CALLABLE_METHORD_SYNCHRONIZE.equalsIgnoreCase(operator)) {
            synchronize();
        } else if (DesignerConstant.CALLABLE_METHORD_GETWIDGET.equalsIgnoreCase(operator)) {
            getWidegt();
        } else if ("default".equalsIgnoreCase(operator)) {
            System.out.println(DesignerComponentFactory.getInstance().getDefautOption().toString());
        }

    }

    private void getWidegt() throws Exception {
        String widgetId = request.getParameter("id");
        Widget widget = WidgetManager.getInstance().getWidget(widgetId);
        if (widget != null) {
            JSONObject object = widget.parseJson();
            resultPool.addValue("widget", object);
            WidgetManager.getInstance().remove(widgetId, widget);
        } else {
            Entity topicLine = DataHandler.getLine(DesignerConstant.TABLE_designer_panelwidget, DesignerConstant.WIDGETID, widgetId);
            String topicName = topicLine.getString(DesignerConstant.FIELD_WIDGETNAME);
            String path = topicLine.getString(DesignerConstant.FIELD_WIDGETPATH);
            path = path.replace(DesignerConstant.ROOT, Configer.getPath_Application());
            File topicFile = DesignerUtil.checkFileLegality(path);
            widget = new Widget(widgetId, topicName);
            XmlReader topicReader = new XmlReader(EDesignerXmlType.realTopic);
            widget.setPath(path);
            topicReader.read(topicFile , widget);
            WidgetManager.getInstance().addWidget(widget);
//            String gsonOptionsStr = GsonUtil.format(widget.getChartOption().getRealChartOption());
//            System.out.println(gsonOptionsStr);
            JSONObject object = widget.parseJson();
            resultPool.addValue("widget", object);
        }

    }


    private void synchronize() {
        try {
            DesignContext designContext = new DesignContext(request);
            designContext.exec();
            String widgetId = designContext.getWidgetId();
            Widget widget = WidgetManager.getInstance().getWidget(widgetId);
            WidgetManager.getInstance().remove(widgetId, widget);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
