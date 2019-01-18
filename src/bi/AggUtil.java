package bi;

import foundation.persist.SqlSession;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author kimi
 * @description 聚合工具类
 * @date 2019-01-15 15:23
 */


public class AggUtil {

    public static boolean checkTableExists(String tableName) {
        Connection connection = SqlSession.createConnection();
        ResultSet rs = null;
        try {
            rs = connection.getMetaData().getTables(null, null, tableName, null);
            if (rs.next()) {
                return true;
            }else {
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return false;
    }
}
