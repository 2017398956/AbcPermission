package personal.nfl.abcpermission;

import android.Manifest;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import personal.nfl.permission.annotation.GetPermissionsAuto;
import personal.nfl.permission.support.constant.ApplicationConstant;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @GetPermissionsAuto({Manifest.permission.READ_CONTACTS})
    private String readContacts() {
        Toast.makeText(ApplicationConstant.application, "readContacts", Toast.LENGTH_SHORT).show();
        // startActivity(new Intent(this, MainActivity.class));
        return "";
    }

    public void onClick(View view) {
        if (view.getId() == R.id.bn_contact) {
            readContacts();
        } else if (view.getId() == R.id.bn_file) {
            readFile();
        }
    }

    @GetPermissionsAuto({Manifest.permission.WRITE_EXTERNAL_STORAGE})
    private void readFile() {
        Toast.makeText(ApplicationConstant.application, "readFile", Toast.LENGTH_SHORT).show();
        return;
    }
}
