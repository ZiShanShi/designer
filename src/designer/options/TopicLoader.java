package designer.options;

import designer.IDesiginerConstant;
import foundation.config.Configer;
import foundation.config.Preloader;
import foundation.util.Util;

import java.io.File;

/**
 * @author kimi
 * @description Topic初始加载类
 * @date 2018-12-17 20:31
 */


public class TopicLoader extends Preloader {

    @Override
    public void load() throws Exception {
        String rootPath = Configer.getParam(IDesiginerConstant.CHART_ROOT_PATH);
        String defaultEngine = Configer.getParam(IDesiginerConstant.DEFAULT_CHART_ENGINE);
        if (rootPath == null) {
            throw new RuntimeException(IDesiginerConstant.CHART_ROOT_PATH + "： 未初始化图表数据路径");
        }

        String defaultRootPath = rootPath + IDesiginerConstant.DEFAULT;
        String chartRootPath = rootPath + IDesiginerConstant.CHART;

        File defaultFolder = new File(defaultRootPath);
        if (!defaultFolder.exists()) {
            throw new RuntimeException(defaultRootPath + "： 图表默认参数文件夹不存在");
        }

        File chartFolder = new File(chartRootPath);
        if (!chartFolder.exists()) {
            throw new RuntimeException(chartFolder + "： 图表文件夹不存在");
        }

        String defaultFilePath = defaultRootPath + defaultEngine + Util.Dot + IDesiginerConstant.DEFAULT_CHART_SUFFIX;
        File defaultFile = new File(defaultFilePath);
        if (!defaultFile.exists()) {
            throw new RuntimeException(defaultFile + "： 图表默认参数文件不存在");
        }

        loadDefaultFile(defaultFile);

    }

    private void loadDefaultFile(File defaultFile) {

    }
}
