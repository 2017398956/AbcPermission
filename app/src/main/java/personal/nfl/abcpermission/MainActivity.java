package personal.nfl.abcpermission;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.lang.reflect.Field;

import personal.nfl.abcpermission.bean.NewBean;
import personal.nfl.permission.annotation.GetPermissions4AndroidX;
import personal.nfl.permission.support.constant.ApplicationConstant;

/**
 * @author nfl
 */
public class MainActivity extends Activity {

    private final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            //判断是否有管理外部存储的权限
            if (!Environment.isExternalStorageManager()) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                intent.setData(Uri.fromParts("package", getPackageName(), null));
                // startActivity(intent);
            }
        }

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

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        TextView textView = new TextView(this);
        textView.setPadding(10,10,10,10);
        textView.setBackgroundResource(R.color.test);
        textView.setText("test read file.");
        textView.setOnClickListener(v -> readFile());
        AlertDialog alertDialog = builder.setTitle("1234567").setPositiveButton("权限测试", (dialog, which) -> {

        }).setView(textView).create();
        if (false){
            alertDialog.show();
        }
//        testStackOverFlow();
    }

    public void onClick(View view) {
        if (view.getId() == R.id.bn_contact) {
            /**
             * FIXME:这里的 Toast 会直接弹出，不会等待权限申请成功后才展示。由于首次还未授予权限，所以展示的内容为:
             *  start read contacts and result:null
             * TODO:这里有个 bug 这样使用会导致权限判断逻辑不能正常执行，而直接调用 readContacts 是可以的
             */
             Toast.makeText(this, "start read contacts and result:" + readContacts(), Toast.LENGTH_SHORT).show();
//            new KotlinTest().test(this);
//            new PermissionTest().test(this);
            // startActivity(new Intent(this , AopTestActivity.class));
        } else if (view.getId() == R.id.bn_file) {
            readFile();
//            Cursor cursor = getContentResolver().query(ContactsContract.Contacts.CONTENT_URI,
//                    null, null, null, null);
//            int a = Integer.parseInt("sdfa") ;
        } else if (view.getId() == R.id.bn_location) {
            getLocation();
        } else if (view.getId() == R.id.bn_clipboard) {
            getClipboardContents();
        }
    }

    @GetPermissions4AndroidX({Manifest.permission.ACCESS_FINE_LOCATION})
    private String getLocation() {
        Toast.makeText(ApplicationConstant.application, "getLocation", Toast.LENGTH_SHORT).show();
        return "shang hai city";
    }

    @GetPermissions4AndroidX({Manifest.permission.READ_CONTACTS, Manifest.permission.READ_PHONE_STATE})
    private String readContacts() {
        Toast.makeText(ApplicationConstant.application, "readContacts function", Toast.LENGTH_SHORT).show();
        // startActivity(new Intent(this, MainActivity.class));
        return "readContacts result";
    }

    @GetPermissions4AndroidX({Manifest.permission.WRITE_EXTERNAL_STORAGE})
    private void readFile() {
        String filePath = "/sdcard/Download/adb.txt";
        File file = new File(filePath);
        Log.e("NFL", "filePath:" + file.getAbsolutePath());
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
            bufferedReader.readLine();
            Log.e("NFL", "readLine:" + bufferedReader.readLine());
        } catch (Exception e) {
            Log.e("NFL", "exception:" + e.getMessage());
            throw new RuntimeException(e);
        }
        Toast.makeText(ApplicationConstant.application, "readFile", Toast.LENGTH_SHORT).show();
        return;
    }

    private void getClipboardContents() {
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clipData = clipboard.getPrimaryClip();
        ClipData.Item item;
        if (clipData != null && clipData.getItemCount() > 0) {
            for (int i = 0; i < clipData.getItemCount(); i++) {
                item = clipData.getItemAt(i);
                CharSequence text = item.getText();
                String pasteString = text.toString();
                Log.d(TAG, "getFromClipboard text=" + pasteString);
            }
        }
    }

    private void testStackOverFlow(){
        testStackOverFlow();
    }

}
