package designer;

import designer.options.DesignerComponentFactory;
import designer.options.Theme;
import designer.options.echart.json.GsonOption;
import designer.topic.Topic;
import designer.xml.EDesignerXmlType;
import designer.xml.XmlReader;
import foundation.config.Configer;
import foundation.config.Preloader;
import foundation.util.Util;

import java.io.File;

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

        loadDefaultTopic();

        loadDefaultThemes();
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
    }

    private void loadDefaultThemes() {
        String themePartPath = Configer.getParam(DesignerConstant.DEFAULT_Theme_PART_PATH);
        String themeRootPath = Util.filePathJion(rootPath, themePartPath);
        File themeRootFile = DesignerUtil.checkFileLegality(themeRootPath);
        loadThemeFile(themeRootFile);
    }

    private void loadDefaultTopic() {
        DesignerComponentFactory instance = DesignerComponentFactory.getInstance();
        String suffix = Configer.getParam(DesignerConstant.DEFAULT_CHART_SUFFIX);
        String topicPartPath = Configer.getParam(DesignerConstant.DEFAULT_Topic_PART_PATH);
        String TopicRootPath = Util.filePathJion(rootPath, topicPartPath);
        DesignerUtil.checkFileLegality(TopicRootPath);

        String topicDefaultFilePath = Util.filePathJion(TopicRootPath, Util.stringJoin(DesignerConstant.DEFAULT, Util.Dot, suffix));
        File topicDefualtFile = DesignerUtil.checkFileLegality(topicDefaultFilePath);

        Topic defaultTopic = new Topic(DesignerConstant.DEFAULT, DesignerConstant.DEFAULT);
        XmlReader topicDefaultReader = new XmlReader(EDesignerXmlType.defaultTopic);
        topicDefaultReader.read(topicDefualtFile,defaultTopic);
        instance.setBaseTopic(defaultTopic);
    }

    private void loadThemeFile(File themeRootFile) {
        File[] files = themeRootFile.listFiles();
        for (File themeFile : files) {
            Theme theme = new Theme();
            XmlReader topicDefaultReader = new XmlReader(EDesignerXmlType.theme);
            topicDefaultReader.read(themeFile,theme);
            DesignerComponentFactory.getInstance().putTheme(theme);
        }
    }

}
