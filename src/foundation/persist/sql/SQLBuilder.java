package foundation.persist.sql;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import foundation.persist.Field;
import foundation.persist.TableMeta;
import foundation.util.Util;


public class SQLBuilder {

	public static String getCreateTableSQL(TableMeta tableMeta, String table, Collection<Field> fields) {
		Set<Field> allFields = new HashSet<Field>();
		
		allFields.addAll(tableMeta.getFields());
		
		allFields.addAll(fields);
		
		return getCreateTableSQL(allFields, table);
	}
	
	public static String getCreateTableSQL(TableMeta tableMeta, String table) {
		Set<Field> fields = new HashSet<Field>();
		
		fields.addAll(tableMeta.getFields());

		return getCreateTableSQL(fields, table);
	}
	
	private static String getCreateTableSQL(Collection<Field> fields, String table) {
		if (table == null) {
			return null;
		}
		
		StringBuilder result = new StringBuilder();
		result.append("CREATE TABLE ").append(table.toUpperCase()).append("(");
		boolean empty = true;
		
		for (Field field: fields) {
			if (!empty) {
				result.append(", ");
			}
			
			String name = field.getName();
			result.append(Util.doubleQuotedStr(name.toUpperCase())).append(" ");
			result.append(field.getSQLTypeCode());
			
			empty = false;
		}
		
		result.append(")");
		
		return result.toString();		
	}

	public static String getModifyTableSQL(List<Field> fieldList, String table) {
		StringBuilder result = new StringBuilder();
		result.append("ALTER TABLE ").append(table.toUpperCase()).append(" ADD (");
		boolean empty = true;
		
		for (Field field: fieldList) {
			if (!empty) {
				result.append(", ");
			}
			
			result.append(field.getName().toUpperCase()).append(" ");
			result.append(field.getSQLTypeCode()).append(field.getSQLNullCode());
			
			empty = false;
		}
		
		result.append(")");
		
		return result.toString();
	}

	public static String getCreateIndexSQL(String table, String[] keyFields) {
		if (table == null) {
			return null;
		}
		
		StringBuilder result = new StringBuilder();
		result.append("CREATE UNIQUE INDEX ").append("IDX_").append(table.toUpperCase());
		result.append(" ON ").append(table).append(" (");
		
		boolean empty = true;
		
		for (String field: keyFields) {
			if (!empty) {
				result.append(", ");
			}
			
			result.append(field);
			empty = false;
		}
		
		result.append(")");
		
		return result.toString();
	}
}
