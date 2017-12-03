package personal.nfl.permission.support.util;

import android.util.Log;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;

/**
 * Created by nfl on 2017/12/3.
 * @hide
 */
@Aspect
public class OnRequestPermissionsResultCallbackAspect {
    public static final String METHOD_CALL = "call(* android.support.v4.app.ActivityCompat.OnRequestPermissionsResultCallback.onRequestPermissionsResult(..))";
    public static final String METHOD_EXE = "execution(* android.support.v4.app.ActivityCompat.OnRequestPermissionsResultCallback.onRequestPermissionsResult(..))";

    @Pointcut(METHOD_CALL)
    public void methodCall() {
    }

    @Pointcut(METHOD_EXE)
    public void methodExe() {
    }

    @Before("methodCall()")
    public void beforeCall(JoinPoint joinPoint) {
        Log.i("NFL", "before onRequestPermissionsResult exe");
    }

    @Around("methodExe()")
    public Object aroundExe(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        Log.i("NFL", "in onRequestPermissionsResult exe");
        proceedingJoinPoint.proceed();
        int requestCode = (int) proceedingJoinPoint.getArgs()[0];
        String[] permissions = (String[]) proceedingJoinPoint.getArgs()[1];
        int[] grantResults = (int[]) proceedingJoinPoint.getArgs()[2];
        AbcPermission.permissionListener.onRequestPermissionsResult(requestCode, permissions, grantResults);
        return null;
    }

    @After("methodCall()")
    public void afterCall(JoinPoint joinPoint) {
        Log.i("NFL", "after onRequestPermissionsResult exe");
    }
}
