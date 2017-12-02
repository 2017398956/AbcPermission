package personal.nfl.permission.support.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;

import personal.nfl.permission.support.constant.ApplicationConstant;

/**
 * Created by nfl on 2017/12/2.
 */

public class AbcPermission {

    public static GetPermissionListener permissionListener = new GetPermissionListener();

    public static void install(Application application) {
        ApplicationConstant.application = application;
    }

    public static class GetPermissionListener {

        public void cannotRequestAgain(final Activity activity, String[] permissions) {
            StringBuffer stringBuffer = new StringBuffer();
            for (String permission : permissions) {
                stringBuffer.append(permission);
                stringBuffer.append("\n");
            }
            AlertDialog.Builder builder = new AlertDialog.Builder(activity).setTitle("权限申请")
                    .setMessage(stringBuffer.toString())
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
