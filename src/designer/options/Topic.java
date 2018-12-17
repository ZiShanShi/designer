package designer.options;

/**
 * @author kimi
 * @description 主题
 * @date 2018-12-17 20:10
 */


public class Topic {
    private String id;
    private String topicPath;

    public String getId() {
        return id;
    }

    public Topic setId(String id) {
        this.id = id;
        return this;
    }

    public String getTopicPath() {
        return topicPath;
    }

    public Topic setTopicPath(String topicPath) {
        this.topicPath = topicPath;
        return this;
    }
}
