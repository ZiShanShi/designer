package foundation.persist;

import java.util.List;

import foundation.callable.Callable;
import foundation.callable.EntitySetResult;
import foundation.callable.IParameterProvider;
import foundation.callable.PageParameter;
import foundation.config.Configer;
import foundation.data.Entity;
import foundation.data.EntitySet;
import foundation.data.Page;
import foundation.persist.sql.NamedSQL;
import foundation.persist.sql.Result;
import foundation.persist.sql.ReturnType;
import foundation.persist.sql.SQLRunner;
import foundation.persist.sql.SQLVariant;
import foundation.server.Sysparam;
import foundation.util.Util;

public class DataObject extends Callable {

	private String data;
	private String operator;

	protected void doReceive(String[] paths) throws Exception {
		try {
			if (paths.length >= 3) {

				// 数据
				data = paths[1];
				// 操作
				operator = paths[2];

				if ("procedure".equalsIgnoreCase(data)) {
					execProcedure();
					resultPool.success();
				}
				else {
					if ("getDataSet".equalsIgnoreCase(operator)) {
						try {
							String filter = getFilter(data);
							String orderby = getOrderBy();
							EntitySet entitySet = DataHandler.getDataSet(data, filter, orderby);

							resultPool.addValue(entitySet);
						}
						catch (Exception e) {
							e.printStackTrace();
							throw e;
						}
					}
					else if ("getSetByPage".equalsIgnoreCase(operator)) {
						String filter = getFilter(data);
						int totalCount = DataHandler.getCount(data, filter);
						String orderby = getOrderBy();
						Page page = getPage(totalCount);
						EntitySet entitySet = DataHandler.getDataSetByPage(data, filter, page, orderby);

						resultPool.addValue("page", page);
						resultPool.addValue(entitySet);
					}
					else if ("getComboboxDataSet".equalsIgnoreCase(operator)) {
						String filter = getFilter(data);
						String orderby = getOrderBy();
						EntitySet entitySet = DataHandler.getDataSet(data, filter, orderby);
						resultPool.addValue(entitySet);
					}
					else if ("getLine".equalsIgnoreCase(operator)) {
						String id = getId();
						Entity entity = DataHandler.getLine(data, id);
						resultPool.addValue(entity);
					}
					else if ("deleteById".equalsIgnoreCase(operator)) {
						String id = getId();
						DataHandler.deleteById(data, id);
						resultPool.success();
					}
					else if ("newObject".equalsIgnoreCase(operator)) {
						String id = Util.newShortGUID();
						resultPool.addValue("objectId", id);
					}
					else if ("addLine".equalsIgnoreCase(operator)) {
						Entity entity = new Entity(data);
						getEntity(entity);

						DataHandler.addLine(entity);
						resultPool.success();
					}
					else if ("saveLine".equalsIgnoreCase(operator)) {
						Entity entity = new Entity(data);
						getEntity(entity);

						DataHandler.saveLine(entity);
						resultPool.success();
					}
					else if ("getCount".equalsIgnoreCase(operator)) {
						String filter = getFilter(data);
						int totalCount = DataHandler.getCount(data, filter);
						resultPool.addValue(totalCount);
					}
					else if ("getSysparams".equalsIgnoreCase(operator)) {
						List<Sysparam> list = Configer.getClientSysparams();
						resultPool.addValue(list);
					}
				}
			}
			else {
				writer.ReplyError("bad data message path:" + fullPath);
			}			
		}
		catch (Exception e) {
			logger.error("execute dataobject error: " + fullPath);
			throw e;
		}
	}
	
	private void getSetByPage(String tableName, PageParameter pageParameter) throws Exception { // 用于多表分页查询
		String filter = getFilter(tableName);
		
		EntitySetResult result = getSetByPage(operator, filter, pageParameter, this);

		resultPool.addValue(result.getEntitySet());
		resultPool.addValue("page", result.getPage());
	}
	
	public static EntitySetResult getSetByPage(String sqlname, String filter, PageParameter pageParameter, IParameterProvider parameterProvider) throws Exception { // 用于多表分页查询
		NamedSQL namedSQL = NamedSQL.getInstance(sqlname);
		String originalSql = namedSQL.getOriginalSql();
		int select = originalSql.indexOf("select");
		int from = originalSql.indexOf("from");
		int limit = originalSql.indexOf("limit");
		
		String fields = originalSql.substring(select + 6, from);
		String limits = originalSql.substring(limit, limit + 33);
		String replace = originalSql.replace(fields, " count(1) ").replace(limits, "");
		
		namedSQL = new NamedSQL(sqlname, replace);
		namedSQL.setReturnType(ReturnType.Int);
		namedSQL.setParam("filter", filter);
		
		for (SQLVariant variant : namedSQL) {
			if (variant.isEmpty()) {
				String name = variant.getName();
				String value = parameterProvider.getStringParam(name);
				
				if (value != null) {
					variant.setValue(value);
				}
			}
		}		
		
		int count = SQLRunner.getInteger(namedSQL);
		Page page = pageParameter.toPage(count);
		
		namedSQL = NamedSQL.getInstance(sqlname);
		namedSQL.setParam("filter", filter);
		namedSQL.setParam("beginrecord", page.getBeginRecordNo_1());
		namedSQL.setParam("pagesize", page.getPageSize());
		
		for (SQLVariant variant : namedSQL) {
			if (variant.isEmpty()) {
				String name = variant.getName();
				String value = parameterProvider.getStringParam(name);
				
				if (value != null) {
					variant.setValue(value);
				}
			}
		}
		
		EntitySet entitySet = SQLRunner.getEntitySet(namedSQL);

		EntitySetResult result = new EntitySetResult(entitySet, page);
		return result;
	}

	private void execProcedure() throws Exception {
		ReturnType returnType = null;
		
		if (paths.length > 3) {
			returnType = ReturnType.valueOfString(paths[3]);
		}
		
		PageParameter pageParameter = new PageParameter(request);
		
		if (!pageParameter.isEmpty()) {
			getSetByPage(operator, pageParameter);
		}
		else {
			NamedSQL namedSQL = NamedSQL.getInstance(operator);
			namedSQL.setReturnType(returnType);

			for (SQLVariant sqlVariant : namedSQL) {
				String name = sqlVariant.getName();
				String value = getStringParam(name);

				if (value == null) {
					logger.error("execute procedure error, empty param: " + name);
					return;
				}

				sqlVariant.setValue(value);
			}

			Result result = namedSQL.exec();
			resultPool.addValue(result.getObject());

			if (ReturnType.None == returnType) {
				resultPool.success();
			}			
		}
	}
}
