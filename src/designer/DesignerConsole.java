package designer;

import designer.engine.DesignContext;
import designer.options.DesignerComponentFactory;
import designer.topic.Topic;
import foundation.callable.Callable;
import foundation.data.Entity;
import foundation.persist.DataHandler;

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
        } else if (DesignerConstant.CALLABLE_METHORD_GETTOPIC.equalsIgnoreCase(operator)) {
            getTopic();
        } else if ("default".equalsIgnoreCase(operator)) {
            System.out.println(DesignerComponentFactory.getInstance().getDefautOption().toString());
        }

    }

    private void getTopic() throws Exception {
        String topicId = request.getParameter("id");
        Entity topicLine = DataHandler.getLine(DesignerConstant.TABLE_designer_paneltopic, DesignerConstant.TOPID, topicId);

        String topicName = topicLine.getString(DesignerConstant.FIELD_TOPICNAME);
        String path = topicLine.getString(DesignerConstant.FIELD_TOPICpath);

        File topicFile = DesignerUtil.checkFileLegality(path);
        Topic topic = new Topic(topicId, topicName);
        DesignerUtil.loadTopicFile(topicFile ,topic);

    }


    private void synchronize() {
        try {
            DesignContext designContext = new DesignContext(request);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
