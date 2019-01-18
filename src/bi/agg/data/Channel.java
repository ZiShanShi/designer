package bi.agg.data;

import bi.agg.Defination;
import bi.agg.SQLParameter;
import foundation.util.ContentBuilder;


public class Channel extends Dimension { 

	public Channel() {
		super("channel");
	}

	@Override
	public void onGetFields(ContentBuilder builder, SQLParameter parameter) {
		builder.append("channelcode");
	}
	
	@Override
	public void onGetFilter(ContentBuilder builder, SQLParameter parameter) {
		
	}

	@Override
	public void onGetMapping(ContentBuilder builder, SQLParameter parameter) {
		if ("3month".equalsIgnoreCase(parameter.getCategory())) {
			String code = parameter.getCode();
			builder.append(code + ".channelcode = dest.channelcode");
		}
		else {
			builder.append("sour.channelcode = dest.channelcode");
		}			
	}

	@Override
	public void loadLoopValue(LoopValue loopValue, Defination defination) {
		
	}

	@Override
	public int size() {
		return 1;
	}

	@Override
	public String getWorkingName() {
		return "channel";
	}
	
}
