package foundation.server;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import org.apache.log4j.Logger;

import foundation.callable.EnvelopWriter;
import foundation.callable.IEnvelop;
import foundation.callable.ISmartWriter;
import foundation.util.Util;

public class Progressor implements ISmartWriter {

	private String taskName;
	private Stack<Phase> phaseStack;
	private List<String> messageList;
	private LineNo lineNo;
	
	private long max;
	private long min;
	private long step;
	private long position;
	private long stepBuffer;
	private String stepMessage;
	
	protected static Logger logger;
	
	
	static {
		logger = Logger.getLogger(Progressor.class);
	}
	
	public Progressor() {
		messageList = new ArrayList<String>(100);
		phaseStack = new Stack<Phase>();
		lineNo = new LineNo();
		
		max = 0;
		min = 0;
		step = 0;
		position = 0;
		stepBuffer = 0;
		stepMessage = "0%--------0/0";
	}
	
	public void clearStep() {
		max = 0;
		min = 0;
		step = 0;
		position = 0;
		stepBuffer = 0;
		stepMessage = "0%--------0/0";
	}
	
	public void stepIt() {
		if (position < max) {
			position ++;	
			stepBuffer ++;
			
			if (stepBuffer >= step) {
				double percent = position * 100 / max;
				stepBuffer = 0;
				stepMessage = Math.round(percent) + "%" + "--------" + position + "/" + max;
				logger.debug(stepMessage);
			}
		}
		else {
			stepMessage = "100%" + "--------" + max + "/" + max;
		}
	}
	
	public void stepIt(int aStep) {
		if (position < max) {
			position = Math.min(position + aStep, max);	
			stepBuffer = stepBuffer + aStep;
			
			if (stepBuffer >= step) {
				double percent = position*100/max;
				stepBuffer = 0;
				stepMessage = Math.round(percent) + "%" + "--------" + position + "/" + max;
			}
		}
		else {
			stepMessage = "100%" + "--------" + max + "/" + max;
		}	
		
		if (logger.isDebugEnabled()) {
			logger.debug(stepMessage);
		}
	}
	
	public long getMax() {
		return max;
	}

	public void setMax(long max) {
		clearStep();
		this.max = max;
	}

	public long getMin() {
		return min;
	}

	public void setMin(int min) {
		this.min = min;
	}

	public String getStepMsg() {
		return stepMessage;
	}
	
	public void newTask(String name) {
		taskName = name;
		
		clearMessage();
		clearStep();
		
		appendLine(name + "开始..." + Util.newDateTimeStr("[yyyy年MM月dd日kk时mm分ss秒]"));
	}
	
	public void endTask() {
		appendLine(taskName + "结束");	
		
		if (logger.isDebugEnabled()) {
			logger.debug("end task: " + taskName);
		}	
	}

	public void newPhase(String code, String phaseName) {
		if (logger.isDebugEnabled()) {
			logger.debug("new phase:" + phaseName);
		}
		
		lineNo.goAhead();
		String title = lineNo.toString() + phaseName;
		
		Phase phase = new Phase(code, phaseName);
		phaseStack.add(phase);
		appendLine(title);
	}

	public void endPhase() {
		clearStep();
		
		lineNo.goBack();
		Phase phase = phaseStack.pop();
		
		if (logger.isDebugEnabled()) {
			logger.debug("end phase: " + phase.getName());
		}		
	}

	public void clearMessage() {
		messageList.clear();
		lineNo.clear();
	}
	
	private void appendLine(String message) {
		logger.debug(message);
		
		if (message != null) {
			messageList.add(message);
		}
	}
	
	public void appendMesage(String message) {
		logger.debug(message);
		messageList.add("&nbsp;&nbsp;&nbsp;&nbsp;" + message);
	}
	
	public void appendInlineMessage(String message) {
		logger.debug(message);
		
		if (message != null) {
			if (messageList.isEmpty()) {
				messageList.add(message);
				return;
			}
			
			int max = messageList.size() - 1;
			String prior = messageList.get(max);
			prior = prior + "......" + message;
			
			messageList.remove(max);
			messageList.add(prior);
		}
	}

	public void setStepName(String step) {
		messageList.add(step);		
	}

	public List<String> getMessageList() {
		return messageList;
	}
	
	public void terminate(String error) {
		messageList.add(error);
		logger.error(error);
	}
	
	
	private static class LineNo {
		
		private Stack<Integer> noStack;
		private int level;
		private String noString;
		
		
		public LineNo() {
			noStack = new Stack<Integer>();
			level = 0;
			noString = "";
		}

		public void clear() {
			noStack.clear();
			level = 0;
			noString = "";
		}

		public void goBack() {
			level--;

			Integer no = noStack.peek();
			noString = noString.substring(0, noString.length() - (Double.valueOf(Math.log10(no)).intValue() + 1 + 1));
		}

		public void goAhead() {
			level++;
			
			if (noStack.isEmpty()) {
				noStack.push(0);
			}
			
			Integer no = noStack.pop();
			no++;
			noStack.push(no);		
			
			noString = noString + no + ".";
		}
		
		public String toString() {
			StringBuilder result = new StringBuilder();
			
			for (int i = 0; i < level; i++) {
				result.append("&nbsp;&nbsp;");
			}
			
			result.append(noString);
			
			return result.toString();
		}
	}


	public boolean isEmpty() {
		return messageList.isEmpty();
	}

	public List<Phase> getCurrentPhases() {
		List<Phase> resultList = new ArrayList<Phase>();
		
		for (Phase phase: phaseStack) {
			resultList.add(phase);
		}
		
		return resultList;
	}

	@Override
	public void write(EnvelopWriter writer) {
		writer.writeObject(IEnvelop.ResultCode_MessageList, getMessageList());
		writer.writeString(IEnvelop.ResultCode_StepMessage, getStepMsg());
		writer.writeObject(IEnvelop.ResultCode_CurrentPhase, getCurrentPhases());		
	}
}
