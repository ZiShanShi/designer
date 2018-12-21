package designer.options;

import designer.options.echart.json.GsonOption;
import designer.topic.Topic;

import java.util.HashMap;
import java.util.Map;

public class DesignerComponentFactory {
    private static DesignerComponentFactory ourInstance;
    private GsonOption defautOption;
    private designer.topic.Topic baseTopic;
    private Map<String, Theme> themeMap;
    private Map<String, Topic> topicMap;
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
    public void putTopic(Topic topic) {
        String id = topic.getId();
        if (topicMap == null) {
            topicMap = new HashMap<>();
        }
        topicMap.put(id, topic);
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

    public Topic getBaseTopic() {
        return baseTopic;
    }

    public DesignerComponentFactory setBaseTopic(Topic baseTopic) {
        this.baseTopic = baseTopic;
        return this;
    }
}
