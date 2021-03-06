package designer;

import designer.options.ChartOption;
import designer.options.echart.json.GsonOption;
import designer.widget.Widget;
import designer.widget.theme.Theme;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class DesignerComponentFactory {
    private static DesignerComponentFactory ourInstance;
    private GsonOption defautOption;
    private Widget baseWidget;
    private Map<String, Theme> themeMap;
    private Map<String, Widget> topicMap;
    private Map<String, ChartOption> optionMap;

    public static DesignerComponentFactory getInstance() {
        if (ourInstance == null) {
            synchronized (DesignerComponentFactory.class) {
                if (ourInstance == null) {
                    ourInstance = new DesignerComponentFactory();
                }
            }
        }
        return ourInstance;
    }

    private DesignerComponentFactory() {
        themeMap = new HashMap<>();
        optionMap = new HashMap<>();
        topicMap = new HashMap<>();
    }

    public void putTheme(Theme theme) {
        String name = theme.getName();
        if (themeMap == null) {
            themeMap = new HashMap<>();
        }
        themeMap.put(name, theme);
    }
    public void putOption(ChartOption option) {
        String topicId = option.getTopicId();
        if (optionMap == null) {
            optionMap = new HashMap<>();
        }
        optionMap.put(topicId, option);
    }
    public void putTopic(Widget widget) {
        String id = widget.getId();
        if (topicMap == null) {
            topicMap = new HashMap<>();
        }
        topicMap.put(id, widget);
    }

    public Theme getThemeByName(String name) {
        return  themeMap.get(name);
    }

    public GsonOption getDefautOption() {
        return defautOption;
    }

    public DesignerComponentFactory setDefautOption(GsonOption defautOption) {

        this.defautOption = defautOption;
        return this;
    }

    public Widget getBaseWidget() {
        return baseWidget;
    }

    public DesignerComponentFactory setBaseWidget(Widget baseWidget) {
        this.baseWidget = baseWidget;
        return this;
    }

    public void refreshTheme(File file) {
        themeMap.remove(file.getName());
        DesignerUtil.loadOneTheme(file);
    }
}
