package layout.com.anew.easyItalian.recite

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.afollestad.materialdialogs.MaterialDialog
import kotlinx.android.synthetic.main.activity_finished.*
import layout.com.anew.easyItalian.MainActivity
import layout.com.anew.easyItalian.R

class FinishedPage : Activity () {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_finished)
    //    Toast.makeText(this,"学习结束",Toast.LENGTH_LONG).show()

        val my = DaoOpt.getInstance()
        my.deleteAllData(this)
        backForFinished.setOnClickListener{
            finish()
            val intent = Intent()
            intent.setClass(this,MainActivity::class.java)
            startActivity(intent)
        }
        MaterialDialog.Builder(this).title("学习结束!")
                .content("学习结束,是否开始新的词书？")
                .negativeText("取消")
                .positiveText("设置").onPositive{_,_->
                    finish()
                    val intent = Intent()
                    intent.setClass(this, SetWordList::class.java)
                    startActivity(intent)
                }.onNegative{
                    _,_ ->
                    finish()
                    val intent = Intent()
                    intent.setClass(this,MainActivity::class.java)
                    startActivity(intent)
                }.show()
    }
}
