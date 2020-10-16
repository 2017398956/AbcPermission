package personal.nfl.aoptest.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import personal.nfl.aoptest.R

class AopTestActivity : AppCompatActivity() , View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_aop_test)
    }

    override fun onClick(v: View?) {
        tip()
    }

    private fun tip(){
        Toast.makeText(this , "tip" , Toast.LENGTH_LONG).show()
    }
}