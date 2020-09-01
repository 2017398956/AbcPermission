package personal.nfl.abcpermission;

import android.Manifest;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.lang.reflect.Field;

import personal.nfl.abcpermission.bean.NewBean;
import personal.nfl.abcpermission.kotlin.KotlinTest;
import personal.nfl.permission.annotation.GetPermissions4AndroidX;
import personal.nfl.permission.annotation.TestAnnotation;
import personal.nfl.permission.support.constant.ApplicationConstant;

/**
 * @author nfl
 */
public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        try {
            Class clazz = Class.forName(NewBean.class.getName());
            Field field = clazz.getDeclaredField("company");
            // Toast.makeText(this, "ok", Toast.LENGTH_SHORT).show();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            Log.e("NFL", e.toString());
            e.printStackTrace();
        }


    }

    public void onClick(View view) {
        if (view.getId() == R.id.bn_contact) {
            // Toast.makeText(this, readContacts(), Toast.LENGTH_SHORT).show();
            new KotlinTest().test(this);
        } else if (view.getId() == R.id.bn_file) {
            readFile();
//            Cursor cursor = getContentResolver().query(ContactsContract.Contacts.CONTENT_URI,
//                    null, null, null, null);
//            int a = Integer.parseInt("sdfa") ;
        }
    }

//    @GetPermissions4AndroidX({Manifest.permission.READ_CONTACTS,Manifest.permission.READ_PHONE_STATE})
    private String readContacts() {
        Toast.makeText(ApplicationConstant.application, "readContacts", Toast.LENGTH_SHORT).show();
        // startActivity(new Intent(this, MainActivity.class));
        return "readContacts";
    }

    @GetPermissions4AndroidX({Manifest.permission.WRITE_EXTERNAL_STORAGE})
    private void readFile() {
        Toast.makeText(ApplicationConstant.application, "readFile", Toast.LENGTH_SHORT).show();
        return;
    }

}
