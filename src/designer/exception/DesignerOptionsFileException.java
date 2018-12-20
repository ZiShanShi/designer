package designer.exception;

import foundation.util.Util;

/**
 * @author kimi
 * @description options 配置错误
 * @date 2018-12-19 14:01
 */


public class DesignerOptionsFileException extends RuntimeException{
    protected String code;
    protected String message;
    public static final String designerOptionError = "配置文件错误:";


    public DesignerOptionsFileException(String message) {
        this.code = "-5";
        this.message = designerOptionError + message;
    }

    public DesignerOptionsFileException(String filePath, String element, String message) {
        StringBuilder builder = new StringBuilder(designerOptionError);
        builder.append("---filePath:");
        builder.append(filePath);
        builder.append("--- 元素：");
        builder.append(element);
        builder.append("---message:");
        builder.append(message);
        this.code = "-5";
        this.message = builder.toString();
    }

    @Override
    public String getMessage() {
        if (!Util.isEmptyStr(message) && !Util.isEmptyStr(code)) {
            return "errorcode:" + code + ";message:" + message;
        }
        return super.getMessage();
    }
}
