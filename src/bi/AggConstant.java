package bi;

import bi.define.AggDirection;

import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;

public class AggConstant {

    public static final String BI_TABLE_THEMEGROUP = "bi_themegroup";

    public static final String BI_Filter_Active = "active = 'T'";
    public static final String BI_Field_No = "no";
    public static final String BI_Field_Id = "id";
    public static final String BI_Field_Code = "code";
    public static final String BI_Field_Name = "name";
    public static final String BI_Field_Type = "type";
    public static final String BI_Field_Active = "active";
    public static final String BI_Field_Topictype = "topictype";
    public static final String BI_Field_Aggcode = "aggcode";
    public static final String BI_Field_Flowid = "flowid";

    public static final String BI_TABLE_THEMEGROUPMAP = "bi_themegroupmap";

    public static final String BI_Field_ThemeGroupId = "themegroupid";
    public static final String BI_Field_RealTable = "realtable";
    public static final String BI_Field_DimensionGroups = "dimensiongroups";
    public static final String BI_Field_Measurmentgroups = "measurmentgroups";


    public static final String BI_TABLE_THEMEG = "bi_theme";
    public static final String BI_Theme_DefaultName = "@{dimension}-@{measurment}聚合";
    public static final String BI_Field_Measurment = "measurment";
    public static final String BI_Field_Dimension = "dimension";

    public static final String BI_TABLE_SqlTemplate = "bi_sqltemplate";
    public static final String BI_Field_Sqltemplateid = "sqltemplateid";
    public static final String BI_Field_Targettable = "targettable";
    public static final String BI_Field_Sourcetable = "sourcetable";
    public static final String BI_Field_Sql = "sql";

    public static final String BI_SqlTemplate_DefaultAchieveSqlId = "Achieve-Insert";
    public static final String BI_SqlTemplate_DefaultGowthSqlId = "Gowth-Update";
    public static final String BI_SqlTemplate_DefaultRank_InsertSqlId = "Rank_Insert";

    public static final String BI_TABLE_Dimension = "bi_dimension";
    public static final String BI_Field_GroupId = "groupid";

    public static final String BI_TABLE_Measurment = "bi_measurment";


    public static final String BI_TABLE_TABLE = "bi_table";
    public static final String BI_TABLE_FLEID = "bi_field";

    public static final String BI_TABLE_TDIMENSIONGROUP = "bi_dimensiongroup";
    public static final String BI_TABLE_Fieldname = "fieldname";
    public static final String BI_TABLE_Parentcode = "parentcode";


    public static final String Sql_createTableTemplate = "createTableTemplate";
    public static final String Sql_Field_tableName = "tableName";
    public static final String Sql_Field_fields = "fields";
    public static final String Sql_getDimensionByCodeGroupId = "getDimensionByCodeGroupId";
    public static final String Sql_getMeasurmentByCode = "getMeasurmentByCode";

    public static final String Create_CommonField_Template = "[{0}] nvarchar({1}) NULL";
    public static final String Create_Primary_Template = " PRIMARY KEY ";
    public static final int CommonField_Default_Length = 64;
    public static final String Create_AggField_Template = "[{0}] decimal({1},{2}) NULL";
    public static final int AggField_Default_Length = 12;
    public static final int AggField_Default_decimalLength = 4;


    public static final String BI_Default_TopicView = "topic_";
    public static final String BI_Default_ThemeGroup = "TG_";
    public static final String BI_Default_Agg = "agg_";
    public static final String BI_Default_As = "as";

    public static final String CONNECTION_TABLE = "TABLE";
    public static final String CONNECTION_VIEW = "VIEW";
    public static final String CONNECTION_TABLE_NAME = "TABLE_NAME";
    public static final String CONNECTION_Field_Template = "Select * from {0}";

    public static final String AGG_As_Template = "({0}) as {0}";
    public static final String AGG_Sum_Template = "sum({0}) as {1}";
    public static final String Agg_Target = "target";
    public static final String Agg_Source= "source";

    public static ArrayList<String> filedUnCatchList = new ArrayList<>();
    public static HashMap<Integer, AggDirection> AggDefaultSqlTypeMap = new HashMap<>();

    static {
        filedUnCatchList.add(BI_Field_Id);
        filedUnCatchList.add(BI_Field_Active);
        filedUnCatchList.add(BI_Field_No);
        filedUnCatchList.add(BI_Field_Topictype);
        filedUnCatchList.add(BI_Field_Aggcode);

        filedUnCatchList.add(BI_Field_Flowid);

        AggDefaultSqlTypeMap.put(Types.INTEGER, AggDirection.Dimension);
        AggDefaultSqlTypeMap.put(Types.VARCHAR, AggDirection.Dimension);
        AggDefaultSqlTypeMap.put(Types.NVARCHAR, AggDirection.Dimension);

        AggDefaultSqlTypeMap.put(Types.DECIMAL, AggDirection.Measurment);
    }

}
