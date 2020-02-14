package personal.nfl.abcpermission;

import android.app.Activity;
import android.app.Application;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import personal.nfl.abcpermission.exception.Cockroach;
import personal.nfl.permission.support.constant.ApplicationConstant;
import personal.nfl.permission.support.util.AbcPermission;

/**
 * Created by nfl on 2017/12/3.
 */

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        AbcPermission.install(this);
        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                ApplicationConstant.nowActivity = activity;
            }

            @Override
            public void onActivityStarted(Activity activity) {
                ApplicationConstant.nowActivity = activity;
            }

            @Override
            public void onActivityResumed(Activity activity) {
                ApplicationConstant.nowActivity = activity;
            }

            @Override
            public void onActivityPaused(Activity activity) {

            }

            @Override
            public void onActivityStopped(Activity activity) {
                if (ApplicationConstant.nowActivity == activity) {
                    ApplicationConstant.nowActivity = null;
                }
            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

            }

            @Override
            public void onActivityDestroyed(Activity activity) {

            }
        });
        AbcPermission.permissionListener = new AbcPermission.GetPermissionListener() {
            @Override
            public void cannotRequestAgain(Activity activity, String[] permissions) {
                super.cannotRequestAgain(activity, permissions);
            }

            @Override
            public void exeException(Throwable throwable) {
                super.exeException(throwable);
            }
        };
        Cockroach.install(new Cockroach.ExceptionHandler() {
            @Override
            public void handlerException(Thread thread, Throwable throwable) {
                throwable.printStackTrace();
                String ret = null;
                try {
                    ByteArrayOutputStream out = new ByteArrayOutputStream();
                    PrintStream pout = null;
                    pout = new PrintStream(out);
                    throwable.printStackTrace(pout);
                    ret = new String(out.toByteArray());
                    out.close();
                    pout.close();
                } catch (Exception e) {
                }
                if (!TextUtils.isEmpty(ret)) {
                    String regex = "android.permission.[A-Z_]{1,}" ;
                    Pattern p = Pattern.compile(regex);
                    Matcher matcher = p.matcher(ret);
                    if (matcher.find()) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if(ApplicationConstant.nowActivity.shouldShowRequestPermissionRationale(matcher.group())){
                                Toast.makeText(MyApplication.this, "打开设置界面", Toast.LENGTH_SHORT).show();
                            }else {
                                Toast.makeText(MyApplication.this, "申请权限" + matcher.group(), Toast.LENGTH_SHORT).show();
                                ApplicationConstant.nowActivity.requestPermissions(new String[]{matcher.group()}, 100);
                            }
                        }
                    }else{
                        Toast.makeText(MyApplication.this, "没匹配到", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}
