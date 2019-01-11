package designer;

import designer.cache.FieldNode;
import designer.widget.EWidgetField;
import foundation.util.Util;

/**
 * @author kimi
 * @description fieldnode 修改包装
 * @date 2019-01-09 12:39
 */


public class ChangeNodeSegment {

    private EChangeType changeType;
    private FieldNode fieldNode;
    private String value;

    public ChangeNodeSegment(EChangeType changeType) {
        this.changeType = changeType;
        createNode(null);
    }

    public ChangeNodeSegment(EChangeType changeType,String fieldNodeName) {
        this.changeType = changeType;
        createNode(fieldNodeName);
    }

    private void createNode(String fieldNodeName) {
        FieldNode node = null;
        switch (changeType) {
            case filters:
            case axis:
            case hasgrid:
            case theme:
                node = new FieldNode(EWidgetField.valueOf(changeType.name()));
                break;
            case inversePositon:
            case inverseX:
            case inverseY:
            case inverse:
                node = new FieldNode(EWidgetField.axis);
                break;
            case gridField:
                node = new FieldNode(EWidgetField.gridOption);
            case unknown:
                //外部传进来
                if (!Util.isEmptyStr(fieldNodeName)) {
                    node = new FieldNode(fieldNodeName);
                }

            default:
                break;

        }
       this.fieldNode = node;
    }

    public EChangeType getChangeType() {
        return changeType;
    }

    public FieldNode getFieldNode() {
        return fieldNode;
    }

    public String getValue() {
        return value;
    }

    public ChangeNodeSegment setValue(String value) {
        this.value = value;
        return this;
    }

    public ChangeNodeSegment setFieldNode(FieldNode fieldNode) {
        this.fieldNode = fieldNode;
        return this;
    }
}
