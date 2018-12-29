package designer.options;

/**
 * @author kimi
 * @description 维度
 * @date 2018-12-26 17:52
 */


public class Dimension {
    private String name;
    private String caption;

    public Dimension(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Dimension setName(String name) {
        this.name = name;
        return this;
    }

    public String getCaption() {
        return caption;
    }

    public Dimension setCaption(String caption) {
        this.caption = caption;
        return this;
    }
}
