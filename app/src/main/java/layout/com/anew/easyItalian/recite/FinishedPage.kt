package layout.com.anew.easyItalian.recite

import android.os.Bundle
import android.support.v7.app.AppCompatActivity

import kotlinx.android.synthetic.main.activity_finished.*
import layout.com.anew.easyItalian.R

class FinishedPage : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_finished)
        setSupportActionBar(toolbar)
    }
}
