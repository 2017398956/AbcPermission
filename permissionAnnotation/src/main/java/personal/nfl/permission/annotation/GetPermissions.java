package personal.nfl.permission.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by nfl on 2017/11/30.
 */
@Retention(RetentionPolicy.CLASS)
@Target(ElementType.METHOD)
public @interface GetPermissions {
    String[] value() ;
}
