package layout.com.anew.easyItalian.recite

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import layout.com.anew.easyItalian.R

// TODO fix this page and redirect to set a new wordList
class FinishedPage : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_finished)
        Toast.makeText(this,"学习结束",Toast.LENGTH_LONG).show()
//        setSupportActionBar(toolbar)
        val my = DaoOpt.getInstance()
        my.deleteAllData(this)
    }
}
