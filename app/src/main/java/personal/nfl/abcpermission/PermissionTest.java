package personal.nfl.abcpermission;

import android.Manifest;
import android.content.Context;
import android.widget.Toast;

import personal.nfl.permission.annotation.GetPermissions4AndroidX;

public class PermissionTest {

    @GetPermissions4AndroidX(Manifest.permission.WRITE_CONTACTS)
    public void test(Context context){
        Toast.makeText(context, "这是 PermissionTest 中的 方法", Toast.LENGTH_LONG).show();
    }
}
