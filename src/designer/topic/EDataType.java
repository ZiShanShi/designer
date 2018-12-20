package designer.topic;

import foundation.util.Util;

public enum EDataType {
    db,sql,unknown;

    public static EDataType parse(String type) {
        if (Util.isEmptyStr(type)) {
            return  unknown;
        }
        if (type.equalsIgnoreCase(db.name())) {
            return  db;
        } else if (type.equalsIgnoreCase(sql.name())) {
            return sql;
        } else {
            return  unknown;
        }

    }
}
