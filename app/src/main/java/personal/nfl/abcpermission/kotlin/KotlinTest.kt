package personal.nfl.abcpermission.kotlin

import android.Manifest
import android.content.Context
import android.widget.Toast
import personal.nfl.permission.annotation.GetPermissions4AndroidX

open class KotlinTest {

    // @TestAnnotation4Kotlin(["test"])
//    @GetPermissions4AndroidX(Manifest.permission.CALL_PHONE)
    open fun test(context: Context) {
        Toast.makeText(context, "这是 kotlin 中的 方法", Toast.LENGTH_LONG).show()
    }
}