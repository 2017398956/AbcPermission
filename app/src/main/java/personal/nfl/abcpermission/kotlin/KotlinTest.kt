package personal.nfl.abcpermission.kotlin

import android.Manifest
import android.content.Context
import android.widget.Toast
import personal.nfl.permission.annotation.GetPermissions4AndroidX
import personal.nfl.permission.annotation.TestAnnotation4Kotlin

class KotlinTest {

    // @TestAnnotation4Kotlin(["test"])
    @GetPermissions4AndroidX(*[Manifest.permission.WRITE_CONTACTS])
    fun test(context : Context){
        Toast.makeText(context , "这是 kotlin 中的 方法" , Toast.LENGTH_LONG).show()
    }
}