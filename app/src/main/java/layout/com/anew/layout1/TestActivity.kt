package layout.com.anew.layout1

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_test.*
import kotlinx.android.synthetic.main.content_test.*
import javax.xml.parsers.DocumentBuilderFactory


// next step : design the recite word algorithm
// based on these codes and ReciteWord Activity
class TestActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)
        setSupportActionBar(toolbar)

        button5.setOnClickListener(){
            insert()
            Toast.makeText(this,"Insert successfully", Toast.LENGTH_LONG).show()
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
        //this function get words from xml and turn save to database
        val my = DaoOpt.getInstance()
        my.deleteAllData(this)
        for (i in 0..8){
            val insertWord = getWordsFromXml(i)
            my.insertData(this,insertWord)
        }
    }
    fun getInfo(){
        // this function shows the word info in database
        val my = DaoOpt.getInstance()
        val n1 = editText2.text.toString()
        val n = n1.toInt()
        val getId = my.queryAll(this)?.get(n)?.id
        val getTestWord = my.queryAll(this)?.get(n)?.word
        val getTestWordTran = my.queryAll(this)?.get(n)?.tran
        val getTestTrans = my.queryAll(this)?.get(n)?.trans
        val getExample = my.queryAll(this)?.get(n)?.example

        textView3.setText(getId.toString()+" "+getTestWord.toString()+" "+getTestTrans+" "+getTestWordTran.toString()+" "+getExample.toString())


    }


    fun getWordsFromXml(num:Int):WordForDB{
        val dbf = DocumentBuilderFactory.newInstance()
        val db = dbf.newDocumentBuilder()
        val doc = db.parse(assets.open("wordbook.xml"))
        val wordList = doc.getElementsByTagName("items")
        val thisWord = WordForDB()
        thisWord.id = num.toLong()
        thisWord.incorrectTime=0
        thisWord.appearTime=0
        thisWord.correctTime=0
        if(num<wordList.length) {
            val elem = wordList.item(num)
            thisWord.word = elem.childNodes.item(1).textContent
            thisWord.pos = elem.childNodes.item(3).textContent
            thisWord.tran = elem.childNodes.item(5).textContent
            thisWord.trans = elem.childNodes.item(7).textContent
            thisWord.example = elem.childNodes.item(9).textContent
        }
        else{
            thisWord.word = "None"
            thisWord.pos = "None"
            thisWord.tran = "None"
            thisWord.trans = "None"
            thisWord.example = "None"
        }
        return thisWord
    }

}
