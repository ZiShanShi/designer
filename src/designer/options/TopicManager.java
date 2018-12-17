package designer.options;

import java.util.HashMap;
import java.util.Map;

/**
 * @author kimi
 * @description 主题管理类
 * @date 2018-12-17 20:27
 */


public class TopicManager {
    private static  TopicManager manager;
    private Map<String ,IOption>  TopicMap;

    public static TopicManager getInstance() {
        if (manager != null) {
            synchronized (TopicManager.class) {
                if (manager != null) {
                    manager = new TopicManager();
                }
            }
        }
        return manager;
    }

    private  TopicManager () {
        TopicMap = new HashMap<>();
    }


}
