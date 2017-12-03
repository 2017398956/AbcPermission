package personal.nfl.abcpermission;

import android.Manifest;
import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import personal.nfl.permission.annotation.GetPermissions;
import personal.nfl.permission.support.constant.ApplicationConstant;
import personal.nfl.permission.support.util.AbcPermission;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AbcPermission.install(getApplication());
        ApplicationConstant.nowActivity = this;
    }

    @GetPermissions({Manifest.permission.READ_CONTACTS})
    private Activity readContacts() {
        Toast.makeText(this, "readContacts", Toast.LENGTH_SHORT).show();
        return this;
    }

    public void onClick(View view) {
        readContacts();
        readFile();
    }

    @GetPermissions({Manifest.permission.WRITE_EXTERNAL_STORAGE})
    private void readFile() {
        Toast.makeText(this, "readFile", Toast.LENGTH_SHORT).show();
        return;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
