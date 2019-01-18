package bi;

import bi.agg.Operator;
import bi.theme.Theme;
import bi.theme.ThemeContainer;
import bi.work.ActivePeriod;
import foundation.callable.Callable;
import foundation.data.Entity;
import foundation.data.EntitySet;
import foundation.persist.DataHandler;
import foundation.persist.sql.SQLRunner;
import foundation.schedule.Engine;
import foundation.util.Util;

import java.lang.reflect.InvocationTargetException;

public class Console extends Callable {

	private static ThemeContainer themeContainer;
	public static boolean Test;
	public static WorkEngine engine;
	private String operator;
	private static final String dimissionPS = "dimissionPS";
	static {
		themeContainer = ThemeContainer.getInstance();
		engine = WorkEngine.getInstance();
	}

	protected void doReceive(String[] paths) throws Exception {
		if (paths.length >= 2) {
			operator = paths[1];

			if ("getdata".equalsIgnoreCase(operator)) {
				if (paths.length == 2) {
					logger.error("bad bi console message path:" + fullPath);
					writer.ReplyError("bad bi console message path:" + fullPath);
					return;
				}

				getData();
			}
			else if ("getmeta".equalsIgnoreCase(operator)) {
				getMeta();

			}
			else if ("calculate".equalsIgnoreCase(operator)) {
				calculate();

			}
			else if ("createBaseAggregation".equalsIgnoreCase(operator)) {
				createBaseAggregation();
			}
			else if ("showMsgs".equalsIgnoreCase(operator)) {
				showMessages();
			}
			else if ("getActivePeriod".equalsIgnoreCase(operator)) {
				resultPool.addValue("activePeriod", ActivePeriod.getInstance());
			}
			else if ("clearSystem".equalsIgnoreCase(operator)) {
				clearSystem();
			}
			else if ("resetWorkStatus".equalsIgnoreCase(operator)) {
				resetWorkStatus();
			}
			else if("sealplate".equalsIgnoreCase(operator)) {
				sealplate();
			}

		}
		else {
			writer.ReplyError("bad bi console message path:" + fullPath);
		}
	}

	private void sealplate() {
		try {
			Engine engine = Engine.getInstance();
			engine.exec("sealplate_right");
			resultPool.setMessage("200", "封板任务提交成功");
		} catch (Exception e) {
			e.printStackTrace();
			resultPool.setMessage("-1", "封板失败");
		}

	}


	/**
	 * @param month
	 * @param fiscalYear
	 * @return
	 * @throws Exception
	 */
	private Entity getPeroidLine(String month, String fiscalYear)
			throws Exception {
		EntitySet dataSet = DataHandler.getDataSet("peroid", "period  =" + Util.quotedStr(fiscalYear) + "and month = " + Util.quotedStr(month));
		Entity periodLine = dataSet.next();
		if (periodLine == null) {
			return null;
		}
		return periodLine;
	}

	private void showMessages() {
		resultPool.addValue(engine.getProgressor());
	}

	private void getData() throws Exception {
		String name = paths[2];
		Theme theme = themeContainer.get(name, false);


		if (theme == null) {
			logger.error("can not find theme: " + name);
			resultPool.error(ErrorCode.Err_ThemeNotExists, "can not find theme");
			return;
		}

		ThemeContext context = new ThemeContext(request, onlineUser);
		String roleid = context.getRoleId();

		if (roleid == null) {
			resultPool.error(ErrorCode.Err_InvalidRole, "invalid role");
			return;
		}

		context.setParametersTo(theme);
		EntitySet entitySet = SQLRunner.getEntitySet(theme);
		resultPool.addValue(entitySet);
	}

	private void getMeta() {
		// TODO Auto-generated method stub

	}

	private void createBaseAggregation()  {
		ThemeContext context = new ThemeContext(request, onlineUser);
		Operator operator = new Operator(context);
		try {
			engine.exec(operator, "createbaseaggregation");

			resultPool.setMessage("200", "聚合任务提交成功");
		} catch (Exception e) {
			e.printStackTrace();
			resultPool.error("-1", "聚合失败");
		}
	}

	private void calculate() throws Exception {
		ThemeContext context = new ThemeContext(request, onlineUser);
		Operator operator = new Operator(context);
		engine.exec(operator, "calculate");
	}

	private void clearSystem() throws IllegalArgumentException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
		ThemeContext context = new ThemeContext(request, onlineUser);
		Operator operator = new Operator(context);
		engine.exec(operator, "clearSystem");
	}

	private void resetWorkStatus() throws IllegalArgumentException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
		ThemeContext context = new ThemeContext(request, onlineUser);
		Operator operator = new Operator(context);
		engine.exec(operator, "resetWorkStatus");
	}

}
