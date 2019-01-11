package designer;

import designer.options.ChartOption;
import designer.options.echart.json.GsonOption;
import designer.widget.Widget;
import designer.xml.EDesignerXmlType;
import designer.xml.XmlReader;
import foundation.config.Configer;
import foundation.config.Preloader;
import foundation.util.Util;
import org.apache.commons.io.monitor.FileAlterationMonitor;
import org.apache.commons.io.monitor.FileAlterationObserver;

import java.io.File;
import java.util.concurrent.TimeUnit;

/**
 * @author kimi
 * @description default文件;初始加载类
 * @date 2018-12-17 20:31
 */


public class DesignerDefaultLoader extends Preloader {

    private String rootPath;

    @Override
    public void load() {
        initOptionDefault();
    }

    private void initOptionDefault() {

        rootPath = Configer.getParam(DesignerConstant.DEFAULT_CHART_ROOT_PATH);
        rootPath = rootPath.replaceAll(DesignerConstant.ROOT,Configer.getPath_Application());
        DesignerUtil.checkFileLegality(rootPath);

        loadDefaultOpiton();

        loadDefaultWidget();

        loadDefaultThemes();

        // 开启文件监听
        startFileListener(rootPath);
    }

    private void startFileListener(String rootPath) {
        //TODO PATH
        String fileListenerTimeStr = Configer.getParam(DesignerConstant.fileListenerTime);
        int fileListenerTime;
        if (Util.isEmptyStr(fileListenerTimeStr)) {
            fileListenerTime = 5;
        } else {
            fileListenerTime = Util.stringToInt(fileListenerTimeStr, 5);
        }
        // 轮询间隔 5 秒
        long interval = TimeUnit.SECONDS.toMillis(fileListenerTime);
        // 创建一个文件观察器用于处理文件的格式
        FileAlterationObserver observer = new FileAlterationObserver("D:\\workspace\\finebi\\source\\my\\desginer\\out\\artifacts\\designer_Web_exploded\\chartTemplate");
        //设置文件变化监听器
        observer.addListener(new FileListener());
        //创建文件变化监听器
        FileAlterationMonitor monitor = new FileAlterationMonitor(interval, observer);
        // 开始监控
        try {
            monitor.start();
            logger.info("开启文件监听");
        } catch (Exception e) {
            logger.error("开启文件监听失败");
            e.printStackTrace();
        }
    }

    private void loadDefaultOpiton() {
        String defaultEngine = Configer.getParam(DesignerConstant.DEFAULT_CHART_ENGINE);
        String defaultPath = Util.filePathJion(rootPath, DesignerConstant.DEFAULT);
        DesignerUtil.checkFileLegality(defaultPath);

        String chartEngintPath = Util.filePathJion(defaultPath, defaultEngine);
        DesignerUtil.checkFileLegality(chartEngintPath);
        String suffix = Configer.getParam(DesignerConstant.DEFAULT_CHART_SUFFIX);
        String chartTemplePath = Util.filePathJion(defaultPath, Util.stringJoin(defaultEngine, Util.Dot, suffix));
        File chartDefaultFile = DesignerUtil.checkFileLegality(chartTemplePath);

        DesignerComponentFactory instance = DesignerComponentFactory.getInstance();

        XmlReader chartDefaultReader = new XmlReader(EDesignerXmlType.defaultChart);
        GsonOption option = new GsonOption();
        chartDefaultReader.read(chartDefaultFile, option);
        instance.setDefautOption(option);

        ChartOption chartOption = new ChartOption("default");
        chartOption.setRealChartOption(option);
        DesignerComponentFactory.getInstance().putOption(chartOption);
    }

    private void loadDefaultThemes() {
        String themePartPath = Configer.getParam(DesignerConstant.DEFAULT_Theme_PART_PATH);
        String themeRootPath = Util.filePathJion(rootPath, themePartPath);
        File themeRootFile = DesignerUtil.checkFileLegality(themeRootPath);
        loadThemeFile(themeRootFile);
    }

    private void loadDefaultWidget() {
        DesignerComponentFactory instance = DesignerComponentFactory.getInstance();
        String suffix = Configer.getParam(DesignerConstant.DEFAULT_CHART_SUFFIX);
        String topicPartPath = Configer.getParam(DesignerConstant.DEFAULT_WIDGET_PART_PATH);
        String TopicRootPath = Util.filePathJion(rootPath, topicPartPath);
        DesignerUtil.checkFileLegality(TopicRootPath);

        String topicDefaultFilePath = Util.filePathJion(TopicRootPath, Util.stringJoin(DesignerConstant.DEFAULT, Util.Dot, suffix));
        File topicDefualtFile = DesignerUtil.checkFileLegality(topicDefaultFilePath);

        Widget defaultWidget = new Widget(DesignerConstant.DEFAULT, DesignerConstant.DEFAULT);
        defaultWidget.setPath(topicDefaultFilePath);
        XmlReader topicDefaultReader = new XmlReader(EDesignerXmlType.defaultWidget);
        topicDefaultReader.read(topicDefualtFile, defaultWidget);
        instance.setBaseWidget(defaultWidget);

        DesignerComponentFactory.getInstance().putTopic(defaultWidget);
    }

    private void loadThemeFile(File themeRootFile) {
        File[] files = themeRootFile.listFiles();
        for (File themeFile : files) {
            DesignerUtil.loadOneTheme(themeFile);
        }
    }

}
