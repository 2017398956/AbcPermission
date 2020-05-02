package personal.nfl.permission.support.view;

import android.app.Fragment;
import android.content.Context;
import android.os.Build;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import personal.nfl.permission.support.util.AbcPermission;

// import androidx.core.content.PermissionChecker;
//import android.support.v4.content.PermissionChecker;

/**
 * Created by fuli.niu on 2017/12/18.
 */

public class Permission23Fragment extends Fragment {

    private PermissionsHandler permissionsHandler;
    // 由于 androidx 的原因这里使用反射
    private Class permissionChecker;
    private int PERMISSION_GRANTED;
    private Method checkSelfPermission;

    public Permission23Fragment() {
        super();
        try {
            permissionChecker = Class.forName("androidx.core.content.PermissionChecker");
        } catch (ClassNotFoundException e) {
            // 发生异常则说明当前用户使用的不是 androidx
            try {
                permissionChecker = Class.forName("android.support.v4.content.PermissionChecker");
            } catch (ClassNotFoundException ex) {
                ex.printStackTrace();
            }
        }
        if (null != permissionChecker) {
            try {
                PERMISSION_GRANTED = (int) permissionChecker.getField("PERMISSION_GRANTED").get(permissionChecker);
                checkSelfPermission = permissionChecker.getMethod("checkSelfPermission" , Context.class , String.class);
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        boolean hasPermissions = true;
        for (int result : grantResults) {
            if (result != PERMISSION_GRANTED) {
                hasPermissions = false;
                break;
            }
        }
        if (hasPermissions) {
            // 执行被注解的方法
            if (permissionsHandler != null) {
                permissionsHandler.success();
            }
        } else {
            List<String> settingPermissions = new ArrayList<>();
            List<String> rationalePermissions = new ArrayList<>();
            for (String permission : permissions) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission != null) {
                    try {
                        if ((int)checkSelfPermission.invoke(permissionChecker, getContext(), permission) != PERMISSION_GRANTED) {
                            if (!shouldShowRequestPermissionRationale(permission)) {
                                settingPermissions.add(permission);
                            } else {
                                rationalePermissions.add(permission);
                            }
                        }
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    }
                }
            }

            if (rationalePermissions.size() > 0) {
                // 向用户展示需要该权限的原因，用户确定后重新申请
                AbcPermission.permissionListener.showRequestPermissionRationale(this, rationalePermissions.toArray(new String[rationalePermissions.size()]));
                return;
            }

            if (settingPermissions.size() > 0) {
                // 向用户展示需要该权限的原因，用户确定后，打开设置界面
                AbcPermission.permissionListener.cannotRequestAgain(getActivity(), settingPermissions.toArray(new String[settingPermissions.size()]));
            }
        }
    }

    public void requestPermissions(String[] permissions) {
        if (permissions != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(permissions, 1);
            } else {
                if (null != permissionsHandler) {
                    permissionsHandler.success();
                }
            }
        }
    }


    public interface PermissionsHandler {
        void success();

    }

    public void setPermissionsHandler(PermissionsHandler permissionsHandler) {
        this.permissionsHandler = permissionsHandler;
    }
}
