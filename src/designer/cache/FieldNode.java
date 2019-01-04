package designer.cache;

import foundation.util.Util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author kimi
 * @description 确定唯一字段包装类
 * @date 2018-12-25 14:53
 */


public class FieldNode {
    private String preName;
    private String fieldName;
    private Class fieldClaz;
    private Boolean isParent = false;
    public FieldNode() {}

    public FieldNode(String fieldName) {
        this.fieldName = fieldName;
    }

    public FieldNode(Class fieldClaz, String fieldName) {
        this.fieldName = fieldName;
        this.fieldClaz = fieldClaz;
    }


    public String getPreName() {
        return preName;
    }

    public FieldNode setPreName(FieldNode preNode) {
        String preName = preNode.toString();
        this.preName = preName;
        preNode.setParent(true);
        return this;
    }

    public FieldNode addPreName(FieldNode node) {
        String fieldName = node.getFieldName();
        if (Util.isEmptyStr(this.preName) || this.preName.equalsIgnoreCase(fieldName)) {
            this.preName = fieldName;
            return this;
        }
        this.preName = Util.stringJoin(fieldName, Util.Separator, this.preName);
        node.setParent(true);
        return this;
    }

    public String getFieldName() {
        return fieldName;
    }

    public FieldNode setFieldName(String fieldName) {
        this.fieldName = fieldName;
        return this;
    }

    public List<String> getHierarchyList() {
        String[] split = Util.stringJoin(toString()).split(Util.Separator);
        List<String> strings = Arrays.asList(split);
        ArrayList<String> hierarchyList = new ArrayList<>(strings);
        return hierarchyList;
    }

    public int getLayer() {
        return  Util.stringJoin(toString()).split(Util.Separator).length;
    }

    public String getLayerFiledName(int layer) {
        String[] split = Util.stringJoin(toString()).split(Util.Separator);
        if (split.length < layer) {
            return  null;
        }
        return  split[layer - 1];
    }
    @Override
    public int hashCode() {
        if (fieldClaz != null) {
            return fieldClaz.hashCode() + fieldName.hashCode();
        } else if (fieldName != null) {
            return fieldName.hashCode();
        } else {
            return super.hashCode();
        }

    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof FieldNode) {
            return this.hashCode() == obj.hashCode();
        } else {
            return super.equals(obj);
        }

    }

    public Boolean isParent() {
        return isParent;
    }

    public FieldNode setParent(Boolean parent) {
        isParent = parent;
        return this;
    }

    @Override
    public String toString() {
        if (Util.isEmptyStr(preName)) {
            return fieldName;
        } else {
            return Util.stringJoin(preName,Util.Separator,fieldName);
        }

    }
}
