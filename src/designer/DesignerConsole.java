package designer;

import designer.engine.DesignContext;
import foundation.callable.Callable;

/**
 * @author kimi
 * @description 设计器入口
 * @date 2018-12-17 15:20
 */


public class DesignerConsole extends Callable {

    @Override
    protected void doReceive(String[] paths) throws Exception {
        String operator = paths[1];
        operator = operator.toLowerCase();

        if (IDesiginerConstant.CALLABLE_METHORD_SYNCHRONIZE.equalsIgnoreCase(operator)) {
            synchronize();
        }

    }

    private void synchronize() {
        try {
            DesignContext designContext = new DesignContext(request);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
