package designer.widget;

import foundation.util.Util;

/**
 * @author kimi
 * @description fileter 组合对
 * @date 2018-12-20 18:39
 */


public class SegmentPart {
    private String name;
    private String value;
    private EFilterLink link;
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

    public EFilterLink getLink() {
        return link;
    }

    public SegmentPart setLink(String link) {
        this.link = EFilterLink.parse(link);
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
    public int hashCode() {
        return name.hashCode() + value.hashCode() + link.hashCode() + valueType.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof SegmentPart) {
            return this.hashCode() == obj.hashCode();
        }
        return super.equals(obj);
    }

    @Override
    public String toString() {
        switch (link) {
            case equal:
                if (valueType.equals(ESqlValueType.value)) {
                    return Util.stringJoin(name, Util.Equal, Util.quotedStr(value));
                } else if (valueType.equals(ESqlValueType.field)){
                    return Util.stringJoin(name, Util.Equal, value);
                }

                break;
            case like:
                if (valueType.equals(ESqlValueType.value)) {
                    return Util.stringJoin(name, Util.SqlLike, Util.quotedLikeStr(value));
                } else if (valueType.equals(ESqlValueType.field)){
                   return Util.stringJoin(name, Util.SqlLike, Util.bracketStr(value));
                }

            case likeleft:
                if (valueType.equals(ESqlValueType.value)) {
                    return Util.stringJoin(name, Util.SqlLike, Util.quotedLeftLikeStr(value));
                } else if (valueType.equals(ESqlValueType.field)){
                    //return Util.stringJoin(name, Util.SqlLike, Util.bracketStr(value));
                }
            case likeright:
                if (valueType.equals(ESqlValueType.value)) {
                    return Util.stringJoin(name, Util.SqlLike, Util.quotedRightLikeStr(value));
                } else if (valueType.equals(ESqlValueType.field)){
                    //return Util.stringJoin(name, Util.SqlLike, Util.bracketStr(value));
                }

            default:
                break;

        }
        return name + link + value;
    }
}
