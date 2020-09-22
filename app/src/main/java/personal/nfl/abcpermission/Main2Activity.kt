package personal.nfl.abcpermission

import android.Manifest
import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import personal.nfl.abcpermission.bean.NewBean
import personal.nfl.abcpermission.kotlin.KotlinTest
import personal.nfl.permission.annotation.GetPermissions4AndroidX
import personal.nfl.permission.support.constant.ApplicationConstant

class Main2Activity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//        try {
//            val clazz = Class.forName(NewBean::class.java.name)
//            val field = clazz.getDeclaredField("company")
//            // Toast.makeText(this, "ok", Toast.LENGTH_SHORT).show();
//        } catch (e: ClassNotFoundException) {
//            e.printStackTrace()
//        } catch (e: NoSuchFieldException) {
//            Log.e("NFL", e.toString())
//            e.printStackTrace()
//        }
    }

    fun onClick(view: View) {
        if (view.id == R.id.bn_contact) {
            // Toast.makeText(this, readContacts(), Toast.LENGTH_SHORT).show();
            KotlinTest().test(this)
            //            new PermissionTest().test(this);
        } else if (view.id == R.id.bn_file) {
            readFile()
            //            Cursor cursor = getContentResolver().query(ContactsContract.Contacts.CONTENT_URI,
//                    null, null, null, null);
//            int a = Integer.parseInt("sdfa") ;
        }
    }

    //    @GetPermissions4AndroidX({Manifest.permission.READ_CONTACTS,Manifest.permission.READ_PHONE_STATE})
    private fun readContacts(): String? {
        Toast.makeText(ApplicationConstant.application, "readContacts", Toast.LENGTH_SHORT).show()
        // startActivity(new Intent(this, MainActivity.class));
        return "readContacts"
    }

    @GetPermissions4AndroidX(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    private fun readFile() {
        Toast.makeText(ApplicationConstant.application, "readFile", Toast.LENGTH_SHORT).show()
        return
    }
}