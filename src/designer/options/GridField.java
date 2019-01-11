package designer.options;

import designer.DesignerConstant;
import designer.options.echart.code.Align;

/**
 * @author kimi
 * @description grid 展示字段 包装类
 * @date 2018-12-27 10:47
 */


public class GridField {
    private String field;
    private String caption;
    private String width;
    private Align align;
    private Boolean istitle;
    private Boolean groupBy;

    public GridField(String field) {
        this.field = field;
        width = DesignerConstant.GRID_DEFAULT_WIDTH;
        align = Align.valueOf(DesignerConstant.GRID_DEFAULT_ALIGN);
        groupBy = Boolean.valueOf(DesignerConstant.GRID_DEFAULT_GROUPBY);
        istitle = Boolean.valueOf(DesignerConstant.GRID_DEFAULT_ISTITLE);
    }

    public String getField() {
        return field;
    }

    public GridField setField(String field) {
        this.field = field;
        return this;
    }

    public String getCaption() {
        return caption;
    }

    public GridField setCaption(String caption) {
        this.caption = caption;
        return this;
    }

    public String getWidth() {
        return width;
    }

    public GridField setWidth(String width) {
        this.width = width;
        return this;
    }

    public Align getAlign() {
        return align;
    }

    public GridField setAlign(Align align) {
        this.align = align;
        return this;
    }

    public Boolean getIstitle() {
        return istitle;
    }

    public GridField setIstitle(Boolean istitle) {
        this.istitle = istitle;
        return this;
    }

    public Boolean getGroupBy() {
        return groupBy;
    }

    public GridField setGroupBy(Boolean groupBy) {
        this.groupBy = groupBy;
        return this;
    }

    public GridField setDefault() {
        if (width == null) {
            width = DesignerConstant.GRID_DEFAULT_WIDTH;
        }
        if (align == null) {
            align = Align.valueOf(DesignerConstant.GRID_DEFAULT_ALIGN);
        }
        if (groupBy == null) {
            groupBy = Boolean.valueOf(DesignerConstant.GRID_DEFAULT_GROUPBY);
        }
        if (istitle == null) {
            istitle = Boolean.valueOf(DesignerConstant.GRID_DEFAULT_ISTITLE);
        }
        return this;
    }
    @Override
    public int hashCode() {
        /*if (Util.isEmptyStr(caption)) {
            return  field.hashCode() + width.hashCode() + align.hashCode() + groupBy.hashCode() + istitle.hashCode();
        } else {
            return  field.hashCode() + caption.hashCode() + width.hashCode() + align.hashCode() + groupBy.hashCode() + istitle.hashCode();
        }*/
        return field.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof GridField) {
            return this.hashCode() == obj.hashCode();
        }
        return super.equals(obj);
    }
}
