package bi.define;

import bi.AggConstant;
import bi.AggUtil;
import bi.exception.AggDBlLoadException;
import foundation.data.Entity;
import foundation.data.EntitySet;
import foundation.persist.DataHandler;
import foundation.persist.TableMeta;
import foundation.persist.sql.NamedSQL;
import foundation.persist.sql.SQLRunner;
import foundation.util.ContentBuilder;
import foundation.util.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class AggThemeGroup {

    private static final String HISTORY_SPLIT = ";";

    private List<AggTheme> aggThemesMapList;
    private String code;
    private String name;
    private Logger logger;


    public AggThemeGroup(Entity themeGroup) {
        aggThemesMapList = new ArrayList<>();
        logger = LoggerFactory.getLogger(this.getClass());
        load(themeGroup);
    }

    private void load(Entity themeGroup) {
        String id = themeGroup.getString(AggConstant.BI_Field_Id);
        if (Util.isEmptyStr(id)) {
            throw new AggDBlLoadException("themeGroup id is null");
        }
        this.code = themeGroup.getString(AggConstant.BI_Field_Code);
        this.name = themeGroup.getString(AggConstant.BI_Field_Name);
        try {
            EntitySet themeGroupMapEntitySet = DataHandler.getDataSet(AggConstant.BI_TABLE_THEMEGROUPMAP, Util.quotedEqualStr(AggConstant.BI_Field_ThemeGroupId, id));

            EntitySet dataSet = DataHandler.getDataSet(AggConstant.BI_TABLE_THEMEG, "groupid = " + Util.quotedStr(id));
            if (dataSet.size() == 0) {
                initThemeByGroup(themeGroupMapEntitySet, dataSet);
                AggTableContainer.getInstance().load();
            }

             for (Entity entity : dataSet) {
                AggTheme aggTheme = new AggTheme(entity);
                aggThemesMapList.add(aggTheme);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void initThemeByGroup(EntitySet themeGroupMapSet, EntitySet dataSet) throws Exception {
        for (Entity themeGroupMapEntity : themeGroupMapSet) {
            addOneTable(themeGroupMapEntity,dataSet);
        }
    }

    private void addOneTable(Entity themeGroupMapEntity, EntitySet dataSet) throws Exception {
        TableMeta tableMeta = dataSet.getTableMeta();

        String measurmentGroups = themeGroupMapEntity.getString(AggConstant.BI_Field_Measurmentgroups);
        List<String> measurmentList = Util.StringToList(measurmentGroups, Util.semicolon);
        String measurmentStrs = measurmentList.stream().map(measurment -> Util.quotedStr(measurment)).collect(Collectors.joining(Util.comma));
        NamedSQL getmeasurmentSql = NamedSQL.getInstance(AggConstant.Sql_getMeasurmentByCode);
        getmeasurmentSql.setParam(AggConstant.BI_Field_Code, measurmentStrs);
        EntitySet entitySet = SQLRunner.getEntitySet(getmeasurmentSql);
        String measurments = entitySet.getEntityList().stream().map(entity -> entity.getString(AggConstant.BI_Field_Code)).collect(Collectors.joining(Util.semicolon));

        String dimensionGroupStr = themeGroupMapEntity.getString(AggConstant.BI_Field_DimensionGroups);
        ArrayList<String> dimensionGroupList = Util.StringToList(dimensionGroupStr, Util.semicolon);
        String[] dimensionGroupArray = new String[dimensionGroupList.size()];
        dimensionGroupList.toArray(dimensionGroupArray);
        ArrayList<ArrayList<String>> dimensionGroups = new ArrayList<>();
        Util.combine(dimensionGroupArray, dimensionGroups);

        ArrayList<String> maxSizeDimensionList = dimensionGroups.get(0);
        for (ArrayList<String> oneDimensionGroups : dimensionGroups) {
            Entity entity = new Entity(tableMeta);
            entity.set(0, Util.newShortGUID());
            entity.set(1, AggConstant.BI_Theme_DefaultName);
            entity.set(2, AggConstant.BI_SqlTemplate_DefaultAchieveSqlId);

            String groupId = themeGroupMapEntity.getString(AggConstant.BI_Field_ThemeGroupId);
            entity.set(3, groupId);

            String oneDimensionStr = oneDimensionGroups.stream().map(dimension -> MessageFormat.format("{0}.*", dimension)).collect(Collectors.joining(Util.semicolon));
            entity.set(4, oneDimensionStr);
            entity.set(5, measurments);
            String topicTableName = themeGroupMapEntity.getString(AggConstant.BI_Field_RealTable);
            entity.set(6, topicTableName);

            String subAggName = calculateTableName(oneDimensionGroups, maxSizeDimensionList);
            String topicType = topicTableName.substring(AggConstant.BI_Default_TopicView.length());
            String aggTableName = MessageFormat.format("agg_{0}_{1}", topicType, subAggName);
            if (!AggUtil.checkTableExists(aggTableName)) {
                boolean created = createAggTable(aggTableName, groupId, oneDimensionGroups, measurmentList);
                if (!created) {
                    throw new AggDBlLoadException("{0} 创建失败", aggTableName);
                }
            }

            entity.set(7, aggTableName);
            entity.set(8, Util.TRUE);
            entity.insert();

            dataSet.append(entity);
        }
    }

    private boolean createAggTable(String aggTableName, String groupId, ArrayList<String> oneDimensionGroups, List<String> measurmentList) throws Exception {
        AggOperator aggOperator = AggOperator.valueOf(groupId);
        if (aggOperator == null) {
            return false;
        }
        ArrayList<String> dimensionFieldList = new ArrayList<>();
        for (String oneDimensionGroupCode : oneDimensionGroups) {
            EntitySet dataSet = DataHandler.getDataSet(AggConstant.BI_TABLE_Dimension, Util.quotedEqualStr(AggConstant.BI_Field_GroupId, oneDimensionGroupCode));
            List<String> fieldList = dataSet.getFieldList(AggConstant.BI_Field_Code);
            dimensionFieldList.addAll(fieldList);
        }
        String fields = dimensionFieldList.stream()
                .map(dimension -> MessageFormat.format(AggConstant.Create_CommonField_Template, dimension, AggConstant.CommonField_Default_Length))
                .collect(Collectors.joining(Util.comma));
        switch (aggOperator) {
            case Sum:
                String measurments = measurmentList.stream()
                        .map(measurment -> MessageFormat.format(AggConstant.Create_AggField_Template, measurment, AggConstant.AggField_Default_Length, AggConstant.AggField_Default_decimalLength))
                        .collect(Collectors.joining(Util.comma));
                fields += Util.stringJoin(Util.comma, measurments);
                break;
            case Ahieve:
            case Growth:
                // 增加指标等字段
                break;
            default:
                break;

        }
        NamedSQL createSql = NamedSQL.getInstance(AggConstant.Sql_createTableTemplate);
        createSql.setParam(AggConstant.Sql_Field_tableName, aggTableName);
        createSql.setParam(AggConstant.Sql_Field_fields, fields);
        int i = SQLRunner.execSQL(createSql);
        if (i == 0) {
            return true;
        } else {
            return false;
        }
    }

    private  String calculateTableName(ArrayList<String> dimensionList, ArrayList<String> maxSizeDimensionList) {
        ContentBuilder builder = new ContentBuilder(Util.SubSeparator);
        for (String dimension : dimensionList) {
            String subDimension = getNoSameSub(dimension, maxSizeDimensionList);
            builder.append(subDimension);
        }

        return builder.toString();
    }

    private String getNoSameSub(String dimension, ArrayList<String> dimensionList) {
        int index = 1;

        while (index < dimension.length()) {
            String subDimension = dimension.substring(0, index);
            boolean sameSub = true;
            for (String oneDimension : dimensionList) {
                if (oneDimension.equalsIgnoreCase(dimension)) {
                    continue;
                }
                sameSub = oneDimension.startsWith(subDimension);
                if (sameSub) {
                    break;
                }
            }
            if (!sameSub) {
                return subDimension;
            }

            index++;
        }
        return dimension;
    }



    public List<AggTheme> getAggThemesMapList() {
        return aggThemesMapList;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

}
