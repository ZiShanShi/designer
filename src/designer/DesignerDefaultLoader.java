package designer;

import designer.exception.DesignerFileException;
import designer.options.ChartOption;
import designer.options.echart.json.GsonOption;
import designer.widget.Widget;
import designer.widget.theme.Theme;
import designer.xml.EDesignerXmlType;
import designer.xml.XmlReader;
import foundation.config.Configer;
import foundation.config.Preloader;
import foundation.util.Util;
import net.sf.json.JSONObject;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

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
        String topicPartPath = Configer.getParam(DesignerConstant.DEFAULT_Topic_PART_PATH);
        String TopicRootPath = Util.filePathJion(rootPath, topicPartPath);
        DesignerUtil.checkFileLegality(TopicRootPath);

        String topicDefaultFilePath = Util.filePathJion(TopicRootPath, Util.stringJoin(DesignerConstant.DEFAULT, Util.Dot, suffix));
        File topicDefualtFile = DesignerUtil.checkFileLegality(topicDefaultFilePath);

        Widget defaultWidget = new Widget(DesignerConstant.DEFAULT, DesignerConstant.DEFAULT);
        defaultWidget.setPath(topicDefaultFilePath);
        XmlReader topicDefaultReader = new XmlReader(EDesignerXmlType.defaultTopic);
        topicDefaultReader.read(topicDefualtFile, defaultWidget);
        instance.setBaseWidget(defaultWidget);

        DesignerComponentFactory.getInstance().putTopic(defaultWidget);
    }

    private void loadThemeFile(File themeRootFile) {
        File[] files = themeRootFile.listFiles();
        for (File themeFile : files) {
            Theme theme = new Theme();
            String fileName = themeFile.getName();
            theme.setName(fileName.substring(0, fileName.lastIndexOf(Util.Dot)));
            String themeJson = null;
            try {
                themeJson = FileUtils.readFileToString(themeFile, "UTF-8");

            } catch (IOException e) {
//                e.printStackTrace();
                throw  new DesignerFileException(themeFile.getPath());
            }
            JSONObject themeObject = JSONObject.fromObject(themeJson);
            theme.setThemeJson(themeObject);
            DesignerComponentFactory.getInstance().putTheme(theme);
        }
    }

}
