package personal.nfl.abcpermission.kotlin

import android.content.Context
import android.widget.Toast

open class KotlinTest {

    // @TestAnnotation4Kotlin(["test"])
//    @GetPermissions4AndroidX(Manifest.permission.WRITE_CONTACTS)
    open fun test(context: Context) {
        Toast.makeText(context, "这是 kotlin 中的 方法", Toast.LENGTH_LONG).show()
    }
}