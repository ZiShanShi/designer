package designer.exception;

/**
 * @author kimi
 * @description
 * @date 2018-12-19 10:14
 */


public class DesignerFileException extends DesignerBaseException {

    public static final String FileException = "File not existst-- path:  ";

    public DesignerFileException(String code, String message) {
        super(code, message);
    }

    public DesignerFileException(String message) {

        super("-4",FileException + message);
    }


}
