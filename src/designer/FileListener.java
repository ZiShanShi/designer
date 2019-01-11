package designer;

import designer.widget.WidgetManager;
import foundation.config.Configer;
import foundation.data.Entity;
import foundation.data.EntitySet;
import foundation.persist.DataHandler;
import foundation.util.Util;
import org.apache.commons.io.monitor.FileAlterationListenerAdaptor;
import org.apache.commons.io.monitor.FileAlterationObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.text.MessageFormat;

/**
 * @author kimi
 * @description 文件修改监听器
 * @date 2019-01-08 14:22
 */


public class FileListener extends FileAlterationListenerAdaptor {

    private final Logger logger;

    FileListener() {
        logger = LoggerFactory.getLogger(this.getClass());
    }
    @Override
    public void onFileChange(File file) {
        logger.info(MessageFormat.format("文件发生修改，文件名：{0}--路径：{1}", file.getName(), file.getAbsolutePath()));
        String name = file.getParentFile().getName();
        String themePathName = Configer.getParam(DesignerConstant.DEFAULT_Theme_PART_PATH);
        String widgetPathName = Configer.getParam(DesignerConstant.DEFAULT_WIDGET_PART_PATH);
        if (name.equalsIgnoreCase(themePathName)) {
            refreshRuntimeTheme(file);
        }
        if (name.equalsIgnoreCase(widgetPathName)) {
            refreshRuntimeWideget(file);
        }
    }

    private void refreshRuntimeTheme(File file) {
        DesignerComponentFactory.getInstance().refreshTheme(file);

    }

    private void refreshRuntimeWideget(File file) {
        String filePath = file.getAbsolutePath();
        getWidgetId(filePath);
    }

    private void getWidgetId(String filePath) {
        String filter = Util.stringJoin(DesignerConstant.FIELD_WIDGETPATH, Util.Equal, Util.quotedStr(filePath));
        try {
            EntitySet dataSet = DataHandler.getDataSet(DesignerConstant.TABLE_designer_panelwidget, filter);
            if (dataSet == null || dataSet.size() == 0) {
                String path_application = Configer.getPath_Application();
                filePath = Util.pathNormalize(filePath);
                path_application = Util.pathNormalize(path_application);
                filePath = filePath.replace(path_application, DesignerConstant.ROOT);
                filePath = Util.path2Window(filePath);
                filter = Util.stringJoin(DesignerConstant.FIELD_WIDGETPATH, Util.Equal, Util.quotedStr(filePath));

                dataSet = DataHandler.getDataSet(DesignerConstant.TABLE_designer_panelwidget, filter);
                if (dataSet == null || dataSet.size() == 0) {
                    return;
                }
                for (Entity entity : dataSet) {
                    String widgetId = entity.getString(DesignerConstant.WIDGETID);
                    WidgetManager.getInstance().refresh(widgetId);
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void onStart(FileAlterationObserver observer) {
        super.onStart(observer);
    }

    @Override
    public void onStop(FileAlterationObserver observer) {
        super.onStop(observer);
    }

}
