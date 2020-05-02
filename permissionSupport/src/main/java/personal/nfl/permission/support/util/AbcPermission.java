package personal.nfl.permission.support.util;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import personal.nfl.permission.support.constant.ApplicationConstant;
import personal.nfl.permission.support.view.Permission23Fragment;

/**
 * Created by nfl on 2017/12/2.
 */

public class AbcPermission {

    public static GetPermissionListener permissionListener = new GetPermissionListener();

    public static void install(Application application) {
        if (ApplicationConstant.application == null) {
            ApplicationConstant.application = application;
            ApplicationConstant.application.registerActivityLifecycleCallbacks(new Application.ActivityLifecycleCallbacks() {
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

                }

                @Override
                public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

                }

                @Override
                public void onActivityDestroyed(Activity activity) {

                }
            });
        }
    }

    public static void install(Activity activity) {
        ApplicationConstant.nowActivity = activity ;
        install(activity.getApplication());
    }

    public static class GetPermissionListener {

        /**
         * 当用户拒绝且没屏蔽提示时，调用这个方法，如弹窗告知用户使用权限的理由，若用户点击确定后，
         * 可使用 {@link Permission23Fragment#requestPermissions(String[])} 重新申请权限；
         * 不要使用 {@link Fragment#requestPermissions(String[], int)} 。
         *
         * @param permission23Fragment
         * @param permissions
         */
        public void showRequestPermissionRationale(final Permission23Fragment permission23Fragment, final String[] permissions) {
            AlertDialog.Builder builder = new AlertDialog.Builder(permission23Fragment.getActivity()).setTitle("权限申请")
                    .setMessage(getMessage(permissions , false))
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            permission23Fragment.requestPermissions(permissions);
                        }
                    })
                    .setNegativeButton("取消", null);
            builder.create().show();

        }

        /**
         * 当用户不给权限且选择了不再提示后，会执行这个方法
         *
         * @param activity
         * @param permissions 用户拒绝授予的权限
         */
        public void cannotRequestAgain(final Activity activity, String[] permissions) {
            AlertDialog.Builder builder = new AlertDialog.Builder(activity).setTitle("权限申请")
                    .setMessage(getMessage(permissions , true))
                    .setPositiveButton("去设置", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Uri uri = Uri.parse("package:" + activity.getPackageName());
                            Intent intent = new Intent("android.settings.APPLICATION_DETAILS_SETTINGS", uri);
                            activity.startActivity(intent);
                        }
                    })
                    .setNegativeButton("取消", null);
            builder.create().show();
        }

        /**
         * 为了程序不崩溃，被注解的方法在这里抛出异常
         *
         * @param throwable
         */
        public void exeException(Throwable throwable) {

        }

        /**
         * 根据所需权限获得相应的提示信息
         * @param permissions
         * @return
         */
        private StringBuffer getMessage(String[] permissions , boolean gotoSetting){
            StringBuffer stringBuffer = new StringBuffer();
            for (String permission : permissions) {
                stringBuffer.append(permission);
                stringBuffer.append("\n");
            }
            StringBuffer message = new StringBuffer();
            String allPermissions = stringBuffer.toString();
            message.append("由于无法获取 ");
            if (allPermissions.contains(Manifest.permission.READ_CONTACTS) || allPermissions.contains(Manifest.permission.WRITE_CONTACTS)) {
                message.append("通讯录 ");
                allPermissions = allPermissions.replace(Manifest.permission.READ_CONTACTS + "\n" , "") ;
                allPermissions = allPermissions.replace(Manifest.permission.WRITE_CONTACTS + "\n" , "") ;
            }
            if (allPermissions.contains(Manifest.permission.READ_EXTERNAL_STORAGE) || allPermissions.contains(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                message.append("存储空间 ");
                allPermissions = allPermissions.replace(Manifest.permission.READ_EXTERNAL_STORAGE + "\n" , "") ;
                allPermissions = allPermissions.replace(Manifest.permission.WRITE_EXTERNAL_STORAGE + "\n" , "") ;
            }
            if (allPermissions.contains(Manifest.permission.READ_PHONE_STATE)) {
                message.append("电话 ");
                allPermissions = allPermissions.replace(Manifest.permission.READ_PHONE_STATE + "\n", "");
            }
            message.append(allPermissions) ;
            message.append("权限，可能无法正常使用，请开启权限后再使用");
            if(gotoSetting){
                message.append("\n\n设置路径：系统设置->");
                message.append(ApplicationConstant.application.getPackageManager().getApplicationLabel(ApplicationConstant.application.getApplicationInfo()));
                message.append("->权限");
            }
            return message ;
        }

    }

    /**
     * 获取应用详情页面intent
     *
     * @return
     */
    private static Intent getAppDetailSettingIntent(Activity activity) {
        Intent localIntent = new Intent();
        localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= 9) {
            localIntent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
            localIntent.setData(Uri.fromParts("package", activity.getPackageName(), null));
        } else if (Build.VERSION.SDK_INT <= 8) {
            localIntent.setAction(Intent.ACTION_VIEW);
            localIntent.setClassName("com.android.settings", "com.android.settings.InstalledAppDetails");
            localIntent.putExtra("com.android.settings.ApplicationPkgName", activity.getPackageName());
        }
        return localIntent;
    }

}
