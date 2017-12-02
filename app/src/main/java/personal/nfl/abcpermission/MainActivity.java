package personal.nfl.abcpermission;

import android.Manifest;
import android.app.Activity;
import android.os.Bundle;
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

    @GetPermissions({Manifest.permission.READ_CONTACTS,Manifest.permission.WRITE_EXTERNAL_STORAGE})
    private Activity readContacts() {
        Toast.makeText(this, "exe", Toast.LENGTH_SHORT).show();
        return this ;
    }

    public void onClick(View view) {
        readContacts();
    }

    @GetPermissions({Manifest.permission.READ_CONTACTS,Manifest.permission.WRITE_EXTERNAL_STORAGE})
    private Activity readFile() {
        Toast.makeText(this, "exe", Toast.LENGTH_SHORT).show();
        return this ;
    }
}
