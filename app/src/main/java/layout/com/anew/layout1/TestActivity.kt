package layout.com.anew.layout1

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_test.*
import kotlinx.android.synthetic.main.content_test.*



class TestActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)
        setSupportActionBar(toolbar)


        button5.setOnClickListener(){
            insert()
        }
        button6.setOnClickListener(){
            getInfo()
        }
       // initListener()
        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }
    }
    fun insert(){
        val testWord = WordForDB()

        testWord.id = 10086
        testWord.word="Test1"
        testWord.pos="testpos1"
        testWord.tran="test Tran1"
        testWord.trans="测试1"
        testWord.example="this is a test word1"
        testWord.appearTime= 2
        testWord.correctTime=1
        testWord.incorrectTime=1

        val my = DaoOpt.getInstance()
        my.insertData(this,testWord)
    }
    fun getInfo(){
        val my = DaoOpt.getInstance()
        val getId = my.queryForId(this,10086)?.get(0)?.id
        val getTestWord = my.queryAll(this)?.get(0)?.word
        val getTestWordTran = my.queryAll(this)?.get(0)?.tran
        val getExample = my.queryAll(this)?.get(0)?.example

        textView3.setText(getId.toString()+getTestWord.toString()+getTestWordTran.toString()+getExample.toString())


    }

}
