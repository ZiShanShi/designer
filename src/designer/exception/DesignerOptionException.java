package designer.exception;

/**
 * @author kimi
 * @description
 * @date 2018-12-19 10:14
 */


public class DesignerOptionException extends DesignerBaseException {

    public static final String FileException = "Option对象有问题:  ";

    public DesignerOptionException(String code, String message) {
        super(code, message);
    }

    public DesignerOptionException(String message) {

        super("-6",FileException + message);
    }


}
