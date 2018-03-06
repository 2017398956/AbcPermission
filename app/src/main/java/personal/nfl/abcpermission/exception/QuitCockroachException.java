package personal.nfl.abcpermission.exception;

/**
 * Created by fuli.niu on 2018/3/5.
 */

public final class QuitCockroachException extends RuntimeException {
    public QuitCockroachException(String message) {
        super(message);
    }
}
