package org.cnbi.util.writeExcel.jxls2.exception;

/**
 * Created by FangQiang on 2018/9/9/009
 */
public class ObjectNotExistException extends RuntimeException{
    public ObjectNotExistException() {
        super();
    }

    public ObjectNotExistException(String message) {
        super( message );
    }

    public ObjectNotExistException(String message, Throwable cause) {
        super( message, cause );
    }

    public ObjectNotExistException(Throwable cause) {
        super( cause );
    }

    protected ObjectNotExistException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super( message, cause, enableSuppression, writableStackTrace );
    }
}
