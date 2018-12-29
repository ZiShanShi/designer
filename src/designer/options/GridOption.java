package designer.options;

import foundation.data.Entity;
import foundation.data.Page;

import java.util.ArrayList;
import java.util.List;

/**
 * @author kimi
 * @description 表格配置
 * @date 2018-12-19 11:10
 */


public class GridOption {
    private Boolean showPage;
    private Boolean hasTitle;
    private Boolean multiSelect;
    private Boolean enableAllSelect;
    private Page page;
    private List<GridField> fieldList;
    private transient List<String> groupList;
    private List<Entity> data;

    public GridOption() {
        fieldList = new ArrayList<>();
        groupList = new ArrayList<>();
        showPage = true;
        hasTitle = true;
        multiSelect = false;
        enableAllSelect = false;
    }

    public GridOption putOneField(GridField field) {
        if (fieldList == null) {
            fieldList = new ArrayList<>();
        }
        fieldList.add(field);
        return this;
    }
    public GridOption putGroupByField(String fieldStr) {
        if (groupList == null) {
            groupList = new ArrayList<>();
        }
        groupList.add(fieldStr);
        return this;
    }

    public Boolean getShowPage() {
        return showPage;
    }

    public GridOption setShowPage(Boolean showPage) {
        this.showPage = showPage;
        return this;
    }

    public Boolean getMultiSelect() {
        return multiSelect;
    }

    public GridOption setMultiSelect(Boolean multiSelect) {
        this.multiSelect = multiSelect;
        return this;
    }

    public Boolean getEnableAllSelect() {
        return enableAllSelect;
    }

    public GridOption setEnableAllSelect(Boolean enableAllSelect) {
        this.enableAllSelect = enableAllSelect;
        return this;
    }

    public Page getPage() {
        return page;
    }

    public GridOption setPage(Page page) {
        this.page = page;
        return this;
    }

    public List<GridField> getFieldList() {
        return fieldList;
    }

    public List<String> getGroupList() {
        return groupList;
    }

    public List<Entity> getData() {
        return data;
    }

    public GridOption setData(List<Entity> data) {
        this.data = data;
        return this;
    }

    public Boolean getHasTitle() {
        return hasTitle;
    }

    public GridOption setHasTitle(Boolean hasTitle) {
        this.hasTitle = hasTitle;
        return this;
    }

    public GridOption putOneEntity(Entity entity) {
        if (data == null) {
            data = new ArrayList<>();
        }
        data.add(entity);
        return this;
    }

}
