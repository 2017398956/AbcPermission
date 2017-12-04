package personal.nfl.abcpermission;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import com.squareup.leakcanary.LeakCanary;

import personal.nfl.permission.support.constant.ApplicationConstant;
import personal.nfl.permission.support.util.AbcPermission;

/**
 * Created by nfl on 2017/12/3.
 */

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        LeakCanary.install(this);
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
                if(ApplicationConstant.nowActivity == activity){
                    ApplicationConstant.nowActivity = null ;
                }
            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

            }

            @Override
            public void onActivityDestroyed(Activity activity) {

            }
        });
        AbcPermission.permissionListener = new AbcPermission.GetPermissionListener(){
            @Override
            public void cannotRequestAgain(Activity activity, String[] permissions) {
                super.cannotRequestAgain(activity, permissions);
            }

            @Override
            public void exeException(Throwable throwable) {
                super.exeException(throwable);
            }
        } ;
    }
}
