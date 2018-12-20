package designer.topic;

/**
 * @author kimi
 * @description fileter 组合对
 * @date 2018-12-20 18:39
 */


public class SegmentPart {
    private String name;
    private String value;
    private String link;
    private ESqlValueType valueType;

    public String getName() {
        return name;
    }

    public SegmentPart setName(String name) {
        this.name = name;
        return this;
    }

    public String getValue() {
        return value;
    }

    public SegmentPart setValue(String value) {
        this.value = value;
        return this;
    }

    public String getLink() {
        return link;
    }

    public SegmentPart setLink(String link) {
        this.link = link;
        return this;
    }

    public ESqlValueType getValueType() {
        return valueType;
    }

    public SegmentPart setValueType(ESqlValueType valueType) {
        this.valueType = valueType;
        return this;
    }

    @Override
    public String toString() {
        return name + link + value;
    }
}
