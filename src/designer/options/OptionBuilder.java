package designer.options;

import designer.engine.DesignContext;

/**
 * @author kimi
 * @description options 生成器
 * @date 2018-12-17 17:11
 * //TODO 滞后一个版本  此版本仅修改已经预设好的options
 */


public class OptionBuilder {

    public static OptionBuilder builder;

    public static OptionBuilder getInstance() {
        if (builder != null) {
            synchronized (OptionBuilder.class) {
                if (builder != null) {
                    builder = new OptionBuilder();
                }
            }
        }
        return builder;
    }

    public static IOption createOptions(DesignContext context) {

        return  null;
    }
}
