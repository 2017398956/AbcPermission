package personal.nfl.permission.support.view;

import android.app.Fragment;
import android.os.Build;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.content.PermissionChecker;

import java.util.ArrayList;
import java.util.List;

import personal.nfl.permission.support.util.AbcPermission;

/**
 * Created by fuli.niu on 2017/12/18.
 */

public class Permission23Fragment extends Fragment {

    private PermissionsHandler permissionsHandler;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        boolean hasPermissions = true;
        for (int result : grantResults) {
            if (result != PermissionChecker.PERMISSION_GRANTED) {
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
                if (PermissionChecker.checkSelfPermission(getContext(), permission) != PermissionChecker.PERMISSION_GRANTED) {
                    if (!shouldShowRequestPermissionRationale(permission)) {
                        settingPermissions.add(permission);
                    } else {
                        rationalePermissions.add(permission);
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
